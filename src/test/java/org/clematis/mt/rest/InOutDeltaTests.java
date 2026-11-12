package org.clematis.mt.rest;

import java.util.HashMap;
import java.util.Map;

import org.clematis.mt.model.InOutDelta;
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
 * REST API tests for InOutDelta endpoints.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class InOutDeltaTests extends HateoasApiTests {

    /**
     * Verifies retrieving in/out deltas via the REST API with authentication.
     */
    @Test
    public void testInOutDeltas() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION,
                "Bearer " + mock.getAccessToken(aTokenConfig().build()));

        ResponseEntity<PagedModel<?>> inOutDeltas = getRestTemplateWithHalMessageConverter()
                .exchange("/api/inOutDeltas", HttpMethod.GET,  new HttpEntity<>(headers),
                        new ParameterizedTypeReference<>() {});
        Assertions.assertEquals(HttpStatus.OK, inOutDeltas.getStatusCode());
    }

    /**
     * Verifies retrieving in/out deltas filtered by currency code.
     */
    @Test
    public void testDeltasInCode() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION,
                "Bearer " + mock.getAccessToken(aTokenConfig().build()));

        Map<String, String> uriParam = new HashMap<>();
        uriParam.put("code", "USD");

        ResponseEntity<PagedModel<InOutDelta>> inOutDeltas = getRestTemplateWithHalMessageConverter()
                .exchange("/api/inOutDeltas/search/code?code={code}", HttpMethod.GET,  new HttpEntity<>(headers),
                        new ParameterizedTypeReference<>() {}, uriParam);
        Assertions.assertEquals(HttpStatus.OK, inOutDeltas.getStatusCode());
    }
}
