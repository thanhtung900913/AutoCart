package com.n2t.autocart.modules.profile.dto;

public record CustomerProfileResponse(
    Integer id,
    String phone,
    String fullname,
    Integer age,
    String avatarUrl
) {
}
