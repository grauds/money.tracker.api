package org.clematis.mt.rest;

import java.util.HashMap;
import java.util.Map;

import org.clematis.mt.model.ExpenseItem;
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

public class ExpenseItemTests extends HateoasApiTests {

    @Test
    public void testExpenseItems() {

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer "
                + mock.getAccessToken(aTokenConfig().build()));

        Map<String, String> uriParam = new HashMap<>();
        uriParam.put("page", "1");
        uriParam.put("size", "12");
        uriParam.put("sort", "name,DESC");

        ResponseEntity<PagedModel<ExpenseItem>> items
                = getRestTemplateWithHalMessageConverter()
                .exchange("/api/expenseItems?page={page}&size={size}&sort={sort}", HttpMethod.GET,
                        new HttpEntity<>(headers),
                        new ParameterizedTypeReference<>() {},
                        uriParam);

        Assertions.assertEquals(HttpStatus.OK, items.getStatusCode());

    }

    @Test
    public void testCommodityExpenseItems() {

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer "
                + mock.getAccessToken(aTokenConfig().build()));

        ResponseEntity<PagedModel<ExpenseItem>> items
                = getRestTemplateWithHalMessageConverter()
                .exchange("/api/expenseItems/search/commodity?commodityId=258",
                        HttpMethod.GET,
                        new HttpEntity<>(headers),
                        new ParameterizedTypeReference<>() {});

        Assertions.assertEquals(HttpStatus.OK, items.getStatusCode());

    }
/*
    @Test
    public void testExpenseItemsCount() {

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer "
                + mock.getAccessToken(aTokenConfig().build()));

        ResponseEntity<Integer> count
                = getRestTemplateWithHalMessageConverter()
                .exchange("/api/expenseItems/count",
                        HttpMethod.GET,
                        new HttpEntity<>(headers),
                        new ParameterizedTypeReference<>() {});

        Assertions.assertEquals(HttpStatus.OK, count.getStatusCode());
        Assertions.assertEquals(345, count.getBody());
    }*/

    @Test
    public void testCommodityTotalSum() {

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer "
                + mock.getAccessToken(aTokenConfig().build()));

        ResponseEntity<Long> sum
                = getRestTemplateWithHalMessageConverter()
                .exchange("/api/expenseItems/search/sumCommodityExpenses?commodityId=258&moneyCode=RUB",
                        HttpMethod.GET,
                        new HttpEntity<>(headers),
                        new ParameterizedTypeReference<>() {});

        Assertions.assertEquals(HttpStatus.OK, sum.getStatusCode());
    }

    @Test
    public void testCommodityTotalQty() {

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer "
                + mock.getAccessToken(aTokenConfig().build()));

        ResponseEntity<Long> sum
                = getRestTemplateWithHalMessageConverter()
                .exchange("/api/expenseItems/search/sumCommodityQuantity?commodityId=258",
                        HttpMethod.GET,
                        new HttpEntity<>(headers),
                        new ParameterizedTypeReference<>() {});

        Assertions.assertEquals(HttpStatus.OK, sum.getStatusCode());
    }


    @Test
    public void testCommodityGroupTotalSum() {

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer "
                + mock.getAccessToken(aTokenConfig().build()));

        ResponseEntity<Long> sum
                = getRestTemplateWithHalMessageConverter()
                .exchange("/api/expenses/search/sumCommodityGroupExpenses?commodityGroupId=287&moneyCode=RUB",
                        HttpMethod.GET,
                        new HttpEntity<>(headers),
                        new ParameterizedTypeReference<>() {});

        Assertions.assertEquals(HttpStatus.OK, sum.getStatusCode());
    }

    @Test
    public void testAgentCommodityGroupTotalSum() {

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer "
              + mock.getAccessToken(aTokenConfig().build()));

        ResponseEntity<?> response = getRestTemplateWithHalMessageConverter()
                .exchange("/api/agentCommodityGroupExpenses?code=RUB&mois=3&an=2018",
                        HttpMethod.GET,
                        new HttpEntity<>(headers),
                        new ParameterizedTypeReference<>() {});

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
