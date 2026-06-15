package com.ordino.core.config.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ordino.core.config.JWT.JWTAuthenticationEntryPoint;
import com.ordino.core.config.JWT.JWTFilter;
import com.ordino.core.config.JWT.JWTService;
import com.ordino.core.config.JWT.JsonErrorWriter;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    @Value("${frontend.url}")
    private String frontendURL;

    private final JWTAuthenticationEntryPoint authenticationEntryPoint;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JWTFilter jwtFilter, PasswordChangedFilter passwordChangedFilter) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable) // should enable
                    .logout(AbstractHttpConfigurer::disable)
                    .cors(cors -> cors
                        .configurationSource(this::getCorsConfiguration)
                    )
                    .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/refresh", "/logout", "/error").permitAll()
                        .requestMatchers(HttpMethod.GET, "/users", "/users/roles").permitAll()
                        .requestMatchers("/users", "/users/**").hasAuthority("admin")
                        .requestMatchers(
                            "/products", "/products/**",
                            "/unit-categories", "/unit-categories/**",
                            "/units", "/units/**",
                            "/recipe-ingredient-categories", "/recipe-ingredient-categories/**"
                        ).hasAnyAuthority("chef", "warehouse manager")
                        .requestMatchers(HttpMethod.GET, "/recipe-categories").hasAnyAuthority("kitchen staff", "line cook", "chef", "manager")
                        .requestMatchers("/recipe-categories", "/recipe-categories/**").hasAuthority("chef")
                        .requestMatchers("/warehouse-product-categories", "/warehouse-product-categories/**").hasAuthority("warehouse manager")
                        .requestMatchers("/recipes/log", "/recipes/log/**").hasAnyAuthority("kitchen staff", "line cook", "chef", "manager")
                        .requestMatchers("/recipes", "/recipes/**").hasAnyAuthority("kitchen staff", "line cook", "chef", "manager")
                        .anyRequest().authenticated()
                    )
                    .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    )
                    .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                    .addFilterAfter(passwordChangedFilter, UsernamePasswordAuthenticationFilter.class)
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

    @Bean
    public JWTFilter jwtFilter(JWTService jwtService, UserDetailsService userDetailsService, JsonErrorWriter jsonErrorWriter) {
        return new JWTFilter(jwtService, userDetailsService, jsonErrorWriter);
    }

    @Bean
    public PasswordChangedFilter passwordChangedFilter(ObjectMapper objectMapper) {
        return new PasswordChangedFilter(objectMapper);
    }
}
