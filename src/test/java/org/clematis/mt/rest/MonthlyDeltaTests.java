package org.clematis.mt.rest;

import java.util.HashMap;
import java.util.Map;

import org.clematis.mt.model.views.MonthlyDelta;
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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MonthlyDeltaTests extends HateoasApiTests {


    @Test
    public void testAccountsTotals() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION,
                "Bearer " + mock.getAccessToken(aTokenConfig().build()));

        ResponseEntity<PagedModel<MonthlyDelta>> monthlyDeltas = getRestTemplateWithHalMessageConverter()
                .exchange("/api/monthlyDeltas", HttpMethod.GET,  new HttpEntity<>(headers),
                        new ParameterizedTypeReference<>() {});
        Assertions.assertEquals(HttpStatus.OK, monthlyDeltas.getStatusCode());
    }

    @Test
    public void testBalances() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION,
                "Bearer " + mock.getAccessToken(aTokenConfig().build()));

        Map<String, String> uriParam = new HashMap<>();
        uriParam.put("an", "2018");
        uriParam.put("mois", "2");
        uriParam.put("code", "RUB");

        ResponseEntity<Long> balance = getRestTemplateWithHalMessageConverter()
                .exchange("/api/monthlyDeltas/search/balance?an={an}&mois={mois}&code={code}",
                        HttpMethod.GET,  new HttpEntity<>(headers),
                        new ParameterizedTypeReference<>() {}, uriParam);
        Assertions.assertEquals(HttpStatus.OK, balance.getStatusCode());
    }

    @Test
    public void testSorting() {

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION,
                "Bearer " + mock.getAccessToken(aTokenConfig().build()));

        Map<String, String> uriParam = new HashMap<>();
        uriParam.put("page", "1");
        uriParam.put("size", "12");
        uriParam.put("sort", "key.an,DESC");

        ResponseEntity<PagedModel<MonthlyDelta>> deltas = getRestTemplateWithHalMessageConverter()
                .exchange("/api/monthlyDeltas?page={page}&size={size}&sort={sort}",
                        HttpMethod.GET,
                        new HttpEntity<>(headers),
                        new ParameterizedTypeReference<>() {},
                        uriParam);
        Assertions.assertEquals(HttpStatus.OK, deltas.getStatusCode());
    }
}
