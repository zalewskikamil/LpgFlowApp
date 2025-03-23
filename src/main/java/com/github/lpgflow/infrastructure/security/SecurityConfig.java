package com.github.lpgflow.infrastructure.security;

import com.github.lpgflow.domain.user.UserFacade;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
class SecurityConfig {

    @Bean
    UserDetailsService userDetailsService(UserFacade userFacade) {
        return new UserDetailsServiceImpl(userFacade);
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthTokenFilter jwtAuthTokenFilter) throws Exception {
        http.csrf(c -> c.disable());
        http.cors(c -> c.disable());
        http.formLogin(c -> c.disable());
        http.httpBasic(c -> c.disable());
        http.sessionManagement(c ->
                c.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.addFilterBefore(jwtAuthTokenFilter, UsernamePasswordAuthenticationFilter.class);
        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/swagger-ui/**").permitAll()
                .requestMatchers("/swagger-resources/**").permitAll()
                .requestMatchers("/v3/api-docs/**").permitAll()
                .requestMatchers(HttpMethod.POST,"/token/**").permitAll()
                .requestMatchers(HttpMethod.GET,"/users/email/**").authenticated()
                .requestMatchers(HttpMethod.GET,"/users/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST,"/users").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT,"/users/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PATCH,"/users/**").hasRole("ADMIN")
                .requestMatchers("/roles/**").hasRole("ADMIN")
                .anyRequest().authenticated());
        return http.build();
    }
}
