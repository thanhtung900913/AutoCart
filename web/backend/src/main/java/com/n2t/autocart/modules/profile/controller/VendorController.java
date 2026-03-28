package com.n2t.autocart.modules.profile.controller;

import com.n2t.autocart.common.anotation.ApiMessage;
import com.n2t.autocart.modules.profile.dto.CreateVendorProfileRequest;
import com.n2t.autocart.modules.profile.dto.UpdateMyVendorProfileRequest;
import com.n2t.autocart.modules.profile.dto.VendorProfileResponse;
import com.n2t.autocart.modules.profile.service.VendorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/profiles/vendors")
public class VendorController {
    private final VendorService vendorService;

    public VendorController(VendorService vendorService) {
        this.vendorService = vendorService;
    }

    @PostMapping
    @ApiMessage("Create vendor profile successfully")
    public ResponseEntity<VendorProfileResponse> createVendorProfile(@RequestBody CreateVendorProfileRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(vendorService.handleCreateVendorProfile(request));
    }

    @GetMapping("/me")
    @ApiMessage("Get vendor profile successfully")
    public ResponseEntity<VendorProfileResponse> getMyVendorProfile() {
        return ResponseEntity.ok(vendorService.handleGetMyVendorProfile());
    }

    @PutMapping("/me")
    @ApiMessage("Update vendor profile successfully")
    public ResponseEntity<VendorProfileResponse> updateMyVendorProfile(@RequestBody UpdateMyVendorProfileRequest request) {
        return ResponseEntity.ok(vendorService.handleUpdateMyVendorProfile(request));
    }
}
