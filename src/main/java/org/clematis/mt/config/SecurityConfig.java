package org.clematis.mt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfigurationSource;

/**
 * @author  Anton Troshin
 */
@Configuration
@EnableWebSecurity
@Conditional(NotLocalEnvironment.class)
public class SecurityConfig {

    public static final String ALL_REGEXP = "/**";

    private static final String[] SWAGGER_WHITELIST = {
        "/v3/api-docs/**",
        "/swagger-ui/**",
        "/swagger-ui.html",
    };

    private final CorsConfigurationSource corsConfigurationSource;

    public SecurityConfig(CorsConfigurationSource corsConfigurationSource) {
        this.corsConfigurationSource = corsConfigurationSource;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .cors().configurationSource(corsConfigurationSource)
            .and()
            .authorizeRequests()
            .antMatchers(HttpMethod.OPTIONS, ALL_REGEXP).permitAll()
            .antMatchers(SWAGGER_WHITELIST).permitAll()
            .antMatchers("/api" + ALL_REGEXP).authenticated()
            .antMatchers(ALL_REGEXP).permitAll()
            .and()
            .oauth2ResourceServer()
            .jwt()
            .jwtAuthenticationConverter(jwtAuthenticationConverter());

        return http.build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        // If you need role conversion logic, add it here
        return new JwtAuthenticationConverter();
    }

}
