package com.ordino.domain.auth.service;

import com.ordino.core.exception.auth.InvalidRefreshTokenException;
import com.ordino.core.exception.auth.RefreshTokenExpiredException;
import com.ordino.domain.auth.model.entity.RefreshToken;
import com.ordino.domain.users.model.entity.User;

public interface RefreshTokenService {
    RefreshToken createRefreshToken(User user);

    User rotateToken(String rawToken) throws InvalidRefreshTokenException, RefreshTokenExpiredException;

    void deleteByToken(String rawToken);
}
