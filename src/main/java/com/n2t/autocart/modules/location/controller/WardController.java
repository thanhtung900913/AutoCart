package com.n2t.autocart.modules.location.controller;

import com.n2t.autocart.modules.location.dto.WardDTO;
import com.n2t.autocart.modules.location.entity.Ward;
import com.n2t.autocart.modules.location.service.WardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/locations")
public class WardController {
    private final WardService wardService;

    public WardController(WardService wardService) {
        this.wardService = wardService;
    }
    @GetMapping("/districts/{districtId}/wards")
    public ResponseEntity<List<WardDTO>> getAllWardsByDistrictId(@PathVariable Integer districtId){
        List<Ward> wards = wardService.getAllWardsByDistrictId(districtId);
        if (!wards.isEmpty()){
            List<WardDTO> wardDTOS = wards.stream()
                    .map(ward -> new WardDTO(
                            ward.getWardId(),
                            ward.getWardName(),
                            ward.getWardNameEn(),
                            ward.getType()
                    )).toList();
            return ResponseEntity.ok().body(wardDTOS);
        }
        throw new RuntimeException();
    }
}
