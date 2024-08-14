package org.clematis.mt.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.session.NullAuthenticatedSessionStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;

/**
 * @author  Anton Troshin
 */
@Conditional(LocalEnvironment.class)
public class NoSecurityConfig {

    public static final String ALL = "*";

    @Bean
    protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
        return new NullAuthenticatedSessionStrategy();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           OidcClientInitiatedLogoutSuccessHandler oidcLogoutSuccessHandler)
        throws Exception {

        http.cors(cors -> cors.configurationSource(request -> {
            CorsConfiguration configuration = new CorsConfiguration();
            configuration.setAllowedOrigins(List.of(ALL));
            configuration.setAllowedMethods(List.of(ALL));
            configuration.setAllowedHeaders(List.of(ALL));
            return configuration;
        }));
        http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(requests -> requests
                .requestMatchers(
                    AntPathRequestMatcher.antMatcher(ALL)
                ).permitAll()
            );

        return http.build();
    }
}
