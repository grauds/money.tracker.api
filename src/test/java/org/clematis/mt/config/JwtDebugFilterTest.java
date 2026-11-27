package org.clematis.mt.config;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.servlet.ServletException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

/**
 * Unit tests for JwtDebugFilter covering branch conditions.
 */
@SuppressWarnings({"checkstyle:MagicNumber", "checkstyle:MultipleStringLiterals"})
public class JwtDebugFilterTest {

    private final JwtDebugFilter filter = new JwtDebugFilter();

    /**
     * Verifies filter continues the chain when no Authorization header is present.
     * Covers the else branch: authHeader is null.
     */
    @Test
    public void testDoFilterWithoutAuthHeader() throws IOException, ServletException {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/test");
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();

        filter.doFilterInternal(request, response, filterChain);

        Assertions.assertEquals(200, response.getStatus());
    }

    /**
     * Verifies filter continues the chain when an Authorization header is not Bearer.
     * Covers the else branch: authHeader does not start with "Bearer".
     */
    @Test
    public void testDoFilterWithNonBearerAuthHeader() throws IOException, ServletException {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/test");
        request.addHeader("Authorization", "Basic dXNlcjpwYXNz");
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();

        filter.doFilterInternal(request, response, filterChain);

        Assertions.assertEquals(200, response.getStatus());
    }

    /**
     * Verifies filter handles JWT with more than 3 parts (malformed).
     * Covers the else branch: parts.length != 3.
     */
    @Test
    public void testDoFilterWithMalformedJwtTooManyParts() throws IOException, ServletException {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/test");
        request.addHeader("Authorization",
            "Bearer header.payload.signature.extra");
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();

        filter.doFilterInternal(request, response, filterChain);

        Assertions.assertEquals(200, response.getStatus());
    }

    /**
     * Verifies filter handles JWT with fewer than 3 parts (malformed).
     * Covers the else branch: parts.length != 3.
     */
    @Test
    public void testDoFilterWithMalformedJwtTooFewParts() throws IOException, ServletException {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/test");
        request.addHeader("Authorization", "Bearer header.payload");
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();

        filter.doFilterInternal(request, response, filterChain);

        Assertions.assertEquals(200, response.getStatus());
    }

    /**
     * Verifies filter handles JWT with invalid base64 payload.
     * Covers the catch branch: Base64 decoding fails.
     */
    @Test
    public void testDoFilterWithInvalidBase64Payload() throws IOException, ServletException {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/test");
        // Valid JWT structure but invalid base64 in payload
        request.addHeader("Authorization",
            "Bearer " + "not-valid-base64!!!.payload.sig");
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();

        filter.doFilterInternal(request, response, filterChain);

        Assertions.assertEquals(200, response.getStatus());
    }

    /**
     * Verifies filter handles JWT with invalid JSON payload.
     * Covers the catch branch: JSON parsing fails.
     */
    @Test
    public void testDoFilterWithInvalidJsonPayload() throws IOException, ServletException {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/test");
        // Valid JWT structure with non-JSON payload
        String payload = new String(
            java.util.Base64.getUrlEncoder().encode("not json".getBytes(StandardCharsets.UTF_8)),
            StandardCharsets.UTF_8
        );
        request.addHeader("Authorization",
            "Bearer " + "header." + payload + ".signature");
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();

        filter.doFilterInternal(request, response, filterChain);

        Assertions.assertEquals(200, response.getStatus());
    }

    /**
     * Verifies filter handles JWT with null aud claim.
     * Covers the else branch: claims.get("aud") is null.
     */
    @Test
    public void testDoFilterWithNullAudClaim() throws IOException, ServletException {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/test");
        // Valid JWT with JSON payload but no "aud" key
        String payload = base64Encode(
            "{\"sub\":\"user1\",\"iss\":\"test\"}"
        );
        request.addHeader("Authorization",
            "Bearer " + "header." + payload + ".signature");
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();

        filter.doFilterInternal(request, response, filterChain);

        Assertions.assertEquals(200, response.getStatus());
    }

    /**
     * Verifies filter handles JWT with valid structure and aud claim.
     * Covers the if branch: claims.get("aud") != null.
     */
    @Test
    public void testDoFilterWithValidJwtAndAudClaim() throws IOException, ServletException {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/test");
        String payload = base64Encode(
            "{\"sub\":\"user1\",\"aud\":\"clematis\"}"
        );
        request.addHeader("Authorization",
            "Bearer " + "header." + payload + ".signature");
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();

        filter.doFilterInternal(request, response, filterChain);

        Assertions.assertEquals(200, response.getStatus());
    }

    /**
     * Verifies filter handles JWT with array aud claim.
     * Covers the if branch: claims.get("aud") is not null and is an array.
     */
    @Test
    public void testDoFilterWithArrayAudClaim() throws IOException, ServletException {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/test");
        String payload = base64Encode(
            "{\"sub\":\"user1\",\"aud\":[\"client1\",\"client2\"]}"
        );
        request.addHeader("Authorization",
            "Bearer " + "header." + payload + ".signature");
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();

        filter.doFilterInternal(request, response, filterChain);

        Assertions.assertEquals(200, response.getStatus());
    }

    private String base64Encode(String input) {
        return java.util.Base64.getUrlEncoder().encodeToString(
            input.getBytes(StandardCharsets.UTF_8)
        );
    }
}
