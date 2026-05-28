package com.ordino.core.config.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import com.ordino.core.config.JWT.JWTAuthenticationEntryPoint;
import com.ordino.core.config.JWT.JWTFilter;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    @Value("${frontend.url}")
    private String frontendURL;

    private final JWTFilter jwtFilter;
    private final JWTAuthenticationEntryPoint authenticationEntryPoint;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable) // should enable
                    .logout(AbstractHttpConfigurer::disable)
                    .cors(cors -> cors
                        .configurationSource(this::getCorsConfiguration)
                    )
                    .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/refresh", "/logout").permitAll()
                        .anyRequest().authenticated()
                    )
                    .sessionManagement(session -> 
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    )
                    .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                    .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(authenticationEntryPoint)
                    )
                    .build();
    }

    private CorsConfiguration getCorsConfiguration(HttpServletRequest exchange) {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin(frontendURL);
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(true);

        return configuration;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) {
        return config.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(DatabaseUserDetailsService userDetailsService) {
        DaoAuthenticationProvider autoProvider = new DaoAuthenticationProvider(userDetailsService);
        autoProvider.setPasswordEncoder(passwordEncoder());
        return autoProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }
}
