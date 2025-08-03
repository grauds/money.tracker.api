package org.clematis.mt;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.stereotype.Component;

import lombok.extern.java.Log;
/**
 * @author Anton Troshin
 */
@SpringBootApplication
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class ClematisMoneyTrackerApplication {

    @Bean
    public GrantedAuthorityDefaults grantedAuthorityDefaults() {

        // Remove the default ROLE_ prefix that Spring Boot otherwise expects
        return new GrantedAuthorityDefaults("");
    }

    @Bean
    public KeycloakSpringBootConfigResolver keycloakConfigResolver() {
        return new KeycloakSpringBootConfigResolver();
    }

    public static void main(String[] args) {
        SpringApplication.run(ClematisMoneyTrackerApplication.class, args);
    }

    @Log
    @Component
    public class HeaderLoggerFilter implements Filter {
        public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
            HttpServletRequest req = (HttpServletRequest) request;
            log.info("X-Forwarded-Proto: " + req.getHeader("X-Forwarded-Proto"));
            log.info("X-Forwarded-Host: " + req.getHeader("X-Forwarded-Host"));
            chain.doFilter(request, response);
        }
    }


}
