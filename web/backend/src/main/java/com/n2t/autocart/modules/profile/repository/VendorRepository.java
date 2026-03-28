package com.n2t.autocart.modules.profile.repository;

import com.n2t.autocart.modules.profile.entity.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VendorRepository extends JpaRepository<Vendor, Integer> {
    Optional<Vendor> findByUser_Email(String email);
}
