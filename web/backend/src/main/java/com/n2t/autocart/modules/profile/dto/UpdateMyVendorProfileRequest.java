package com.n2t.autocart.modules.profile.dto;

public record UpdateMyVendorProfileRequest(
    String name,
    String description,
    String logoUrl
) {
}
