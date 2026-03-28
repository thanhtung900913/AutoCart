package com.n2t.autocart.modules.product.controller;

import com.n2t.autocart.common.anotation.ApiMessage;
import com.n2t.autocart.modules.product.dto.ProductResponse;
import com.n2t.autocart.modules.product.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    @ApiMessage("Get products successfully")
    public ResponseEntity<Page<ProductResponse>> getProducts(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) Integer vendorId,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "productId") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        return ResponseEntity.ok(productService.handleGetProducts(
                keyword,
                categoryId,
                vendorId,
                brand,
                minPrice,
                maxPrice,
                page,
                size,
                sortBy,
                sortDir
        ));
    }

    @GetMapping("/{id}")
    @ApiMessage("Get product detail successfully")
    public ResponseEntity<ProductResponse> getProductDetail(@PathVariable Integer id) {
        return ResponseEntity.ok(productService.handleGetProductDetail(id));
    }
}
