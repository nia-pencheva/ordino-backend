package com.ordino.domain.users.service;

import java.security.SecureRandom;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PasswordServiceImpl implements PasswordService {
    @Value("${initial-password.length}")
    private int length;

    @Value("${initial-password.pool}")
    private String pool;

    public String generate() {
        SecureRandom random = new SecureRandom();
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int idx = random.nextInt(pool.length());
            builder.append(pool.charAt(idx));
        }
        return builder.toString();
    }
}
