package com.n2t.autocart.modules.location.service;

import com.n2t.autocart.modules.location.entity.Ward;
import com.n2t.autocart.modules.location.repository.WardRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WardService {
    private final WardRepository wardRepository;

    public WardService(WardRepository wardRepository) {
        this.wardRepository = wardRepository;
    }

    public List<Ward> getAllWardsByDistrictId(Integer districtId){
        return wardRepository.findByDistrict_DistrictId(districtId);
    }
}
