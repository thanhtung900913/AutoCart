package com.n2t.autocart.modules.cart.dto;

public record AddCartItemRequest(
        Integer productId,
        Integer quantity
) {
}
