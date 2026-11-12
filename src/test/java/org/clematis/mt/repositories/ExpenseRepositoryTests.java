package org.clematis.mt.repositories;

import java.sql.Date;
import java.util.List;

import org.clematis.mt.ClematisMoneyTrackerApplicationTests;
import org.clematis.mt.dto.AgentCommodityGroup;
import org.clematis.mt.dto.DateRange;
import org.clematis.mt.dto.MoneyShareInCurrency;
import org.clematis.mt.model.Expense;
import org.clematis.mt.model.MoneyTypeCode;
import org.clematis.mt.repository.ExpenseRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * Repository tests for Expense data access.
 */
public class ExpenseRepositoryTests extends ClematisMoneyTrackerApplicationTests {

    @Autowired
    private ExpenseRepository expenseRepository;

    /**
     * Verifies the total count of expenses.
     */
    @Test
    public void testGetAll() {
        Iterable<Expense> result = expenseRepository.findAll();
        Assertions.assertEquals(681, result.spliterator().estimateSize());
    }

    /**
     * Verifies the agent/commodity group expense total is correctly calculated.
     */
    @Test
    public void testAgentCommodityTotalSum() {
        List<AgentCommodityGroup> groups = expenseRepository.getAgentCommodityGroups("RUB",
            3, 2018, 3, 2019);
        Assertions.assertEquals(142, groups.size());
    }

    /**
     * Verifies handling of invalid date ranges for agent/commodity group expenses.
     */
    @Test
    public void testAgentCommodityTotalSumInvalidDates() {
        List<AgentCommodityGroup> groups = expenseRepository.getAgentCommodityGroups("RUB",
            3, 2026, 3, 2030);
        Assertions.assertEquals(0, groups.size());
    }

    /**
     * Verifies the date range retrieval for expenses.
     */
    @Test
    public void testDates() {
        DateRange range = expenseRepository.getDatesRange();
        Assertions.assertEquals(Date.valueOf("2017-06-11"), range.getStart());
        Assertions.assertEquals(Date.valueOf("2018-06-09"), range.getEnd());
    }

    /**
     * Verifies expense breakdown by currency in RUB.
     */
    @Test
    public void testGetIncomeByCurrency() {
        List<MoneyShareInCurrency> result = expenseRepository.getExpensesByCurrency(String.valueOf(MoneyTypeCode.RUB));
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
     * Verifies expense breakdown by currency in USD.
     */
    @Test
    public void testGetIncomeByCurrencyUsd() {
        List<MoneyShareInCurrency> result = expenseRepository.getExpensesByCurrency(String.valueOf(MoneyTypeCode.USD));
        Assertions.assertNotNull(result);
        Assertions.assertFalse(result.isEmpty());

        for (MoneyShareInCurrency row : result) {
            Assertions.assertNotNull(row.getCode());
            Assertions.assertNotNull(row.getTotal());
            Assertions.assertNotNull(row.getTotalConverted());
        }
    }

    /**
     * Verifies the total expense amount in RUB.
     */
    @Test
    public void testSumTotalInCurrency() {
        Double result = expenseRepository.sumTotalInCurrency(String.valueOf(MoneyTypeCode.RUB));
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result > 0);
    }

    /**
     * Verifies the total expense amount in USD.
     */
    @Test
    public void testSumTotalInCurrencyUsd() {
        Double result = expenseRepository.sumTotalInCurrency(String.valueOf(MoneyTypeCode.USD));
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result > 0);
    }

}
