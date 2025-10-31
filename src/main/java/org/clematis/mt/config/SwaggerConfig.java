package org.clematis.mt.config;

import static org.springdoc.core.utils.Constants.ALL_PATTERN;
import org.springdoc.core.models.GroupedOpenApi;
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
        return new Info().title("Clematis Money Tracker AP")
            .summary("Hateoas Restful API for Dominsoft Money Tracker DB")
            .description(buildProperties.getName())
            .version(buildProperties.getVersion());
    }

    @Bean
    public GroupedOpenApi accountsApi() {
        return GroupedOpenApi.builder()
            .group("Accounts")
            .pathsToMatch("/api/accounts/**", "/api/accountGroups/**", "/api/accountsTotals/**")
            .addOpenApiCustomizer(openApi -> openApi.info(
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
            .addOpenApiCustomizer(openApi -> openApi.info(
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
            .addOpenApiCustomizer(openApi -> openApi.info(
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
            .addOpenApiCustomizer(openApi -> openApi.info(
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
            .addOpenApiCustomizer(openApi -> openApi.info(
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
            .addOpenApiCustomizer(openApi -> openApi.info(
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
            .addOpenApiCustomizer(openApi -> openApi.info(
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
            .addOpenApiCustomizer(openApi -> openApi.info(
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
            .addOpenApiCustomizer(openApi -> openApi.info(
                    new Info()
                        .title("MT Actuator API")
                        .version(buildProperties.getVersion())
                )
            )
            .build();
    }
}
