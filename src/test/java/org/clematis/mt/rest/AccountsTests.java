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
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AccountsTests extends HateoasApiTests {

    private static final String USER = "user";
    private static final String PASSWORD = "719d2aca-8559-4d45-a42b-cbe34488ccb0";

    @Test
    public void testAccounts() {
        ResponseEntity<PagedModel<Account>> accounts = getRestTemplateWithHalMessageConverter()
                .withBasicAuth(USER, PASSWORD)
                .exchange("/api/accounts", HttpMethod.GET, null, new ParameterizedTypeReference<>() {});
        Assertions.assertEquals(HttpStatus.OK, accounts.getStatusCode());
    }

    @Test
    public void testAccountGroups() {
        ResponseEntity<PagedModel<AccountGroup>> accounts = getRestTemplateWithHalMessageConverter()
                .withBasicAuth(USER, PASSWORD)
                .exchange("/api/accountGroups", HttpMethod.GET, null,
                        new ParameterizedTypeReference<>() {});
        Assertions.assertEquals(HttpStatus.OK, accounts.getStatusCode());

    }

    @Test
    @SuppressFBWarnings
    public void getBalances() {
        TestRestTemplate testRestTemplate = getRestTemplateWithHalMessageConverter();
        ResponseEntity<PagedModel<AccountTotal>> accounts = testRestTemplate
                .withBasicAuth(USER, PASSWORD)
                .exchange("/api/accountsTotals", HttpMethod.GET, null,
                        new ParameterizedTypeReference<>() {});

        Assertions.assertNotNull(accounts);
        Assertions.assertNotNull(accounts.getBody());

        Collection<AccountTotal> totals = accounts.getBody().getContent();
        Assertions.assertNotNull(totals);
        Assertions.assertEquals(17, totals.size());
    }
}
