package org.clematis.mt.config;

import static org.springdoc.core.Constants.ALL_PATTERN;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointProperties;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

/**
 *
 * @author Anton Troshin
 */
@Configuration
@ComponentScan(
    basePackages = {"org.clematis.mt"}
)
@SecurityScheme(
    name = "Bearer Authentication",
    type = SecuritySchemeType.HTTP,
    bearerFormat = "JWT",
    scheme = "bearer"
)
public class SwaggerConfig {

    private final BuildProperties buildProperties;

    public SwaggerConfig(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI().info(getInfo());
    }

    public Info getInfo() {
        return new Info().title("Clematis Money Tracker API")
            .summary("Hateoas Restful API for Dominsoft Money Tracker DB")
            .description(buildProperties.getName())
            .version(buildProperties.getVersion());
    }

    @Bean
    public GroupedOpenApi accountsApi() {
        return GroupedOpenApi.builder()
            .group("Accounts")
            .pathsToMatch("/api/accounts/**", "/api/accountGroups/**", "/api/accountsTotals/**")
            .addOpenApiCustomiser(openApi -> openApi.info(
                new Info()
                    .title("Accounts API")
                    .version(buildProperties.getVersion())
            ))
            .build();
    }

    @Bean
    public GroupedOpenApi commoditiesApi() {
        return GroupedOpenApi.builder()
            .group("Commodities")
            .pathsToMatch("/api/commodities/**", "/api/commodityGroups/**")
            .addOpenApiCustomiser(openApi -> openApi.info(
                new Info()
                    .title("Commodities API")
                    .version(buildProperties.getVersion())
            ))
            .build();
    }

    @Bean
    public GroupedOpenApi expensesApi() {
        return GroupedOpenApi.builder()
            .group("Expenses")
            .pathsToMatch("/api/expenses/**", "/api/expenseItems/**", "/api/agentCommodityGroupExpenses/**")
            .addOpenApiCustomiser(openApi -> openApi.info(
                new Info()
                    .title("Expenses API")
                    .version(buildProperties.getVersion())
            ))
            .build();
    }

    @Bean
    public GroupedOpenApi incomeApi() {
        return GroupedOpenApi.builder()
            .group("Income")
            .pathsToMatch("/api/incomes/**", "/api/incomeItems/**")
            .addOpenApiCustomiser(openApi -> openApi.info(
                new Info()
                    .title("Income API")
                    .version(buildProperties.getVersion())
            ))
            .build();
    }

    @Bean
    public GroupedOpenApi moneytypesApi() {
        return GroupedOpenApi.builder()
            .group("Money Types")
            .pathsToMatch("/api/moneytypes/**")
            .addOpenApiCustomiser(openApi -> openApi.info(
                new Info()
                    .title("Money Types API")
                    .version(buildProperties.getVersion())
            ))
            .build();
    }

    @Bean
    public GroupedOpenApi operationsApi() {
        return GroupedOpenApi.builder()
            .group("Operations")
            .pathsToMatch("/api/operations/**")
            .addOpenApiCustomiser(openApi -> openApi.info(
                new Info()
                    .title("Operations API")
                    .version(buildProperties.getVersion())
            ))
            .build();
    }

    @Bean
    public GroupedOpenApi exchangeApi() {
        return GroupedOpenApi.builder()
            .group("Money Exchange")
            .pathsToMatch("/api/exchange/**")
            .addOpenApiCustomiser(openApi -> openApi.info(
                new Info()
                    .title("Money Exchange API")
                    .version(buildProperties.getVersion())
            ))
            .build();
    }

    @Bean
    public GroupedOpenApi organizationsApi() {
        return GroupedOpenApi.builder()
            .group("Organizations")
            .pathsToMatch("/api/organizations/**", "/api/organizationGroups/**")
            .addOpenApiCustomiser(openApi -> openApi.info(
                new Info()
                    .title("Organizations API")
                    .version(buildProperties.getVersion())
            ))
            .build();
    }

    @Bean
    public GroupedOpenApi actuatorApi(WebEndpointProperties endpointProperties) {
        return GroupedOpenApi.builder()
            .group("Actuator")
            .pathsToMatch(endpointProperties.getBasePath() + ALL_PATTERN)
            .addOpenApiCustomiser(openApi -> openApi.info(
                    new Info()
                        .title("MT Actuator API")
                        .version(buildProperties.getVersion())
                )
            )
            .build();
    }
}
