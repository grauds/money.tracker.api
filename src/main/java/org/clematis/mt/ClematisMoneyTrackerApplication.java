package org.clematis.mt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.web.filter.ForwardedHeaderFilter;

/**
 * @author Anton Troshin
 */
@SpringBootApplication
@EnableWebSecurity
public class ClematisMoneyTrackerApplication {

    @Bean
    public GrantedAuthorityDefaults grantedAuthorityDefaults() {

        // Remove the default ROLE_ prefix that Spring Boot otherwise expects
        return new GrantedAuthorityDefaults("");
    }

    public static void main(String[] args) {
        SpringApplication.run(ClematisMoneyTrackerApplication.class, args);
    }

    @Bean
    public ForwardedHeaderFilter forwardedHeaderFilter() {
        return new ForwardedHeaderFilter();
    }

}
