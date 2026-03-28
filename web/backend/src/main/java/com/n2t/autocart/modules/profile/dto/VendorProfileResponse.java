package com.n2t.autocart.modules.profile.dto;

import java.time.LocalDate;

public record VendorProfileResponse(
    Integer id,
    String name,
    String phone,
    String description,
    String logoUrl,
    LocalDate dateJoin
) {
}
