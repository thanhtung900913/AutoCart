package com.n2t.autocart.modules.product.repository;

import com.n2t.autocart.modules.product.entity.CartProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartProductRepository extends JpaRepository<CartProduct, Integer> {
    List<CartProduct> findAllByCart_CartId(Integer cartId);

    Optional<CartProduct> findByCart_CartIdAndProduct_ProductId(Integer cartId, Integer productId);

    Optional<CartProduct> findByCart_CartIdAndCartProductId(Integer cartId, Integer cartProductId);
}
