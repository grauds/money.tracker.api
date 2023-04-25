package org.clematis.mt.rest;

import static com.tngtech.keycloakmock.api.TokenConfig.aTokenConfig;
import java.util.HashMap;
import java.util.Map;

import org.clematis.mt.dto.NamedEntityLink;
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

        Map<String, String> uriParam = new HashMap<>();
        uriParam.put("page", "0");
        uriParam.put("size", "12");
        uriParam.put("sort", "name,DESC");
        uriParam.put("id", "303");

        ResponseEntity<PagedModel<NamedEntityLink>> all
            = getRestTemplateWithHalMessageConverter()
            .exchange("/api/commodityGroups/search/" +
                    "recursiveWithCommoditiesByParentId?page={page}&size={size}&sort={sort}&id={id}",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<>() {},
                uriParam);

        // see https://github.com/spring-projects/spring-data-rest/issues/1213
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, all.getStatusCode());
    }
}
