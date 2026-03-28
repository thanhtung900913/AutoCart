package com.n2t.autocart.modules.cart.repository;

import com.n2t.autocart.modules.cart.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    boolean existsBySlug(String slug);
}
