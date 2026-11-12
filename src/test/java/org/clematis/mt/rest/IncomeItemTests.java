package org.clematis.mt.rest;

import static com.tngtech.keycloakmock.api.TokenConfig.aTokenConfig;
import java.util.HashMap;
import java.util.Map;

import org.clematis.mt.model.IncomeItem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


/**
 * REST API tests for IncomeItem endpoints.
 */
public class IncomeItemTests extends HateoasApiTests {

    /**
     * Verifies retrieving income items via the REST API with authentication and pagination.
     */
    @Test
    public void testIncomeItems() {

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer "
            + mock.getAccessToken(aTokenConfig().build()));

        Map<String, String> uriParam = new HashMap<>();
        uriParam.put("page", "1");
        uriParam.put("size", "12");
        uriParam.put("sort", "name,DESC");

        ResponseEntity<PagedModel<IncomeItem>> items
            = getRestTemplateWithHalMessageConverter()
            .exchange("/api/incomeItems?page={page}&size={size}&sort={sort}", HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<>() {},
                uriParam);

        Assertions.assertEquals(HttpStatus.OK, items.getStatusCode());

    }

    /**
     * Verifies retrieving income items filtered by commodity ID.
     */
    @Test
    public void testCommodityIncomeItems() {

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer "
            + mock.getAccessToken(aTokenConfig().build()));

        ResponseEntity<PagedModel<IncomeItem>> items
            = getRestTemplateWithHalMessageConverter()
            .exchange("/api/incomeItems/search/commodity?id=654",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<>() {});

        Assertions.assertEquals(HttpStatus.OK, items.getStatusCode());

    }

    /**
     * Verifies retrieving the commodity income total via the REST API.
     */
    @Test
    public void testCommodityTotalSum() {

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer "
            + mock.getAccessToken(aTokenConfig().build()));

        ResponseEntity<Long> sum
            = getRestTemplateWithHalMessageConverter()
            .exchange("/api/incomeItems/search/sumCommodityIncome?id=654&moneyCode=RUB",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<>() {});

        Assertions.assertEquals(HttpStatus.OK, sum.getStatusCode());
    }

    /**
     * Verifies retrieving the commodity quantity total via the REST API.
     */
    @Test
    public void testCommodityTotalQty() {

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer "
            + mock.getAccessToken(aTokenConfig().build()));

        ResponseEntity<Long> sum
            = getRestTemplateWithHalMessageConverter()
            .exchange("/api/incomeItems/search/sumCommodityQuantity?id=654",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<>() {});

        Assertions.assertEquals(HttpStatus.OK, sum.getStatusCode());
    }

    /**
     * Verifies retrieving the commodity group income total via the REST API.
     */
    @Test
    public void testCommodityGroupTotalSum() {

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer "
            + mock.getAccessToken(aTokenConfig().build()));

        ResponseEntity<Long> sum
            = getRestTemplateWithHalMessageConverter()
            .exchange("/api/incomeItems/search/sumCommodityGroupIncome?id=278&moneyCode=RUB",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<>() {});

        Assertions.assertEquals(HttpStatus.OK, sum.getStatusCode());
    }

    /**
     * Verifies retrieving income monthly report via the REST API.
     */
    @Test
    public void testIncomeIncomeItems() {

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer "
                + mock.getAccessToken(aTokenConfig().build()));

        Map<String, String> uriParam = new HashMap<>();
        uriParam.put("moisStart", "1");
        uriParam.put("anStart", "2010");
        uriParam.put("moisEnd", "1");
        uriParam.put("anEnd", "2022");
        uriParam.put("code", "RUB");

        ResponseEntity<?> items
                = getRestTemplateWithHalMessageConverter()
                .exchange("/api/incomeMonthly/search/report?code={code}" +
                                "&moisStart={moisStart}&anStart={anStart}&moisEnd={moisEnd}&anEnd={anEnd}",
                        HttpMethod.GET,
                        new HttpEntity<>(headers),
                        new ParameterizedTypeReference<>() {},
                        uriParam);

        Assertions.assertEquals(HttpStatus.OK, items.getStatusCode());

    }
}
