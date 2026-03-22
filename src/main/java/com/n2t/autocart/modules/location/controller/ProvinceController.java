package com.n2t.autocart.modules.address.controller;

import com.n2t.autocart.common.anotation.ApiMessage;
import com.n2t.autocart.modules.address.dto.ProvinceDTO;
import com.n2t.autocart.modules.address.service.ProvinceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/address")
public class ProvinceController {
    private final ProvinceService provinceService;

    public ProvinceController(ProvinceService provinceService) {
        this.provinceService = provinceService;
    }
    @GetMapping("/provinces")
    public ResponseEntity<List<ProvinceDTO>> getProvinces(){
        List<ProvinceDTO> provinceDTOList = provinceService.getAllProvinces()
                .stream()
                .map(province -> new ProvinceDTO(
                        province.getProvinceId(),
                        province.getProvinceName(),
                        province.getProvinceNameEn(),
                        province.getType()
                )).toList();
        return ResponseEntity.ok().body(provinceDTOList);
    }
}
