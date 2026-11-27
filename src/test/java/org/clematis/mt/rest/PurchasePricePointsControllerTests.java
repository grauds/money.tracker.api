package org.clematis.mt.rest;

import java.util.List;

import org.clematis.mt.dto.PurchasePricePointDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static com.tngtech.keycloakmock.api.TokenConfig.aTokenConfig;

/**
 * REST API tests for PurchasePricePoints controller endpoints.
 */
@SuppressWarnings("checkstyle:MultipleStringLiterals")
public class PurchasePricePointsControllerTests extends HateoasApiTests {

    /**
     * Verifies retrieving purchase price points for an existing commodity.
     */
    @Test
    public void testGetPurchasePricePoints() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer "
                + mock.getAccessToken(aTokenConfig().build()));

        ResponseEntity<List<PurchasePricePointDTO>> response = getRestTemplateWithHalMessageConverter()
                .exchange("/api/commodities/258/purchasePricePoints",
                        HttpMethod.GET,
                        new HttpEntity<>(headers),
                        new ParameterizedTypeReference<>() {});

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
    }

    /**
     * Verifies retrieving purchase price points returns correct structure.
     */
    @Test
    public void testGetPurchasePricePointsStructure() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer "
                + mock.getAccessToken(aTokenConfig().build()));

        ResponseEntity<List<PurchasePricePointDTO>> response = getRestTemplateWithHalMessageConverter()
                .exchange("/api/commodities/258/purchasePricePoints",
                        HttpMethod.GET,
                        new HttpEntity<>(headers),
                        new ParameterizedTypeReference<>() {});

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());

        for (PurchasePricePointDTO dto : response.getBody()) {
            Assertions.assertNotNull(dto.getTradeplaceName());
            Assertions.assertNotNull(dto.getTradeplaceId());
            Assertions.assertNotNull(dto.getTransferDate());
            Assertions.assertNotNull(dto.getPrice());
        }
    }

    /**
     * Verifies retrieving purchase price points returns 404 for non-existent commodity.
     */
    @Test
    public void testGetPurchasePricePointsNotFound() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer "
                + mock.getAccessToken(aTokenConfig().build()));

        ResponseEntity<List<PurchasePricePointDTO>> response = getRestTemplateWithHalMessageConverter()
                .exchange("/api/commodities/999999/purchasePricePoints",
                        HttpMethod.GET,
                        new HttpEntity<>(headers),
                        new ParameterizedTypeReference<>() {});

        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
