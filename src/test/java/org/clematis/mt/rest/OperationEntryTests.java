package org.clematis.mt.rest;

import java.util.HashMap;
import java.util.Map;

import org.clematis.mt.model.views.OperationEntry;
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

public class OperationEntryTests extends HateoasApiTests {

    @Test
    public void testSorting() {

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION,
                "Bearer " + mock.getAccessToken(aTokenConfig().build()));


        Map<String, String> uriParam = new HashMap<>();
        uriParam.put("page", "1");
        uriParam.put("size", "12");
        uriParam.put("sort", "amount,DESC");

        ResponseEntity<PagedModel<OperationEntry>> deltas = getRestTemplateWithHalMessageConverter()
                .exchange("/api/operations?page={page}&size={size}&sort={sort}",
                        HttpMethod.GET,
                        new HttpEntity<>(headers),
                        new ParameterizedTypeReference<>() {},
                        uriParam);
        Assertions.assertEquals(HttpStatus.OK, deltas.getStatusCode());
    }
}
