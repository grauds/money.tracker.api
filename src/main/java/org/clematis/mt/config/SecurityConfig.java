package org.clematis.mt.config;

import org.keycloak.adapters.springsecurity.KeycloakConfiguration;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Conditional;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.authentication.session.NullAuthenticatedSessionStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.web.cors.CorsConfigurationSource;

/**
 * @author  Anton Troshin
 */
@KeycloakConfiguration
@Conditional(NotLocalEnvironment.class)
public class SecurityConfig extends KeycloakWebSecurityConfigurerAdapter {

    public static final String ALL_REGEXP = "/**";

    private static final String[] SWAGGER_WHITELIST = {
        "/v3/api-docs/**",
        "/swagger-ui/**",
        "/swagger-ui.html",
    };

    @Autowired
    private CorsConfigurationSource corsConfigurationSource;

    @Override
    protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
        return new NullAuthenticatedSessionStrategy();
    }

    @Autowired
    public void configureGlobal(final AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(keycloakAuthenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
        http.csrf().disable()
                .cors().configurationSource(corsConfigurationSource)
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS, ALL_REGEXP)
                .permitAll()
                .antMatchers(SWAGGER_WHITELIST)
                .permitAll()
                .antMatchers("/api" + ALL_REGEXP)
                .authenticated()
                .antMatchers(ALL_REGEXP)
                .permitAll();
    }
}
