package org.clematis.mt.rest;

import org.clematis.mt.model.Organization;
import org.clematis.mt.model.OrganizationGroup;
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
public class OrganizationTests extends HateoasApiTests {

    @Test
    public void testOrganizations() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer "
                + mock.getAccessToken(aTokenConfig().build()));

        ResponseEntity<PagedModel<OrganizationGroup>> groups
                = getRestTemplateWithHalMessageConverter()
                .exchange("/api/organizationGroups/search/recursiveByParentId?id=264", HttpMethod.GET,
                        new HttpEntity<>(headers),
                        new ParameterizedTypeReference<>() {});

        Assertions.assertEquals(HttpStatus.OK, groups.getStatusCode());

        ResponseEntity<PagedModel<Organization>> organizations
                = getRestTemplateWithHalMessageConverter()
                .exchange("/api/organizations/search/recursiveByParentGroupId?id=264", HttpMethod.GET,
                        new HttpEntity<>(headers),
                        new ParameterizedTypeReference<>() {});

        Assertions.assertEquals(HttpStatus.OK, organizations.getStatusCode());
    }
}
