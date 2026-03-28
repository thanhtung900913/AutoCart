package com.n2t.autocart.modules.product.service;

import com.n2t.autocart.modules.product.dto.ProductResponse;
import com.n2t.autocart.modules.product.entity.Product;
import com.n2t.autocart.modules.product.repository.ProductRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Page<ProductResponse> handleGetProducts(
            String keyword,
            Integer categoryId,
            Integer vendorId,
            String brand,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            int page,
            int size,
            String sortBy,
            String sortDir
    ) {
        String sortField = (sortBy == null || sortBy.isBlank()) ? "productId" : sortBy;
        Sort sort = "asc".equalsIgnoreCase(sortDir)
                ? Sort.by(sortField).ascending()
                : Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(Math.max(page, 0), Math.max(size, 1), sort);

        return productRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (keyword != null && !keyword.isBlank()) {
                String keywordValue = "%" + keyword.trim().toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("productName")), keywordValue),
                        cb.like(cb.lower(root.get("productDescription")), keywordValue),
                        cb.like(cb.lower(root.get("slug")), keywordValue),
                        cb.like(cb.lower(root.get("brand")), keywordValue)
                ));
            }

            if (categoryId != null) {
                predicates.add(cb.equal(root.get("category").get("categoryId"), categoryId));
            }

            if (vendorId != null) {
                predicates.add(cb.equal(root.get("vendor").get("id"), vendorId));
            }

            if (brand != null && !brand.isBlank()) {
                predicates.add(cb.equal(cb.lower(root.get("brand")), brand.trim().toLowerCase()));
            }

            if (minPrice != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("originalPrice"), minPrice));
            }

            if (maxPrice != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("originalPrice"), maxPrice));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        }, pageable).map(this::mapToResponse);
    }

    public ProductResponse handleGetProductDetail(Integer id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return mapToResponse(product);
    }

    private ProductResponse mapToResponse(Product product) {
        return new ProductResponse(
                product.getProductId(),
                product.getProductName(),
                product.getOriginalPrice(),
                product.getStock(),
                product.getBrand(),
                product.getSlug(),
                product.getProductDescription(),
                product.getDateCreated(),
                product.getRatings(),
                product.getDateAdd(),
                product.getCategory() != null ? product.getCategory().getCategoryId() : null,
                product.getCategory() != null ? product.getCategory().getCategoryName() : null,
                product.getVendor() != null ? product.getVendor().getId() : null,
                product.getVendor() != null ? product.getVendor().getName() : null
        );
    }
}
