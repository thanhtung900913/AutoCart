package com.n2t.autocart.modules.location.service;

import com.n2t.autocart.modules.location.entity.District;
import com.n2t.autocart.modules.location.repository.DistrictRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DistrictService {
    private final DistrictRepository districtRepository;

    public DistrictService(DistrictRepository districtRepository) {
        this.districtRepository = districtRepository;
    }
    public List<District> getAllDistrictsByProvinceId(Integer provinceId){
        return districtRepository.findByProvince_ProvinceId(provinceId);
    }
}
