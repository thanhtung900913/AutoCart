package com.n2t.autocart.modules.profile.repository;

import com.n2t.autocart.modules.profile.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
	Optional<Customer> findByUser_Email(String email);
}
