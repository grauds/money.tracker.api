package org.clematis.mt.rest;

import java.util.Collection;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.clematis.mt.model.Account;
import org.clematis.mt.model.AccountGroup;
import org.clematis.mt.model.views.AccountTotal;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static com.tngtech.keycloakmock.api.TokenConfig.aTokenConfig;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AccountsTests extends HateoasApiTests {


    @Test
    public void testAccounts() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + mock.getAccessToken(aTokenConfig().build()));

        ResponseEntity<PagedModel<Account>> accounts = getRestTemplateWithHalMessageConverter()
                .exchange("/api/accounts", HttpMethod.GET,  new HttpEntity<>(headers), new ParameterizedTypeReference<>() {});
        Assertions.assertEquals(HttpStatus.OK, accounts.getStatusCode());
    }

    @Test
    public void testAccountGroups() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + mock.getAccessToken(aTokenConfig().build()));

        ResponseEntity<PagedModel<AccountGroup>> accounts = getRestTemplateWithHalMessageConverter()
                .exchange("/api/accountGroups", HttpMethod.GET, new HttpEntity<>(headers),
                        new ParameterizedTypeReference<>() {});
        Assertions.assertEquals(HttpStatus.OK, accounts.getStatusCode());

    }

    @Test
    @SuppressFBWarnings
    public void getBalances() {
        TestRestTemplate testRestTemplate = getRestTemplateWithHalMessageConverter();

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + mock.getAccessToken(aTokenConfig().build()));

        ResponseEntity<PagedModel<AccountTotal>> accounts = testRestTemplate
                .exchange("/api/accountsTotals", HttpMethod.GET,  new HttpEntity<>(headers),
                        new ParameterizedTypeReference<>() {});

        Assertions.assertEquals(HttpStatus.OK, accounts.getStatusCode());
        Assertions.assertNotNull(accounts);
        Assertions.assertNotNull(accounts.getBody());

        Collection<AccountTotal> totals = accounts.getBody().getContent();
        Assertions.assertNotNull(totals);
        Assertions.assertEquals(17, totals.size());
    }
}
