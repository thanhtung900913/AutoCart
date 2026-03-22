package com.n2t.autocart.modules.address.service;

import com.n2t.autocart.modules.address.entity.District;
import com.n2t.autocart.modules.address.repository.DistrictRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DistrictService {
    private final DistrictRepository districtRepository;

    public DistrictService(DistrictRepository districtRepository) {
        this.districtRepository = districtRepository;
    }
    public List<District> getDistrict(Integer provinceId){
        return districtRepository.findByProvince_ProvinceId(provinceId);
    }
}
