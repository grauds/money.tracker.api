package org.clematis.mt.config;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class JwtDebugFilter extends OncePerRequestFilter {

    private static final Logger LOG = LoggerFactory.getLogger(JwtDebugFilter.class);

    @SuppressWarnings({"checkstyle:MagicNumber", "checkstyle:NestedIfDepth", "checkstyle:MultipleStringLiterals"})
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            try {
                // Parse the JWT to inspect it
                String[] parts = token.split("\\.");
                if (parts.length == 3) {
                    String payload = new String(Base64.getUrlDecoder().decode(parts[1]), StandardCharsets.UTF_8);
                    LOG.info("JWT payload: {}", payload);

                    // Extract audience claim
                    ObjectMapper mapper = new ObjectMapper();
                    Map<String, Object> claims = mapper.readValue(payload, new TypeReference<>() {});
                    LOG.info("JWT audience: {}", claims.get("aud"));
                    // Type of aud claim
                    if (claims.get("aud") != null) {
                        LOG.info("Audience type: {}",
                            claims.get("aud").getClass().getName()
                        );
                    }
                }
            } catch (Exception e) {
                LOG.error("Error parsing JWT token", e);
            }
        }

        filterChain.doFilter(request, response);
    }
}
