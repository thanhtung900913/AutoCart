package com.n2t.autocart.modules.account.service;

import com.n2t.autocart.modules.account.entity.RefreshToken;
import com.n2t.autocart.modules.account.entity.User;
import com.n2t.autocart.modules.account.repository.RefreshTokenRepository;
import com.n2t.autocart.modules.account.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
public class RefreshTokenService {
    private final RefreshTokenRepository repository;
    private final UserRepository userRepository;

    public RefreshTokenService(RefreshTokenRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    public void createRefreshToken(String token, User user){
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(token);
        refreshToken.setUser(user);
        refreshToken.setExpiresAt(Instant.now().plus(100, ChronoUnit.DAYS));
        refreshToken.setRevoked(false);
        repository.save(refreshToken);
    }
    public RefreshToken updateRefreshToken(String newToken, String oldToken){
        Optional<RefreshToken> refreshToken = repository.findByToken(oldToken);
        if(refreshToken.isPresent()){
            RefreshToken ref = refreshToken.get();
            ref.setToken(newToken);
            return repository.save(ref);
        }
        return null;
    }
}
