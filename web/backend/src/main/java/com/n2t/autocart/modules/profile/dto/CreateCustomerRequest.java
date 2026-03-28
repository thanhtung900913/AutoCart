package com.n2t.autocart.modules.profile.dto;

public record CreateCustomerRequest(
    String phone,
    String fullname,
    Integer age,
    String avatarUrl,
    Integer userId
) {
}
