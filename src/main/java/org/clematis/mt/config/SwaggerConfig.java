package org.clematis.mt.config;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import lombok.Getter;
/**
 *
 * @author Anton Troshin
 */
@Configuration
public class SwaggerConfig {

    private static final String API_DESCRIPTION = "Open Doc API";
    private static final String OAUTH = "spring_oauth";
    private static final String TOKEN_NAME = "oauthtoken";

    @Value("${spring.auth.authServer}")
    private String authServer;

    @Value("${keycloak.resource}")
    private String clientId;

    @Value("${spring.auth.clientSecret}")
    private String clientSecret;

    @Value("${keycloak.realm}")
    private String realm;

    private final BuildProperties buildProperties;

    private final ApplicationContext applicationContext;

    public SwaggerConfig(BuildProperties buildProperties, ApplicationContext applicationContext) {
        this.buildProperties = buildProperties;
        this.applicationContext = applicationContext;
    }

    @PostConstruct
    public void init() {
        ConfigurableListableBeanFactory beanFactory = ((ConfigurableApplicationContext) applicationContext)
                .getBeanFactory();
        AtomicInteger count = new AtomicInteger();
        dockets().forEach(
            docket -> beanFactory.registerSingleton("Docket " + count.getAndIncrement(), docket)
        );
    }

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI();
    }

    public List<GroupedOpenApi> dockets() {
        return Stream.of(
                new GroupedOpenApiInfo("Accounts", new String[] {
                    "/api/accounts", "/api/accountGroups", "/api/accountsTotals"
                }),
                new GroupedOpenApiInfo("Commodities", new String[] {"/api/commodities", "/api/commodityGroups"}),
                new GroupedOpenApiInfo("Expenses", new String[] {"/api/expenses", "/api/expenseItems"}),
                new GroupedOpenApiInfo("Income", new String[] {"/api/incomes", "/api/incomeItems"}),
                new GroupedOpenApiInfo("Money Exchange", new String[] {"/api/moneytypes"}),
                new GroupedOpenApiInfo("Operations", new String[] {"/api/operations"}),
                new GroupedOpenApiInfo("Organizations", new String[] {
                    "/api/organizations", "/api/organizationGroups"
                })
            )
            .map(this::createDocket)
            .collect(Collectors.toList());
    }

    private GroupedOpenApi createDocket(GroupedOpenApiInfo groupedOpenApiInfo) {

        return GroupedOpenApi.builder()
                .group(groupedOpenApiInfo.getName())
                .pathsToMatch(groupedOpenApiInfo.getFilters())
                .build();
    }

    @Getter
    static class GroupedOpenApiInfo {

        private final String name;
        private final String[] filters;

        GroupedOpenApiInfo(String name, String[] filters) {

            this.name = name;
            this.filters = filters;
        }
    }
}
