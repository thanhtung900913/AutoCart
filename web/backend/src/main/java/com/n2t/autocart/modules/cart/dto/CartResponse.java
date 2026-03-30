package com.n2t.autocart.modules.cart.dto;

import java.math.BigDecimal;
import java.util.List;

public record CartResponse(
        Integer cartId,
        Integer itemsTotal,
        BigDecimal grandTotal,
        List<CartItemResponse> items
) {
}
