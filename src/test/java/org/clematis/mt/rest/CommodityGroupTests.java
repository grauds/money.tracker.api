package org.clematis.mt.rest;

import static com.tngtech.keycloakmock.api.TokenConfig.aTokenConfig;

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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CommodityGroupTests extends HateoasApiTests {

    @Test
    public void testCommodityGroups() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer "
            + mock.getAccessToken(aTokenConfig().build()));

        ResponseEntity<PagedModel<CommodityGroup>> all
            = getRestTemplateWithHalMessageConverter()
            .exchange("/api/commodityGroups/search/recursiveWithCommoditiesByParentId?id=303", HttpMethod.GET,  new HttpEntity<>(headers),
                new ParameterizedTypeReference<>() {});

        Assertions.assertEquals(HttpStatus.OK, all.getStatusCode());
    }
}
