package com.ordino.core.config.firebase;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "firebase")
public record FirebaseProperties(
    String projectId,
    String privateKeyId,
    String privateKey,
    String clientEmail,
    String clientId
) {}
