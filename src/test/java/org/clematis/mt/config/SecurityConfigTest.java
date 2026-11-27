package org.clematis.mt.config;

import java.lang.reflect.Field;
import java.time.Instant;
import java.util.Collections;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;

/**
 * Unit tests for SecurityConfig audienceValidator covering branch conditions.
 */
@SuppressWarnings({"checkstyle:MagicNumber", "checkstyle:MultipleStringLiterals"})
public class SecurityConfigTest {

    private static final String CLIENT_ID = "test-client-id";

    private static final Instant NOW = Instant.now();

    /**
     * Verifies audienceValidator accepts tokens with azp = "clematis-money-tracker-ui".
     * Covers the first if branch: "clematis-money-tracker-ui".equals(azp).
     */
    @Test
    public void testAudienceValidatorAcceptsUiClient() {
        SecurityConfig config = createConfig();

        Jwt jwt = createJwtWithAzp("clematis-money-tracker-ui");
        OAuth2TokenValidatorResult result = config.audienceValidator().validate(jwt);

        Assertions.assertTrue(result.getErrors().isEmpty(),
            "Should accept azp = clematis-money-tracker-ui");
    }

    /**
     * Verifies audienceValidator accepts tokens with azp matching clientId.
     * Covers the second if branch: clientId.equals(azp).
     */
    @Test
    public void testAudienceValidatorAcceptsMatchingClientId() {
        SecurityConfig config = createConfig();

        Jwt jwt = createJwtWithAzp(CLIENT_ID);
        OAuth2TokenValidatorResult result = config.audienceValidator().validate(jwt);

        Assertions.assertTrue(result.getErrors().isEmpty(),
            "Should accept azp matching clientId");
    }

    /**
     * Verifies audienceValidator rejects tokens with non-matching azp.
     * Covers the else branch: azp matches neither UI nor clientId.
     */
    @Test
    public void testAudienceValidatorRejectsNonMatchingAzp() {
        SecurityConfig config = createConfig();

        Jwt jwt = createJwtWithAzp("unknown-client");
        OAuth2TokenValidatorResult result = config.audienceValidator().validate(jwt);

        Assertions.assertFalse(result.getErrors().isEmpty(),
            "Should reject azp that matches neither UI nor clientId");

        Assertions.assertEquals("invalid_token",
            result.getErrors().iterator().next().getErrorCode());
    }

    /**
     * Verifies audienceValidator rejects tokens with null azp.
     * Covers the else branch: azp is null.
     */
    @Test
    public void testAudienceValidatorRejectsNullAzp() {
        SecurityConfig config = createConfig();

        Jwt jwt = createJwtWithNullAzp();
        OAuth2TokenValidatorResult result = config.audienceValidator().validate(jwt);

        Assertions.assertFalse(result.getErrors().isEmpty(),
            "Should reject null azp");
    }

    private SecurityConfig createConfig() {
        SecurityConfig config = new SecurityConfig(null, null) {
        };
        try {
            Field clientIdField = SecurityConfig.class
                .getDeclaredField("clientId");
            clientIdField.setAccessible(true);
            clientIdField.set(config, CLIENT_ID);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            Assertions.fail("Should be able to access clientId field", e);
        }
        return config;
    }

    private Jwt createJwtWithAzp(String azp) {
        return new Jwt(
            "test-token",
            NOW.minusSeconds(10),
            NOW.plusSeconds(3600),
            Collections.singletonMap("alg", "RS256"),
            Collections.singletonMap("azp", azp)
        );
    }

    private Jwt createJwtWithNullAzp() {
        return new Jwt(
            "test-token",
            NOW.minusSeconds(10),
            NOW.plusSeconds(3600),
            Collections.singletonMap("alg", "RS256"),
            Collections.singletonMap("sub", "test-user")
        );
    }
}
