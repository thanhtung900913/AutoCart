package com.n2t.autocart.modules.account.dto;

public record LoginRequestDTO(
        String email,
        String password
) {
}
