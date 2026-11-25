package org.clematis.mt.rest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static com.tngtech.keycloakmock.api.TokenConfig.aTokenConfig;


/**
 * REST API tests for IncomeExpenseController endpoints.
 */
@SuppressWarnings({"checkstyle:MagicNumber", "checkstyle:MultipleStringLiterals"})
public class IncomeExpenseControllerTests extends HateoasApiTests {

    /**
     * Verifies retrieving total income in RUB.
     */
    @Test
    public void testGetIncomeTotalRUB() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer "
            + mock.getAccessToken(aTokenConfig().build()));

        ResponseEntity<?> response = getRestTemplateWithHalMessageConverter()
            .exchange("/api/income/search/total?code=RUB", HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<>() {});

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    /**
     * Verifies retrieving total income in USD.
     */
    @Test
    public void testGetIncomeTotalUSD() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer "
            + mock.getAccessToken(aTokenConfig().build()));

        ResponseEntity<?> response = getRestTemplateWithHalMessageConverter()
            .exchange("/api/income/search/total?code=USD", HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<>() {});

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    /**
     * Verifies retrieving total income breakdown by currency in RUB.
     */
    @Test
    public void testGetIncomeByCurrencyRUB() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer "
            + mock.getAccessToken(aTokenConfig().build()));

        ResponseEntity<?> response = getRestTemplateWithHalMessageConverter()
            .exchange("/api/income/search/totalByCurrency?code=RUB", HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<>() {});

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    /**
     * Verifies retrieving total income breakdown by categories in RUB.
     */
    @Test
    public void testGetIncomeByCategoriesRUB() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer "
            + mock.getAccessToken(aTokenConfig().build()));

        ResponseEntity<?> response = getRestTemplateWithHalMessageConverter()
            .exchange("/api/income/search/totalByCategories?code=RUB", HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<>() {});

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    /**
     * Verifies retrieving total income breakdown by organizations in RUB.
     */
    @Test
    public void testGetIncomeByOrganizationsRUB() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer "
            + mock.getAccessToken(aTokenConfig().build()));

        ResponseEntity<?> response = getRestTemplateWithHalMessageConverter()
            .exchange("/api/income/search/totalByOrganizations?code=RUB", HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<>() {});

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    /**
     * Verifies retrieving total expense in RUB.
     */
    @Test
    public void testGetExpenseTotalRUB() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer "
            + mock.getAccessToken(aTokenConfig().build()));

        ResponseEntity<?> response = getRestTemplateWithHalMessageConverter()
            .exchange("/api/expenses/search/total?code=RUB", HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<>() {});

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    /**
     * Verifies retrieving total expense in USD.
     */
    @Test
    public void testGetExpenseTotalUSD() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer "
            + mock.getAccessToken(aTokenConfig().build()));

        ResponseEntity<?> response = getRestTemplateWithHalMessageConverter()
            .exchange("/api/expenses/search/total?code=USD", HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<>() {});

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    /**
     * Verifies retrieving total expense breakdown by currency in RUB.
     */
    @Test
    public void testGetExpenseByCurrencyRUB() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer "
            + mock.getAccessToken(aTokenConfig().build()));

        ResponseEntity<?> response = getRestTemplateWithHalMessageConverter()
            .exchange("/api/expenses/search/totalByCurrency?code=RUB", HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<>() {});

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    /**
     * Verifies retrieving total expense breakdown by categories in RUB.
     */
    @Test
    public void testGetExpenseByCategoriesRUB() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer "
            + mock.getAccessToken(aTokenConfig().build()));

        ResponseEntity<?> response = getRestTemplateWithHalMessageConverter()
            .exchange("/api/expenses/search/totalByCategories?code=RUB", HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<>() {});

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    /**
     * Verifies retrieving total expense breakdown by organizations in RUB.
     */
    @Test
    public void testGetExpenseByOrganizationsRUB() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer "
            + mock.getAccessToken(aTokenConfig().build()));

        ResponseEntity<?> response = getRestTemplateWithHalMessageConverter()
            .exchange("/api/expenses/search/totalByOrganizations?code=RUB", HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<>() {});

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

}
