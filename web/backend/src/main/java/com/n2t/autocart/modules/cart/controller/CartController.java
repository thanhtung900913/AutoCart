package com.n2t.autocart.modules.cart.controller;

import com.n2t.autocart.common.anotation.ApiMessage;
import com.n2t.autocart.modules.cart.dto.AddCartItemRequest;
import com.n2t.autocart.modules.cart.dto.CartResponse;
import com.n2t.autocart.modules.cart.dto.UpdateCartItemRequest;
import com.n2t.autocart.modules.cart.service.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/carts")
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/me")
    @ApiMessage("Get my cart successfully")
    public ResponseEntity<CartResponse> getMyCart() {
        return ResponseEntity.ok(cartService.handleGetMyCart());
    }

    @PostMapping("/me/items")
    @ApiMessage("Add item to cart successfully")
    public ResponseEntity<CartResponse> addItemToMyCart(@RequestBody AddCartItemRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cartService.handleAddItemToMyCart(request));
    }

    @PutMapping("/me/items/{itemId}")
    @ApiMessage("Update cart item quantity successfully")
    public ResponseEntity<CartResponse> updateMyCartItem(
            @PathVariable Integer itemId,
            @RequestBody UpdateCartItemRequest request
    ) {
        return ResponseEntity.ok(cartService.handleUpdateMyCartItem(itemId, request));
    }

    @DeleteMapping("/me/items/{itemId}")
    @ApiMessage("Delete cart item successfully")
    public ResponseEntity<CartResponse> deleteMyCartItem(@PathVariable Integer itemId) {
        return ResponseEntity.ok(cartService.handleDeleteMyCartItem(itemId));
    }
}
