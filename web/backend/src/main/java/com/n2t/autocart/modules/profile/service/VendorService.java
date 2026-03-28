package com.n2t.autocart.modules.profile.service;

import com.n2t.autocart.modules.account.entity.User;
import com.n2t.autocart.modules.account.repository.UserRepository;
import com.n2t.autocart.modules.profile.dto.CreateVendorProfileRequest;
import com.n2t.autocart.modules.profile.dto.UpdateMyVendorProfileRequest;
import com.n2t.autocart.modules.profile.dto.VendorProfileResponse;
import com.n2t.autocart.modules.profile.entity.Vendor;
import com.n2t.autocart.modules.profile.repository.VendorRepository;
import com.n2t.autocart.security.SecurityUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class VendorService {
    private final VendorRepository vendorRepository;
    private final UserRepository userRepository;

    public VendorService(VendorRepository vendorRepository, UserRepository userRepository) {
        this.vendorRepository = vendorRepository;
        this.userRepository = userRepository;
    }

    public VendorProfileResponse handleCreateVendorProfile(CreateVendorProfileRequest request) {
        if (request.name() == null || request.name().isBlank()) {
            throw new RuntimeException("Vendor name is required");
        }

        String email = SecurityUtil.getCurrentUserLogin()
                .orElseThrow(() -> new RuntimeException("Unauthorized"));
        if (vendorRepository.findByUser_Email(email).isPresent()) {
            throw new RuntimeException("Vendor profile already exists");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Vendor vendor = new Vendor();
        vendor.setName(request.name().trim());
        vendor.setPhone(request.phone());
        vendor.setDescription(request.description());
        vendor.setLogoUrl(request.logoUrl());
        vendor.setDateJoin(LocalDate.now());
        vendor.setUser(user);

        Vendor savedVendor = vendorRepository.save(vendor);
        return mapToProfileResponse(savedVendor);
    }

    public VendorProfileResponse handleGetMyVendorProfile() {
        Vendor vendor = getCurrentVendor();
        return mapToProfileResponse(vendor);
    }

    public VendorProfileResponse handleUpdateMyVendorProfile(UpdateMyVendorProfileRequest request) {
        if (request.name() == null || request.name().isBlank()) {
            throw new RuntimeException("Vendor name is required");
        }

        Vendor vendor = getCurrentVendor();
        vendor.setName(request.name().trim());
        vendor.setDescription(request.description());
        vendor.setLogoUrl(request.logoUrl());

        Vendor updatedVendor = vendorRepository.save(vendor);
        return mapToProfileResponse(updatedVendor);
    }

    private Vendor getCurrentVendor() {
        String email = SecurityUtil.getCurrentUserLogin()
                .orElseThrow(() -> new RuntimeException("Unauthorized"));
        return vendorRepository.findByUser_Email(email)
                .orElseThrow(() -> new RuntimeException("Vendor profile not found"));
    }
    private VendorProfileResponse mapToProfileResponse(Vendor vendor) {
        return new VendorProfileResponse(
                vendor.getId(),
                vendor.getName(),
                vendor.getPhone(),
                vendor.getDescription(),
                vendor.getLogoUrl(),
                vendor.getDateJoin()
        );
    }
}
