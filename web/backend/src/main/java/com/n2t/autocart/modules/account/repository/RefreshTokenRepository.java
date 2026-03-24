package com.n2t.autocart.modules.account.repository;

import com.n2t.autocart.modules.account.entity.RefreshToken;
import com.n2t.autocart.modules.account.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {
    Optional<RefreshToken> findByToken(String token);
}
