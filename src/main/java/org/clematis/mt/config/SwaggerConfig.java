package org.clematis.mt.config;

import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.GroupedOpenApi;
import org.springdoc.core.SwaggerUiConfigProperties;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointProperties;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;

import static org.springdoc.core.Constants.ALL_PATTERN;

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


    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI().info(getInfo());
    }

    public Info getInfo() {
        return new Info().title("Money Tracker API")
                .summary("Hateoas Restful API for Dominsoft Money Tracker DB")
                .description("")
                .version(buildProperties.getVersion())
                .description(buildProperties.getName());
    }

    @Bean
    public GroupedOpenApi accountsApi() {
        return GroupedOpenApi.builder()
                .group("Accounts")
                .pathsToMatch("/api/accounts", "/api/accountGroups", "/api/accountsTotals")
                .build();
    }

    @Bean
    public GroupedOpenApi commoditiesApi() {
        return GroupedOpenApi.builder()
                .group("Commodities")
                .pathsToMatch("/api/commodities", "/api/commodityGroups")
                .build();
    }

    @Bean
    public GroupedOpenApi expensesApi() {
        return GroupedOpenApi.builder()
                .group("Expenses")
                .pathsToMatch("/api/expenses", "/api/expenseItems")
                .build();
    }

    @Bean
    public GroupedOpenApi incomeApi() {
        return GroupedOpenApi.builder()
                .group("Income")
                .pathsToMatch("/api/incomes", "/api/incomeItems")
                .build();
    }

    @Bean
    public GroupedOpenApi moneytypesApi() {
        return GroupedOpenApi.builder()
                .group("Money Types")
                .pathsToMatch("/api/moneytypes")
                .build();
    }

    @Bean
    public GroupedOpenApi operationsApi() {
        return GroupedOpenApi.builder()
                .group("Operations")
                .pathsToMatch("/api/operations")
                .build();
    }

    @Bean
    public GroupedOpenApi organizationsApi() {
        return GroupedOpenApi.builder()
                .group("Organizations")
                .pathsToMatch("/api/organizations", "/api/organizationGroups")
                .build();
    }

    @Bean
    public GroupedOpenApi actuatorApi(OpenApiCustomiser actuatorOpenApiCustomiser,
                                      WebEndpointProperties endpointProperties) {
        return GroupedOpenApi.builder()
                .group("Actuator")
                .pathsToMatch(endpointProperties.getBasePath() + ALL_PATTERN)
                .addOpenApiCustomiser(actuatorOpenApiCustomiser)
                .addOpenApiCustomiser(openApi -> openApi.info(new Info()
                        .title("Money Tracker Actuator API")
                        .version(buildProperties.getVersion()))
                )
                .pathsToExclude("/rest/actuator/health/**")
                .pathsToExclude("/rest/actuator/health/*")
                .build();
    }

    @Bean
    SwaggerUiConfigProperties swaggerUiConfig() {
        SwaggerUiConfigProperties config = new SwaggerUiConfigProperties();
        config.setShowCommonExtensions(true);
        return config;
    }

}
