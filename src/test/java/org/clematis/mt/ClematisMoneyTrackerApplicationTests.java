package org.clematis.mt;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import org.firebirdsql.testcontainers.FirebirdContainer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
//import org.testcontainers.utility.MountableFile;

@Testcontainers
@SpringBootTest
class ClematisMoneyTrackerApplicationTests {

    private static final DockerImageName IMAGE =
            DockerImageName.parse(FirebirdContainer.IMAGE).withTag("2.5.9-sc");

    @Container
    private static final FirebirdContainer<?> container = new FirebirdContainer<>(IMAGE)
            .withUsername("testuser")
            .withPassword("testpassword");
            //.withCopyFileToContainer(MountableFile.forClasspathResource("mt.fdb"), "/firebird/data");

    @DynamicPropertySource
    static void init(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.password", container::getPassword);
        registry.add("spring.datasource.username", container::getUsername);
    }

    public void canConnectToContainer() throws Exception {
        try (Connection connection = DriverManager
                .getConnection(container.getJdbcUrl(), container.getUsername(), container.getPassword());
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("select CURRENT_USER from RDB$DATABASE")) {
            Assertions.assertTrue(rs.next(), "has row");
            Assertions.assertEquals("TESTUSER", rs.getString(1), "user name");
        }
    }

    @Test
    void contextLoads() throws Exception {
        canConnectToContainer();
    }
}
