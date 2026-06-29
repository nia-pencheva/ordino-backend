package com.ordino.domain.auth.service;

import com.ordino.core.exception.auth.InvalidRefreshTokenException;
import com.ordino.core.exception.auth.RefreshTokenExpiredException;
import com.ordino.domain.auth.model.entity.RefreshToken;
import com.ordino.domain.auth.repository.RefreshTokenRepository;
import com.ordino.domain.users.model.entity.User;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    @Value("${security.refresh-token.expiration-time}")
    private long expirationTime;

    private final RefreshTokenRepository repository;

    @Transactional
    public RefreshToken createRefreshToken(User user) {
        repository.deleteByUser(user);

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setUser(user);
        refreshToken.setExpiresAt(Instant.now().plusMillis(expirationTime));

        return repository.save(refreshToken);
    }

    @Transactional
    public User rotateToken(String rawToken) throws InvalidRefreshTokenException, RefreshTokenExpiredException {
        RefreshToken refreshToken = repository.findByToken(rawToken)
                .orElseThrow(() -> new InvalidRefreshTokenException());

        if (refreshToken.getExpiresAt().isBefore(Instant.now())) {
            repository.deleteByToken(rawToken);
            throw new RefreshTokenExpiredException();
        }

        User user = refreshToken.getUser();
        repository.deleteByToken(rawToken);
        return user;
    }

    @Transactional
    public void deleteByToken(String rawToken) {
        repository.deleteByToken(rawToken);
    }
}
