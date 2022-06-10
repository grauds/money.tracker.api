package org.clematis.mt.rest;

import org.clematis.mt.model.Commodity;
import org.clematis.mt.model.CommodityGroup;
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
public class CommodityTests extends HateoasApiTests {

    @Test
    public void testCommodities() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer "
                + mock.getAccessToken(aTokenConfig().build()));

        ResponseEntity<PagedModel<CommodityGroup>> groups
                = getRestTemplateWithHalMessageConverter()
                .exchange("/api/commodityGroups/search/recursiveByParentId?id=303", HttpMethod.GET,  new HttpEntity<>(headers),
                        new ParameterizedTypeReference<>() {});

        Assertions.assertEquals(HttpStatus.OK, groups.getStatusCode());

        ResponseEntity<PagedModel<Commodity>> commodities
                = getRestTemplateWithHalMessageConverter()
                .exchange("/api/commodities/search/recursiveByParentId?id=303", HttpMethod.GET,  new HttpEntity<>(headers),
                        new ParameterizedTypeReference<>() {});

        Assertions.assertEquals(HttpStatus.OK, commodities.getStatusCode());
    }
}
