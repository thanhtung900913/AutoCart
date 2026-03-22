package com.n2t.autocart.modules.location.dto;

public record AddressUpdateRequest(
    String addressDetail,
    Integer wardId,
    Integer districtId,
    Integer provinceId
) {}
