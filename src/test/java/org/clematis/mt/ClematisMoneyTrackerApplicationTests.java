package org.clematis.mt;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

@SpringBootTest
@TestConfiguration
class ClematisMoneyTrackerApplicationTests {

    @Test
    void contextLoads() {
    }

    @Bean
    public DataSource dataSource() {
        return Mockito.mock(DataSource.class);
    }
}
