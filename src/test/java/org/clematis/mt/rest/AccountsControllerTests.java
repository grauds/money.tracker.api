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
 * REST API tests for AccountsController endpoints.
 */
@SuppressWarnings("checkstyle:MagicNumber")
public class AccountsControllerTests extends HateoasApiTests {

    /**
     * Verifies retrieving the monthly summary for RUB in the full date range.
     */
    @SuppressWarnings("checkstyle:MultipleStringLiterals")
    @Test
    public void testGetMonthlySummaryRUB() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer "
            + mock.getAccessToken(aTokenConfig().build()));

        ResponseEntity<?> response = getRestTemplateWithHalMessageConverter()
            .exchange(
                "/api/monthly/summary?code=RUB&moisStart=1&anStart=2017&moisEnd=12&anEnd=2018",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new org.springframework.core.ParameterizedTypeReference<>() {}
            );

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    /**
     * Verifies retrieving a monthly summary for USD.
     */
    @Test
    public void testGetMonthlySummaryUSD() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer "
            + mock.getAccessToken(aTokenConfig().build()));

        ResponseEntity<?> response = getRestTemplateWithHalMessageConverter()
            .exchange(
                "/api/monthly/summary?code=USD&moisStart=1&anStart=2017&moisEnd=12&anEnd=2018",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new org.springframework.core.ParameterizedTypeReference<>() {}
            );

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    /**
     * Verifies retrieving a monthly summary for a single month.
     */
    @Test
    public void testGetMonthlySummarySingleMonth() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer "
            + mock.getAccessToken(aTokenConfig().build()));

        ResponseEntity<?> response = getRestTemplateWithHalMessageConverter()
            .exchange(
                "/api/monthly/summary?code=RUB&moisStart=2&anStart=2018&moisEnd=2&anEnd=2018",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new org.springframework.core.ParameterizedTypeReference<>() {}
            );

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    /**
     * Verifies retrieving monthly summary for EUR currency.
     */
    @Test
    public void testGetMonthlySummaryEUR() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer "
            + mock.getAccessToken(aTokenConfig().build()));

        ResponseEntity<?> response = getRestTemplateWithHalMessageConverter()
            .exchange(
                "/api/monthly/summary?code=EUR&moisStart=1&anStart=2017&moisEnd=12&anEnd=2018",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new org.springframework.core.ParameterizedTypeReference<>() {}
            );

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    /**
     * Verifies retrieving a monthly summary for non-existent currency returns the empty list.
     */
    @SuppressWarnings("checkstyle:MultipleStringLiterals")
    @Test
    public void testGetMonthlySummaryNonExistentCurrency() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer "
            + mock.getAccessToken(aTokenConfig().build()));

        ResponseEntity<?> response = getRestTemplateWithHalMessageConverter()
            .exchange(
                "/api/monthly/summary?code=XXX&moisStart=1&anStart=2017&moisEnd=12&anEnd=2018",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new org.springframework.core.ParameterizedTypeReference<>() {}
            );

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    /**
     * Verifies retrieving a monthly summary with the reversed date range.
     */
    @Test
    public void testGetMonthlySummaryReversedDateRange() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer "
            + mock.getAccessToken(aTokenConfig().build()));

        ResponseEntity<?> response = getRestTemplateWithHalMessageConverter()
            .exchange(
                "/api/monthly/summary?code=RUB&moisStart=12&anStart=2018&moisEnd=1&anEnd=2017",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new org.springframework.core.ParameterizedTypeReference<>() {}
            );

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

}
