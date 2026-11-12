package org.clematis.mt.repositories;

import java.util.List;

import org.clematis.mt.ClematisMoneyTrackerApplicationTests;
import org.clematis.mt.dto.MoneyShareInCurrency;
import org.clematis.mt.model.MoneyTypeCode;
import org.clematis.mt.repository.IncomeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * Repository tests for Income data access.
 */
public class IncomeRepositoryTests extends ClematisMoneyTrackerApplicationTests {

    @Autowired
    private IncomeRepository incomeRepository;

    /**
     * Verifies the total income amount in RUB.
     */
    @Test
    public void testSumTotalInCurrency() {
        Double result = incomeRepository.sumTotalInCurrency(String.valueOf(MoneyTypeCode.RUB));
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result > 0);
    }

    /**
     * Verifies the total income amount in USD.
     */
    @Test
    public void testSumTotalInCurrencyUsd() {
        Double result = incomeRepository.sumTotalInCurrency(String.valueOf(MoneyTypeCode.USD));
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result > 0);
    }

    /**
     * Verifies income breakdown by currency in RUB.
     */
    @Test
    public void testGetIncomeByCurrency() {
        List<MoneyShareInCurrency> result = incomeRepository.getIncomeByCurrency(String.valueOf(MoneyTypeCode.RUB));
        Assertions.assertNotNull(result);
        Assertions.assertFalse(result.isEmpty());

        for (MoneyShareInCurrency row : result) {
            Assertions.assertNotNull(row.getCode());
            Assertions.assertNotNull(row.getTotal());
            Assertions.assertNotNull(row.getTotalConverted());
            Assertions.assertTrue(row.getTotal() >= 0);
            Assertions.assertTrue(row.getTotalConverted() > 0);
        }
    }

    /**
     * Verifies income breakdown by currency in USD.
     */
    @Test
    public void testGetIncomeByCurrencyUsd() {
        List<MoneyShareInCurrency> result = incomeRepository.getIncomeByCurrency(String.valueOf(MoneyTypeCode.USD));
        Assertions.assertNotNull(result);
        Assertions.assertFalse(result.isEmpty());

        for (MoneyShareInCurrency row : result) {
            Assertions.assertNotNull(row.getCode());
            Assertions.assertNotNull(row.getTotal());
            Assertions.assertNotNull(row.getTotalConverted());
        }
    }
}
