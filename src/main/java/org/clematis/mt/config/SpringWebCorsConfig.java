package org.clematis.mt.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


/**
 * Provides CORS configuration source for Spring Web MVC.
 */
@Configuration
public class SpringWebCorsConfig {

    public static final String ALL_REGEXP = "/**";
    public static final String ORIGINS = "*";

    /**
     * Returns the CORS configuration source bean.
     * @return CORS configuration source
     */
    @Bean(name = "corsConfigurationSource")
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(ORIGINS));
        configuration.setAllowedHeaders(List.of(ORIGINS));
        configuration.setAllowedMethods(List.of(HttpMethod.GET.name(),
                HttpMethod.POST.name(),
                HttpMethod.PUT.name(),
                HttpMethod.PATCH.name(),
                HttpMethod.DELETE.name(),
                HttpMethod.OPTIONS.name(),
                HttpMethod.HEAD.name()));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration(ALL_REGEXP, configuration);
        return source;
    }
}
