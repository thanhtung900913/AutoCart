package com.n2t.autocart.modules.cart.service;

import com.n2t.autocart.modules.cart.dto.AddCartItemRequest;
import com.n2t.autocart.modules.cart.dto.CartItemResponse;
import com.n2t.autocart.modules.cart.dto.CartResponse;
import com.n2t.autocart.modules.cart.dto.UpdateCartItemRequest;
import com.n2t.autocart.modules.cart.entity.Cart;
import com.n2t.autocart.modules.cart.repository.CartRepository;
import com.n2t.autocart.modules.product.entity.CartProduct;
import com.n2t.autocart.modules.product.entity.Product;
import com.n2t.autocart.modules.product.repository.CartProductRepository;
import com.n2t.autocart.modules.product.repository.ProductRepository;
import com.n2t.autocart.security.SecurityUtil;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CartService {
    private final CartRepository cartRepository;
    private final CartProductRepository cartProductRepository;
    private final ProductRepository productRepository;

    public CartService(
            CartRepository cartRepository,
            CartProductRepository cartProductRepository,
            ProductRepository productRepository
    ) {
        this.cartRepository = cartRepository;
        this.cartProductRepository = cartProductRepository;
        this.productRepository = productRepository;
    }

    public CartResponse handleGetMyCart() {
        Cart cart = getCurrentUserCart();
        recalculateCartTotals(cart);
        return mapToCartResponse(cart);
    }

    public CartResponse handleAddItemToMyCart(AddCartItemRequest request) {
        if (request.productId() == null) {
            throw new RuntimeException("Product id is required");
        }
        if (request.quantity() == null || request.quantity() <= 0) {
            throw new RuntimeException("Quantity must be greater than 0");
        }

        Product product = productRepository.findById(request.productId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Cart cart = getCurrentUserCart();
        // Check if the product is already in the cart, if so update the quantity, otherwise create a new cart item
        CartProduct cartProduct = cartProductRepository
                .findByCart_CartIdAndProduct_ProductId(cart.getCartId(), request.productId())
                .orElseGet(() -> {
                    CartProduct item = new CartProduct();
                    item.setCart(cart);
                    item.setProduct(product);
                    item.setQuantity(0);
                    return item;
                });

        int newQuantity = cartProduct.getQuantity() + request.quantity();
        validateStock(product, newQuantity);

        cartProduct.setQuantity(newQuantity);
        cartProductRepository.save(cartProduct);

        recalculateCartTotals(cart);
        return mapToCartResponse(cart);
    }

    public CartResponse handleUpdateMyCartItem(Integer itemId, UpdateCartItemRequest request) {
        if (itemId == null) {
            throw new RuntimeException("Item id is required");
        }
        if (request.quantity() == null || request.quantity() <= 0) {
            throw new RuntimeException("Quantity must be greater than 0");
        }

        Cart cart = getCurrentUserCart();
        CartProduct cartProduct = cartProductRepository
                .findByCart_CartIdAndCartProductId(cart.getCartId(), itemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        validateStock(cartProduct.getProduct(), request.quantity());
        cartProduct.setQuantity(request.quantity());
        cartProductRepository.save(cartProduct);

        recalculateCartTotals(cart);
        return mapToCartResponse(cart);
    }

    public CartResponse handleDeleteMyCartItem(Integer itemId) {
        if (itemId == null) {
            throw new RuntimeException("Item id is required");
        }

        Cart cart = getCurrentUserCart();
        CartProduct cartProduct = cartProductRepository
                .findByCart_CartIdAndCartProductId(cart.getCartId(), itemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        cartProductRepository.delete(cartProduct);
        recalculateCartTotals(cart);
        return mapToCartResponse(cart);
    }

    private Cart getCurrentUserCart() {
        String email = getCurrentUserEmail();
        return cartRepository.findByCustomer_User_Email(email)
                .orElseThrow(() -> new RuntimeException("User cart not found"));
    }


    private String getCurrentUserEmail() {
        return SecurityUtil.getCurrentUserLogin()
                .orElseThrow(() -> new RuntimeException("Unauthorized"));
    }

    private void validateStock(Product product, int quantity) {
        if (product.getStock() == null || product.getStock() < quantity) {
            throw new RuntimeException("Product is out of stock");
        }
    }

    private void recalculateCartTotals(Cart cart) {
        List<CartProduct> cartItems = cartProductRepository.findAllByCart_CartId(cart.getCartId());

        int itemsTotal = cartItems.stream()
                .mapToInt(CartProduct::getQuantity)
                .sum();

        BigDecimal grandTotal = cartItems.stream()
                .map(item -> item.getProduct().getOriginalPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        cart.setItemsTotal(itemsTotal);
        cart.setGrandTotal(grandTotal);
        cartRepository.save(cart);
    }

    private CartResponse mapToCartResponse(Cart cart) {
        List<CartItemResponse> items = cartProductRepository.findAllByCart_CartId(cart.getCartId()).stream()
                .map(item -> new CartItemResponse(
                        item.getCartProductId(),
                        item.getProduct().getProductId(),
                        item.getProduct().getProductName(),
                        item.getProduct().getOriginalPrice(),
                        item.getQuantity(),
                        item.getProduct().getOriginalPrice().multiply(BigDecimal.valueOf(item.getQuantity()))
                ))
                .toList();

        return new CartResponse(
                cart.getCartId(),
                cart.getItemsTotal(),
                cart.getGrandTotal(),
                items
        );
    }
}
