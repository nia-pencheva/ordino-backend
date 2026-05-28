package com.ordino.domain.auth.model.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

import com.ordino.domain.users.model.entity.User;

@Entity
@Table(name = "refresh_tokens")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(columnDefinition = "INT UNSIGNED")
    private Long id;

    @Column(nullable = false, unique = true, columnDefinition = "CHAR(36)")
    private String token;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "expires_at", nullable = false, columnDefinition = "TIMESTAMP")
    private Instant expiresAt;
}
