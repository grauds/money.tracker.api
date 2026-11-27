package org.clematis.mt;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import org.clematis.storage.client.StorageApiClient;
import org.firebirdsql.testcontainers.FirebirdContainer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.MountableFile;

import lombok.extern.java.Log;


/**
 * Base integration test class with Testcontainers setup for the Firebird database.
 * Provides a shared Firebird database container for all tests.
 */
@SuppressWarnings("checkstyle:MultipleStringLiterals")
@Testcontainers
@SpringBootTest(
        classes = ClematisMoneyTrackerApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles({"test", "local"})
@Log
public class ClematisMoneyTrackerApplicationTests {

    private static final DockerImageName IMAGE =
            DockerImageName.parse(FirebirdContainer.IMAGE).withTag("2.5.9-sc");

    private static final FirebirdContainer<?> FIREBIRD_CONTAINER;

    static {
        FIREBIRD_CONTAINER = new FirebirdContainer<>(IMAGE)
                .withDatabaseName("mt.fdb")
                .withUsername("SYSDBA")
                .withCopyFileToContainer(
                        MountableFile.forClasspathResource("mt.fdb"), "/firebird/data/mt.fdb"
                )
                .withUrlParam("encoding", "win1251");
        FIREBIRD_CONTAINER.start();
    }

    @MockBean
    protected StorageApiClient storageApiClient;

    @DynamicPropertySource
    static void init(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", FIREBIRD_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.password", FIREBIRD_CONTAINER::getPassword);
        registry.add("spring.datasource.username", FIREBIRD_CONTAINER::getUsername);
    }

    /**
     * Verifies that the test container Firebird database is accessible.
     *
     * @throws Exception if the connection fails
     */
    @Test
    public void canConnectToContainer() throws Exception {
        try (Connection connection = DriverManager
                .getConnection(
                    FIREBIRD_CONTAINER.getJdbcUrl(),
                    FIREBIRD_CONTAINER.getUsername(),
                    FIREBIRD_CONTAINER.getPassword()
                );
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery("select CURRENT_USER from RDB$DATABASE")
        ) {
            Assertions.assertTrue(rs.next(), "has row");
            Assertions.assertEquals("SYSDBA", rs.getString(1), "user name");
        }
    }

}
