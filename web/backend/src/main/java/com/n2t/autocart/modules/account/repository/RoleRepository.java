package com.n2t.autocart.modules.account.repository;

import com.n2t.autocart.modules.account.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    List<Role> findDistinctByUserRoles_User_Email(String email);
}
