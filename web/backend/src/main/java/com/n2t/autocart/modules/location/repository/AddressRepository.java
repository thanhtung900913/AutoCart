package com.n2t.autocart.modules.location.repository;

import com.n2t.autocart.modules.location.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, Integer> {
}
