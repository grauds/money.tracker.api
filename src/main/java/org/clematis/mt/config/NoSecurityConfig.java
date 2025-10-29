package org.clematis.mt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfigurationSource;

/**
 * @author  Anton Troshin
 */
@Configuration
@EnableWebSecurity
@Conditional(LocalEnvironment.class)
public class NoSecurityConfig {

    public static final String ALL_REGEXP = "/**";

    private final CorsConfigurationSource corsConfigurationSource;

    public NoSecurityConfig(CorsConfigurationSource corsConfigurationSource) {
        this.corsConfigurationSource = corsConfigurationSource;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .cors().configurationSource(corsConfigurationSource)
            .and()
            .authorizeRequests()
            .antMatchers(ALL_REGEXP).permitAll();

        return http.build();

    }
}