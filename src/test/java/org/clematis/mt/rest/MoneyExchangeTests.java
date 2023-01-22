package org.clematis.mt.rest;

import java.util.HashMap;
import java.util.Map;

import org.clematis.mt.model.MoneyExchange;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static com.tngtech.keycloakmock.api.TokenConfig.aTokenConfig;

public class MoneyExchangeTests extends HateoasApiTests {

    @Test
    public void testAverageExhangeRate() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION,
                "Bearer " + mock.getAccessToken(aTokenConfig().build()));

        Map<String, String> uriParam = new HashMap<>();
        uriParam.put("source", "USD");
        uriParam.put("dest", "RUB");

        ResponseEntity<Long> exchange = getRestTemplateWithHalMessageConverter()
                .exchange("/api/exchange/search/average?source={source}&dest={dest}",
                        HttpMethod.GET,
                        new HttpEntity<>(headers),
                        new ParameterizedTypeReference<>() {}, uriParam);
        Assertions.assertEquals(HttpStatus.OK, exchange.getStatusCode());
    }

    @Test
    public void testAllFields() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION,
                "Bearer " + mock.getAccessToken(aTokenConfig().build()));
        ResponseEntity<PagedModel<MoneyExchange>> exchange = getRestTemplateWithHalMessageConverter()
                .exchange("/api/exchange",
                        HttpMethod.GET,
                        new HttpEntity<>(headers),
                        new ParameterizedTypeReference<>() {});
        Assertions.assertEquals(HttpStatus.OK, exchange.getStatusCode());

    }

    @Test
    public void testExchangeReport() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION,
                "Bearer " + mock.getAccessToken(aTokenConfig().build()));

        Map<String, String> uriParam = new HashMap<>();
        uriParam.put("source", "USD");
        uriParam.put("dest", "RUB");

        // do not put DTO interface here, it will fail to be constructed
        ResponseEntity<?> exchange = getRestTemplateWithHalMessageConverter()
                .exchange("/api/exchange/search/report",
                        HttpMethod.GET,
                        new HttpEntity<>(headers),
                        new ParameterizedTypeReference<>() {}, uriParam);
        Assertions.assertEquals(HttpStatus.OK, exchange.getStatusCode());
    }
}
