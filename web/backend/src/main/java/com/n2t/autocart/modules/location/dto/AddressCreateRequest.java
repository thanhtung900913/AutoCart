package com.n2t.autocart.modules.location.dto;

public record AddressCreateRequest(
    String addressDetail,
    Integer districtId,
    Integer provinceId,
    Integer wardId
) {}
