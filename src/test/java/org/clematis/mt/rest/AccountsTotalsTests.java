package org.clematis.mt.rest;

import org.clematis.mt.model.Account;
import org.clematis.mt.model.views.AccountTotal;
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
public class AccountsTotalsTests extends HateoasApiTests {

    @Test
    public void testAccountsTotals() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + mock.getAccessToken(aTokenConfig().build()));

        ResponseEntity<PagedModel<AccountTotal>> accountsTotals = getRestTemplateWithHalMessageConverter()
                .exchange("/api/accountsTotals", HttpMethod.GET,  new HttpEntity<>(headers), new ParameterizedTypeReference<>() {});
        Assertions.assertEquals(HttpStatus.OK, accountsTotals.getStatusCode());
    }
}
