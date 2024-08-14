package org.clematis.mt.rest;

import static com.tngtech.keycloakmock.api.TokenConfig.aTokenConfig;
import java.sql.Date;

import org.clematis.mt.dto.InfoAbout;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * @author Anton Troshin
 */
public class AboutControllerTests extends HateoasApiTests {


    @Test
    public void testAboutInfo() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer "
            + mock.getAccessToken(aTokenConfig().build()));

        ResponseEntity<InfoAbout> infoAboutResponseEntity = getRestTemplateWithHalMessageConverter()
            .exchange("/about", HttpMethod.GET,  new HttpEntity<>(headers),
                new ParameterizedTypeReference<>() {});

        Assertions.assertEquals(HttpStatus.OK, infoAboutResponseEntity.getStatusCode());
        Assertions.assertNotNull(infoAboutResponseEntity);
        Assertions.assertNotNull(infoAboutResponseEntity.getBody());

        InfoAbout infoAbout = infoAboutResponseEntity.getBody();

        Assertions.assertNotNull(infoAbout);
        Assertions.assertEquals(0, infoAbout.getExpensesNoCommodity());
        Assertions.assertEquals(0, infoAbout.getExpensesNoTradeplace());
        Assertions.assertEquals(2291, infoAbout.getExpensesTradeplace());
        Assertions.assertEquals(17, infoAbout.getAccounts());
        Assertions.assertEquals(681, infoAbout.getExpenses());
        Assertions.assertEquals(685, infoAbout.getIncome());
        Assertions.assertEquals(99, infoAbout.getOrganizations());
        Assertions.assertEquals(Date.valueOf("2017-06-11"), infoAbout.getDates().getStart());
        Assertions.assertEquals(Date.valueOf("2018-06-09"), infoAbout.getDates().getEnd());
    }
}
