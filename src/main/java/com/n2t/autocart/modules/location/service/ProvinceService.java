package com.n2t.autocart.modules.location.service;

import com.n2t.autocart.modules.location.entity.Province;
import com.n2t.autocart.modules.location.repository.ProvinceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProvinceService {
    private final ProvinceRepository provinceRepository;

    public ProvinceService(ProvinceRepository provinceRepository) {
        this.provinceRepository = provinceRepository;
    }
    public List<Province> getAllProvinces(){
        return provinceRepository.findAll();
    }
}
