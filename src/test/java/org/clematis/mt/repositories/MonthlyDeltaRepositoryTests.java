package org.clematis.mt.repositories;

import org.clematis.mt.ClematisMoneyTrackerApplicationTests;
import org.clematis.mt.repository.MonthlyDeltaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Anton Troshin
 */
public class MonthlyDeltaRepositoryTests extends ClematisMoneyTrackerApplicationTests {

    @Autowired
    private MonthlyDeltaRepository monthlyDeltaRepository;

    @Test
    void countDeltas() {
        Assertions.assertEquals(27, monthlyDeltaRepository.findAll().size());
    }

    @Test
    void countBalances() {
        Assertions.assertEquals(16289,
                monthlyDeltaRepository.getBalanceForCurrency(2018, 2, "RUB"));
    }
}
