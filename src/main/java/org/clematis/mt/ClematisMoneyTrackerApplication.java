package org.clematis.mt;

import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.core.GrantedAuthorityDefaults;

/**
 * @author Anton Troshin
 */
@SpringBootApplication
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class ClematisMoneyTrackerApplication {

    @Bean
    public GrantedAuthorityDefaults grantedAuthorityDefaults() {
        // Remove the default ROLE_ prefix that spring boot otherwise expects
        return new GrantedAuthorityDefaults("");
    }

    @Bean
    public KeycloakSpringBootConfigResolver keycloakConfigResolver() {
        return new KeycloakSpringBootConfigResolver();
    }

    public static void main(String[] args) {
        SpringApplication.run(ClematisMoneyTrackerApplication.class, args);
    }


}
