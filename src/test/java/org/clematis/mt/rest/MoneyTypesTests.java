package org.clematis.mt.rest;

import static com.tngtech.keycloakmock.api.TokenConfig.aTokenConfig;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.clematis.mt.model.MoneyType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class MoneyTypesTests extends HateoasApiTests {

    @Test
    public void testMoneyTypes() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION,
            "Bearer " + mock.getAccessToken(aTokenConfig().build()));

        ResponseEntity<PagedModel<MoneyType>> exchange = getRestTemplateWithHalMessageConverter()
            .exchange("/api/moneyTypes",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<>() {});

        Assertions.assertEquals(HttpStatus.OK, exchange.getStatusCode());
        Assertions.assertEquals(5, Objects.requireNonNull(exchange.getBody()).getContent().size());
    }

    @Test
    public void testFindByCode() {

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION,
            "Bearer " + mock.getAccessToken(aTokenConfig().build()));

        Map<String, String> uriParam = new HashMap<>();
        uriParam.put("code", "USD");

        ResponseEntity<MoneyType> exchange = getRestTemplateWithHalMessageConverter()
            .exchange("/api/moneyTypes/search/findByCode?code={code}",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<>() {}, uriParam);

        Assertions.assertEquals(HttpStatus.OK, exchange.getStatusCode());
        Assertions.assertEquals("Доллар США", Objects.requireNonNull(exchange.getBody()).getName());
    }
}
