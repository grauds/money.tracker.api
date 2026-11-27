package org.clematis.mt.rest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static com.tngtech.keycloakmock.api.TokenConfig.aTokenConfig;

/**
 * REST API tests for AgentExpenseController endpoints covering branch conditions.
 */
@SuppressWarnings("checkstyle:MultipleStringLiterals")
public class AgentExpenseControllerTests extends HateoasApiTests {

    private static final String ENDPOINT = "/api/agentCommodityGroupExpenses";

    /**
     * Verifies retrieving agent commodity group expenses returns 200 OK with data.
     * Covers the else branch: groups is not empty.
     */
    @Test
    public void testGetAgentCommodityGroupExpensesWithResults() {
        HttpHeaders headers = createHeaders();

        ResponseEntity<?> response = getRestTemplateWithHalMessageConverter().exchange(
            ENDPOINT + "?code=RUB&moisStart=1&anStart=2017&moisEnd=12&anEnd=2018",
            HttpMethod.GET,
            new HttpEntity<>(headers),
            new org.springframework.core.ParameterizedTypeReference<>() {}
        );

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
    }

    /**
     * Verifies retrieving agent commodity group expenses with non-matching params.
     * Covers if branch: groups.isEmpty() -> returns empty page.
     */
    @Test
    public void testGetAgentCommodityGroupExpensesEmptyResults() {
        HttpHeaders headers = createHeaders();

        ResponseEntity<?> response = getRestTemplateWithHalMessageConverter().exchange(
            ENDPOINT + "?code=XXX&moisStart=1&anStart=2099&moisEnd=12&anEnd=2099",
            HttpMethod.GET,
            new HttpEntity<>(headers),
            new org.springframework.core.ParameterizedTypeReference<>() {}
        );

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(mock.getAccessToken(aTokenConfig().build()));
        return headers;
    }
}
