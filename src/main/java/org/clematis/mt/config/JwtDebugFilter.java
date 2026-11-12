package org.clematis.mt.config;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * Servlet filter that decodes and logs JWT token payload for debugging purposes.
 * Extracts and logs the JWT payload and audience claim from Bearer tokens in requests.
 */
@Component
public class JwtDebugFilter extends OncePerRequestFilter {

    private static final Logger LOG = LoggerFactory.getLogger(JwtDebugFilter.class);

    /**
     * Decodes the JWT token from the Authorization header and logs the payload and audience claim.
     * If the token is invalid or cannot be parsed, the error is logged and processing continues.
     *
     * @param request the HTTP request
     * @param response the HTTP response
     * @param filterChain the filter chain to continue processing
     * @throws ServletException if a servlet error occurs
     * @throws IOException if an I/O error occurs
     */
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
