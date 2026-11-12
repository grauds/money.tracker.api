package org.clematis.mt.rest;

import java.util.ArrayList;
import java.util.List;

import org.clematis.mt.ClematisMoneyTrackerApplicationTests;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.hateoas.server.mvc.TypeConstrainedMappingJackson2HttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;

import static com.tngtech.keycloakmock.api.ServerConfig.aServerConfig;

import com.tngtech.keycloakmock.junit5.KeycloakMockExtension;


/**
 * Abstract base class for HATEOAS REST API tests.
 * Provides a shared Keycloak mock and configured RestTemplate with HAL message converter.
 */
abstract class HateoasApiTests extends ClematisMoneyTrackerApplicationTests {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @RegisterExtension
    static KeycloakMockExtension mock = new KeycloakMockExtension(
            aServerConfig().withRealm("clematis").build()
    );

    @Autowired
    @Qualifier("halJacksonHttpMessageConverter")
    private TypeConstrainedMappingJackson2HttpMessageConverter halJacksonHttpMessageConverter;

    /**
     * Returns a RestTemplate configured with the HAL message converter.
     *
     * @return RestTemplate with HAL support
     */
    public TestRestTemplate getRestTemplateWithHalMessageConverter() {
        List<HttpMessageConverter<?>> existingConverters = testRestTemplate.getRestTemplate().getMessageConverters();
        List<HttpMessageConverter<?>> newConverters = new ArrayList<>();
        newConverters.add(halJacksonHttpMessageConverter);
        newConverters.addAll(existingConverters);
        testRestTemplate.getRestTemplate().setMessageConverters(newConverters);
        return testRestTemplate;
    }
}
