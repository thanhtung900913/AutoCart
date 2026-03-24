package com.n2t.autocart.modules.account.dto;

import com.n2t.autocart.modules.location.dto.AddressCreateRequest;

public record UpdateUserAddressRequest(
        Integer id,
        AddressCreateRequest addressCreateRequest
){
}
