package com.n2t.autocart.modules.cart.dto;

public record CreateCategoryRequest(
    String categoryName,
    String slug,
    String categoryImgUrl
) {
}
