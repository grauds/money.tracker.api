package org.clematis.mt.rest;

import java.util.HashMap;
import java.util.Map;

import org.clematis.mt.model.AccountTotal;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static com.tngtech.keycloakmock.api.TokenConfig.aTokenConfig;


/**
 * REST API tests for AccountTotals endpoints.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AccountsTotalsTests extends HateoasApiTests {

    /**
     * Verifies retrieving account totals via the REST API with authentication.
     */
    @Test
    public void testAccountsTotals() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + mock.getAccessToken(aTokenConfig().build()));

        ResponseEntity<PagedModel<AccountTotal>> accountsTotals = getRestTemplateWithHalMessageConverter()
                .exchange("/api/accountsTotals",
                    HttpMethod.GET,
                    new HttpEntity<>(headers),
                    new ParameterizedTypeReference<>() {}
                );
        Assertions.assertEquals(HttpStatus.OK, accountsTotals.getStatusCode());
    }

    /**
     * Verifies retrieving account totals filtered by currency code.
     */
    @Test
    public void testTotalsBalanceInCode() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + mock.getAccessToken(aTokenConfig().build()));

        Map<String, String> uriParam = new HashMap<>();
        uriParam.put("code", "RUB");

        ResponseEntity<PagedModel<AccountTotal>> accountsTotals = getRestTemplateWithHalMessageConverter()
                .exchange("/api/accountsTotals/search/code?code={code}",
                    HttpMethod.GET,
                    new HttpEntity<>(headers),
                    new ParameterizedTypeReference<>() {},
                    uriParam
                );
        Assertions.assertEquals(HttpStatus.OK, accountsTotals.getStatusCode());
    }

    /**
     * Verifies retrieving the total balance for a specific currency.
     */
    @Test
    public void testBalanceInCode() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + mock.getAccessToken(aTokenConfig().build()));

        Map<String, String> uriParam = new HashMap<>();
        uriParam.put("code", "RUB");

        ResponseEntity<Long> total = getRestTemplateWithHalMessageConverter()
                .exchange("/api/accountsTotals/search/balance?code={code}",
                    HttpMethod.GET,
                    new HttpEntity<>(headers),
                    new ParameterizedTypeReference<>() {},
                    uriParam
                );
        Assertions.assertEquals(HttpStatus.OK, total.getStatusCode());
    }

    /**
     * Verifies retrieving the historical balance for a specific currency and number of days.
     */
    @Test
    public void testBalanceHistoryInCode() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + mock.getAccessToken(aTokenConfig().build()));

        Map<String, String> uriParam = new HashMap<>();
        uriParam.put("code", "RUB");
        uriParam.put("days", "14");

        ResponseEntity<Long> total = getRestTemplateWithHalMessageConverter()
                .exchange("/api/accountsTotals/search/balanceHistory?code={code}&days={days}",
                    HttpMethod.GET,
                    new HttpEntity<>(headers),
                    new ParameterizedTypeReference<>() {},
                    uriParam
                );
        Assertions.assertEquals(HttpStatus.OK, total.getStatusCode());
    }
}
