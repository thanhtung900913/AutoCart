package com.n2t.autocart.modules.account.repository;

import com.n2t.autocart.modules.account.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {
}
