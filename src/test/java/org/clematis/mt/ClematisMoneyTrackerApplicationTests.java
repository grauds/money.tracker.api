package org.clematis.mt;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import com.tngtech.keycloakmock.junit5.KeycloakMockExtension;
import org.firebirdsql.testcontainers.FirebirdContainer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.MountableFile;

@Testcontainers
@SpringBootTest(
        classes = ClematisMoneyTrackerApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ClematisMoneyTrackerApplicationTests {

    public static final Logger LOGGER = LoggerFactory.getLogger(ClematisMoneyTrackerApplicationTests.class);

    private static final DockerImageName IMAGE =
            DockerImageName.parse(FirebirdContainer.IMAGE).withTag("2.5.9-sc");

    private static final FirebirdContainer<?> container;

    static {
        container = new FirebirdContainer<>(IMAGE)
                .withDatabaseName("mt.fdb")
                .withUsername("SYSDBA")
                .withCopyFileToContainer(MountableFile.forClasspathResource("mt.fdb"), "/firebird/data/mt.fdb")
                .withLogConsumer(new Slf4jLogConsumer(LOGGER));
        container.start();
    }

    @DynamicPropertySource
    static void init(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.password", container::getPassword);
        registry.add("spring.datasource.username", container::getUsername);
    }

    @Test
    public void canConnectToContainer() throws Exception {
        try (Connection connection = DriverManager
                .getConnection(container.getJdbcUrl(), container.getUsername(), container.getPassword());
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("select CURRENT_USER from RDB$DATABASE")) {
                 Assertions.assertTrue(rs.next(), "has row");
                 Assertions.assertEquals("SYSDBA", rs.getString(1), "user name");
        }
    }
}
