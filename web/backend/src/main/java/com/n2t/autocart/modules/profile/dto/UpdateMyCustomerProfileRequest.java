package com.n2t.autocart.modules.profile.dto;

public record UpdateMyCustomerProfileRequest(
    String fullname,
    Integer age,
    String avatarUrl
) {
}
