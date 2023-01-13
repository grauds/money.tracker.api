package org.clematis.mt.repositories;

import org.clematis.mt.ClematisMoneyTrackerApplicationTests;
import org.clematis.mt.repository.MoneyExchangeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class MoneyExchangeRepositoryTests extends ClematisMoneyTrackerApplicationTests {

    @Autowired
    private MoneyExchangeRepository moneyExchangeRepository;

    @Test
    void findAll() {
        Assertions.assertEquals(5, this.moneyExchangeRepository.count());
    }

    @Test
    void getAverageRate() {

        Assertions.assertEquals(0.03988699100577624,
                this.moneyExchangeRepository.getAverageExchangeRate("USD", "RUB"));

        Assertions.assertEquals(0.02857142857142857,
                this.moneyExchangeRepository.getAverageExchangeRate("EUR", "RUB"));
    }
}
