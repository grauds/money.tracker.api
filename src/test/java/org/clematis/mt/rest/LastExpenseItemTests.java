package org.clematis.mt.rest;


import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.clematis.mt.model.LastExpenseItem;
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

public class LastExpenseItemTests extends HateoasApiTests {

    @Test
    public void testLastExpenseItems() {

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer "
                + mock.getAccessToken(aTokenConfig().build()));

        ResponseEntity<PagedModel<LastExpenseItem>> items
                = getRestTemplateWithHalMessageConverter()
                .exchange("/api/lastExpenseItems/search", HttpMethod.GET,  new HttpEntity<>(headers),
                        new ParameterizedTypeReference<>() {});

        Assertions.assertEquals(HttpStatus.OK, items.getStatusCode());

    }

    @Test
    @SuppressFBWarnings
    public void testSearchLastExpenseItems() {

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer "
                + mock.getAccessToken(aTokenConfig().build()));

        Map<String, String> uriParam = new HashMap<>();
        uriParam.put("name", "Н");
        uriParam.put("page", "1");
        uriParam.put("size", "12");
        uriParam.put("sort", "name,DESC");

        ResponseEntity<PagedModel<LastExpenseItem>> items
                = getRestTemplateWithHalMessageConverter()
                .exchange("/api/lastExpenseItems/search/findByNameStarting?" +
                                "name={name}&page={page}&size={size}&sort={sort}",
                        HttpMethod.GET,
                        new HttpEntity<>(headers),
                        new ParameterizedTypeReference<>() {},
                        uriParam);

        Assertions.assertEquals(HttpStatus.OK, items.getStatusCode());
        Assertions.assertEquals(16, Objects.requireNonNull(
                Objects.requireNonNull(items.getBody()).getMetadata()
        ).getTotalElements());
    }
}
