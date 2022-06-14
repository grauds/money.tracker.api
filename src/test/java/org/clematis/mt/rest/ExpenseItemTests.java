package org.clematis.mt.rest;

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

        ResponseEntity<PagedModel<ExpenseItem>> items
                = getRestTemplateWithHalMessageConverter()
                .exchange("/api/expenseItems/search", HttpMethod.GET,  new HttpEntity<>(headers),
                        new ParameterizedTypeReference<>() {});

        Assertions.assertEquals(HttpStatus.OK, items.getStatusCode());

    }

    @Test
    public void testCommodityExpenseItems() {

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer "
                + mock.getAccessToken(aTokenConfig().build()));

        ResponseEntity<PagedModel<ExpenseItem>> items
                = getRestTemplateWithHalMessageConverter()
                .exchange("/api/expenseItems/search/findByCommodityId?commodityId=258",
                        HttpMethod.GET,
                        new HttpEntity<>(headers),
                        new ParameterizedTypeReference<>() {});

        Assertions.assertEquals(HttpStatus.OK, items.getStatusCode());

    }

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
}
