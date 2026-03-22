package com.n2t.autocart.modules.address.controller;

import com.n2t.autocart.modules.address.dto.DistrictDTO;
import com.n2t.autocart.modules.address.entity.District;
import com.n2t.autocart.modules.address.service.DistrictService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/address")
public class DistrictController {
    private final DistrictService districtService;

    public DistrictController(DistrictService districtService) {
        this.districtService = districtService;
    }
    @GetMapping("/provinces/{provinceId}/districts")
    public ResponseEntity<List<DistrictDTO>> getDistrictByProvinceId(@PathVariable Integer provinceId){
        List<District> districts = districtService.getDistrict(provinceId);
        if(districts.isEmpty()){
            throw new RuntimeException();
        }
        List<DistrictDTO> districtDTOS = districts.stream()
                .map(district -> new DistrictDTO(
                        district.getDistrictId(),
                        district.getDistrictName(),
                        district.getDistrictNameEn(),
                        district.getType()
                )).toList();
        return ResponseEntity.ok().body(districtDTOS);
    }
}
