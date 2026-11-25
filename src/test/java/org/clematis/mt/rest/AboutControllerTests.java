package org.clematis.mt.rest;

import java.sql.Date;

import org.clematis.mt.dto.GeneralStatsDTO;
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
 * REST API tests for the About controller endpoint.
 */
@SuppressWarnings("checkstyle:MagicNumber")
public class AboutControllerTests extends HateoasApiTests {


    /**
     * Verifies retrieving the general info via the REST API with authentication.
     */
    @Test
    public void testAboutInfo() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer "
            + mock.getAccessToken(aTokenConfig().build()));

        ResponseEntity<GeneralStatsDTO> infoAboutResponseEntity = getRestTemplateWithHalMessageConverter()
            .exchange("/about", HttpMethod.GET,  new HttpEntity<>(headers),
                new ParameterizedTypeReference<>() {});

        Assertions.assertEquals(HttpStatus.OK, infoAboutResponseEntity.getStatusCode());
        Assertions.assertNotNull(infoAboutResponseEntity);
        Assertions.assertNotNull(infoAboutResponseEntity.getBody());

        GeneralStatsDTO generalStatsDTO = infoAboutResponseEntity.getBody();

        Assertions.assertNotNull(generalStatsDTO);
        Assertions.assertEquals(0, generalStatsDTO.getExpensesNoCommodity());
        Assertions.assertEquals(0, generalStatsDTO.getExpensesNoTradeplace());
        Assertions.assertEquals(2291, generalStatsDTO.getExpensesTradeplace());
        Assertions.assertEquals(17, generalStatsDTO.getAccounts());
        Assertions.assertEquals(681, generalStatsDTO.getExpenses());
        Assertions.assertEquals(685, generalStatsDTO.getIncome());
        Assertions.assertEquals(99, generalStatsDTO.getOrganizations());
        Assertions.assertEquals(Date.valueOf("2017-06-11"), generalStatsDTO.getDates().getStart());
        Assertions.assertEquals(Date.valueOf("2018-06-09"), generalStatsDTO.getDates().getEnd());
    }
}
