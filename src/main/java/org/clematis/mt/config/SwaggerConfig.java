package org.clematis.mt.config;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.AuthorizationCodeGrantBuilder;
import springfox.documentation.builders.OAuthBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.GrantType;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.service.TokenEndpoint;
import springfox.documentation.service.TokenRequestEndpoint;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger.web.SecurityConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
/**
 *
 * @author Anton Troshin
 */
@EnableSwagger2
@Configuration
@Conditional(NotLocalEnvironment.class)
public class SwaggerConfig {

    private static final String API_DESCRIPTION = "Swagger API";
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
        dockets().forEach(docket -> beanFactory.registerSingleton("Docket " + count.getAndIncrement(),
                docket));
    }

    public List<Docket> dockets() {
        return Stream.of(new DocketInfo("Accounts", List.of("/api/accounts", "/api/accountGroups",
                                "/api/accountsTotals")),
                new DocketInfo("Commodities", List.of("/api/commodities", "/api/commodityGroups")),
                new DocketInfo("Expenses", List.of("/api/expenses", "/api/expenseItems")),
                new DocketInfo("Income", List.of("/api/incomes", "/api/incomeItems")),
                new DocketInfo("Money Exchange", List.of("/api/moneytypes")),
                new DocketInfo("Operations", List.of("/api/operations")),
                new DocketInfo("Organizations", List.of("/api/organizations", "/api/organizationGroups")))
                .map(this::createDocket)
                .collect(Collectors.toList());
    }

    private Docket createDocket(DocketInfo docketInfo) {

        List<Predicate<String>> selectors = docketInfo.getFilters()
                .stream()
                .map(PathSelectors::regex)
                .collect(Collectors.toList());

        Predicate<String> chained = null;

        if (selectors.stream().findFirst().isPresent()) {
            chained = selectors.stream().findFirst().get();
            for (Predicate<String> selector : selectors) {
                chained = chained.or(selector);
            }

        }

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .securityContexts(List.of(securityContext()))
                .securitySchemes(List.of(securityScheme()))
                .groupName(docketInfo.getName())
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(chained)
                .build();
    }

    private SecurityScheme securityScheme() {
        GrantType grantType = new AuthorizationCodeGrantBuilder()
                .tokenEndpoint(new TokenEndpoint(authServer + "/token", TOKEN_NAME))
                .tokenRequestEndpoint(
                        new TokenRequestEndpoint(authServer + "/auth",  clientId, clientSecret))
                .build();

        return new OAuthBuilder().name(OAUTH)
                .grantTypes(List.of(grantType))
                .scopes(List.of(scopes()))
                .build();
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(
                        List.of(new SecurityReference(OAUTH, scopes())))
                .forPaths(PathSelectors.regex("/api.*"))
                .build();
    }

    private AuthorizationScope[] scopes() {
        return new AuthorizationScope[]{
            new AuthorizationScope("read", "for read operations"),
            new AuthorizationScope("write", "for write operations")
        };
    }

    @Bean
    public SecurityConfiguration security() {
        return SecurityConfigurationBuilder.builder()
                .clientId(clientId)
                .clientSecret(clientSecret)
                .realm(realm)
                .scopeSeparator(" ")
                .useBasicAuthenticationWithAccessCodeGrant(true)
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(getTitle())
                .description(API_DESCRIPTION)
                .version(getVersion())
                .build();
    }

    private String getTitle() {
        return buildProperties.getName();
    }

    private String getVersion() {
        return buildProperties.getVersion();
    }

    @Getter
    static class DocketInfo {

        private final String name;
        private final List<String> filters;

        DocketInfo(String name, List<String> filters) {

            this.name = name;
            this.filters = filters;
        }
    }
}
