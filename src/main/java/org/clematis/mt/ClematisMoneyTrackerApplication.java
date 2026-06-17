package org.clematis.mt;

import org.clematis.mt.repository.NameFilteringRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.web.filter.ForwardedHeaderFilter;

/**
 * Entry point for the Clematis Money Tracker application.
 * <p>
 * This is a Spring Boot application configured with:
 * - Spring Security for security features, with global method security enabled.
 * - JPA repositories, using a custom repository base class for extended filtering functionality.
 * - Feign clients for seamless integration with remote services under the specified base package.
 * <p>
 * Configuration Details:
 * - Integration with a custom repository base class `NameFilteringRepository` allows
 *   automatic name-based filtering for JPA entities.
 * - Disabled default Spring Security ROLE_ prefix for granted authorities
 *   via the `GrantedAuthorityDefaults` bean.
 * - Forwarding of HTTP headers is enabled by providing the `ForwardedHeaderFilter` bean.
 * <p>
 * Annotations:
 * - `@SpringBootApplication`: Indicates a Spring Boot application and enables component scanning.
 * - `@EnableWebSecurity`: Enables Spring Security features.
 * - `@EnableJpaRepositories`: Enables JPA repository scanning and registers the custom base repository class.
 * - `@EnableGlobalMethodSecurity`: Activates global method security with support for secured and
 * pre- / post-annotations.
 * - `@EnableFeignClients`: Enables scanning for Feign clients to facilitate remote service calls.
 */
@SpringBootApplication
@EnableWebSecurity
@EnableJpaRepositories(repositoryBaseClass = NameFilteringRepository.class)
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
@EnableFeignClients(basePackages = "org.clematis.storage.client")
public class ClematisMoneyTrackerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClematisMoneyTrackerApplication.class, args);
    }

    @Bean
    public GrantedAuthorityDefaults grantedAuthorityDefaults() {
        // Remove the default ROLE_ prefix that Spring Boot otherwise expects
        return new GrantedAuthorityDefaults("");
    }

    @Bean
    public ForwardedHeaderFilter forwardedHeaderFilter() {
        return new ForwardedHeaderFilter();
    }

}
