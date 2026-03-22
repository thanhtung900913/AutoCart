package com.n2t.autocart.modules.location.service;

import com.n2t.autocart.modules.location.dto.AddressCreateRequest;
import com.n2t.autocart.modules.location.dto.AddressUpdateRequest;
import com.n2t.autocart.modules.location.entity.Address;
import com.n2t.autocart.modules.location.repository.AddressRepository;
import com.n2t.autocart.modules.location.repository.DistrictRepository;
import com.n2t.autocart.modules.location.repository.ProvinceRepository;
import com.n2t.autocart.modules.location.repository.WardRepository;
import org.springframework.stereotype.Service;

@Service
public class AddressService {
    private final AddressRepository addressRepository;
    private final WardRepository wardRepository;
    private final ProvinceRepository provinceRepository;
    private final DistrictRepository districtRepository;


    public AddressService(AddressRepository addressRepository, WardRepository wardRepository, ProvinceRepository provinceRepository, DistrictRepository districtRepository) {
        this.addressRepository = addressRepository;
        this.wardRepository = wardRepository;
        this.provinceRepository = provinceRepository;
        this.districtRepository = districtRepository;
    }

    public Address handleCreateAddress(AddressCreateRequest request){
        boolean isValidAddress = wardRepository.
                existsByWardIdAndDistrict_DistrictIdAndProvince_ProvinceId(
                request.wardId(),
                request.districtId(),
                request.provinceId());
        if (isValidAddress){
            // 2. Map dữ liệu để lưu (Dùng getReferenceById để tối ưu, không tốn query SELECT)
            Address address = new Address();
            address.setAddressDetail(request.addressDetail());
            address.setProvince(provinceRepository.getReferenceById(request.provinceId()));
            address.setDistrict(districtRepository.getReferenceById(request.districtId()));
            address.setWard(wardRepository.getReferenceById(request.wardId()));

            // Thực thi lưu xuống DB
            return addressRepository.save(address);
        }
        throw new RuntimeException("Address is not valid");
    }

    public Address handleUpdateAddress(Integer addressId, AddressUpdateRequest request) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Address not found"));

        if (address.getIsDeleted()) {
            throw new RuntimeException("Cannot update a deleted address");
        }

        // Validate if new location combination is valid
        boolean isValidAddress = wardRepository.
                existsByWardIdAndDistrict_DistrictIdAndProvince_ProvinceId(
            request.wardId(),
            request.districtId(),
            request.provinceId());

        if (!isValidAddress) {
            throw new RuntimeException("Address is not valid");
        }

        // Update fields
        address.setAddressDetail(request.addressDetail());
        address.setProvince(provinceRepository.getReferenceById(request.provinceId()));
        address.setDistrict(districtRepository.getReferenceById(request.districtId()));
        address.setWard(wardRepository.getReferenceById(request.wardId()));

        return addressRepository.save(address);
    }

    public void handleDeleteAddress(Integer addressId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Address not found"));

        // Soft delete - mark as deleted instead of removing
        address.setIsDeleted(true);
        addressRepository.save(address);
    }
}
