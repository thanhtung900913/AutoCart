package com.n2t.autocart.modules.profile.dto;

public record CreateVendorProfileRequest(
    String name,
    String phone,
    String description,
    String logoUrl
) {
}
