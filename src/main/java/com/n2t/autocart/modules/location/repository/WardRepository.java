package com.n2t.autocart.modules.address.repository;

import com.n2t.autocart.modules.address.entity.Ward;
import org.springframework.data.jpa.repository.JpaRepository;

interface WardRepository extends JpaRepository<Ward, Integer> {
}
