package com.ordino.core.config.firebase;

import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
@AllArgsConstructor
@EnableConfigurationProperties(FirebaseProperties.class)
public class FirebaseConfiguration {
    private final FirebaseProperties firebaseProperties;

    @PostConstruct
    public void initialize() throws IOException {
        ServiceAccountCredentials credentials = ServiceAccountCredentials.fromPkcs8(
            firebaseProperties.clientId(),
            firebaseProperties.clientEmail(),
            firebaseProperties.privateKey().replace("\\n", "\n"),
            firebaseProperties.privateKeyId(),
            null
        );

        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(FirebaseOptions.builder()
                .setCredentials(credentials)
                .setProjectId(firebaseProperties.projectId())
                .build());
        }
    }
}
