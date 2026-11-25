package org.clematis.mt.repositories;

import java.sql.Date;
import java.util.List;

import org.clematis.mt.ClematisMoneyTrackerApplicationTests;
import org.clematis.mt.dto.DateRangeDTO;
import org.clematis.mt.model.Expense;
import org.clematis.mt.model.MoneyTypeCode;
import org.clematis.mt.projections.AgentCommodityGroupProjection;
import org.clematis.mt.projections.EntityAmountProjection;
import org.clematis.mt.projections.MoneyShareInCurrencyProjection;
import org.clematis.mt.repository.ExpenseRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * Repository tests for Expense data access.
 */
@SuppressWarnings({"checkstyle:MagicNumber", "checkstyle:MultipleStringLiterals"})
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
    @SuppressWarnings("checkstyle:MultipleStringLiterals")
    @Test
    public void testAgentCommodityTotalSum() {
        List<AgentCommodityGroupProjection> groups = expenseRepository.getAgentCommodityGroups("RUB",
            3, 2018, 3, 2019);
        Assertions.assertEquals(142, groups.size());
    }

    /**
     * Verifies handling of invalid date ranges for agent/commodity group expenses.
     */
    @Test
    public void testAgentCommodityTotalSumInvalidDates() {
        List<AgentCommodityGroupProjection> groups = expenseRepository.getAgentCommodityGroups("RUB",
            3, 2026, 3, 2030);
        Assertions.assertEquals(0, groups.size());
    }

    /**
     * Verifies the date range retrieval for expenses.
     */
    @Test
    public void testDates() {
        DateRangeDTO range = expenseRepository.getDatesRange();
        Assertions.assertEquals(Date.valueOf("2017-06-11"), range.getStart());
        Assertions.assertEquals(Date.valueOf("2018-06-09"), range.getEnd());
    }

    /**
     * Verifies expense breakdown by currency in RUB.
     */
    @Test
    public void testGetIncomeByCurrency() {
        List<MoneyShareInCurrencyProjection> result
            = expenseRepository.getExpensesByCurrency(String.valueOf(MoneyTypeCode.RUB));
        Assertions.assertNotNull(result);
        Assertions.assertFalse(result.isEmpty());

        for (MoneyShareInCurrencyProjection row : result) {
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
        List<MoneyShareInCurrencyProjection> result
            = expenseRepository.getExpensesByCurrency(String.valueOf(MoneyTypeCode.USD));
        Assertions.assertNotNull(result);
        Assertions.assertFalse(result.isEmpty());

        for (MoneyShareInCurrencyProjection row : result) {
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
        Assertions.assertEquals(1060524.75, result);
    }

    /**
     * Verifies the total expense amount in USD.
     */
    @Test
    public void testSumTotalInCurrencyUsd() {
        Double result = expenseRepository.sumTotalInCurrency(String.valueOf(MoneyTypeCode.USD));
        Assertions.assertEquals(32021.39, result);
    }

    /**
     * Verifies expense breakdown by categories in RUB returns 34 items.
     */
    @Test
    public void testGetExpenseByCategories() {
        List<EntityAmountProjection> result = expenseRepository.getExpenseByCategories("RUB");
        Assertions.assertEquals(34, result.size());

        Assertions.assertEquals("Квартира", result.get(0).getName());
        Assertions.assertEquals(872646.0, result.get(0).getTotal(), 0.01);
        Assertions.assertEquals(14, result.get(0).getTxCount());

        Assertions.assertEquals("Разное", result.get(1).getName());
        Assertions.assertEquals(34797.36, result.get(1).getTotal(), 0.01);
        Assertions.assertEquals(105, result.get(1).getTxCount());
    }

    /**
     * Verifies expense breakdown by categories in USD returns 34 items.
     */
    @Test
    public void testGetExpenseByCategoriesUsd() {
        List<EntityAmountProjection> result = expenseRepository.getExpenseByCategories("USD");
        Assertions.assertEquals(34, result.size());

        Assertions.assertEquals("Квартира", result.get(0).getName());
        Assertions.assertEquals(25794.17, result.get(0).getTotal(), 0.01);
        Assertions.assertEquals(14, result.get(0).getTxCount());
    }

    /**
     * Verifies expense breakdown by trade place in RUB returns 56 items.
     */
    @SuppressWarnings("checkstyle:MultipleStringLiterals")
    @Test
    public void testGetExpenseByTradePlace() {
        List<EntityAmountProjection> result = expenseRepository.getExpenseByTradePlace("RUB");
        Assertions.assertEquals(56, result.size());

        Assertions.assertEquals("Обычное", result.get(0).getName());
        Assertions.assertEquals(930058.4, result.get(0).getTotal(), 0.01);
        Assertions.assertEquals(409, result.get(0).getTxCount());

        Assertions.assertEquals("ДНС", result.get(1).getName());
        Assertions.assertEquals(19385.0, result.get(1).getTotal(), 0.01);
        Assertions.assertEquals(12, result.get(1).getTxCount());
    }

    /**
     * Verifies expense breakdown by trade place in USD returns 56 items.
     */
    @SuppressWarnings("checkstyle:MultipleStringLiterals")
    @Test
    public void testGetExpenseByTradePlaceUsd() {
        List<EntityAmountProjection> result = expenseRepository.getExpenseByTradePlace("USD");
        Assertions.assertEquals(56, result.size());

        Assertions.assertEquals("Обычное", result.get(0).getName());
        Assertions.assertEquals(27653.24, result.get(0).getTotal(), 0.01);
        Assertions.assertEquals(409, result.get(0).getTxCount());
    }

    /**
     * Verifies expense breakdown by categories for non-existent currency.
     */
    @SuppressWarnings("checkstyle:MultipleStringLiterals")
    @Test
    public void testGetExpenseByCategoriesNonExistentCurrency() {
        List<EntityAmountProjection> result = expenseRepository.getExpenseByCategories("XXX");
        Assertions.assertNotNull(result);
        Assertions.assertEquals(34, result.size());
    }

    /**
     * Verifies expense breakdown by trade place for non-existent currency.
     */
    @SuppressWarnings("checkstyle:MultipleStringLiterals")
    @Test
    public void testGetExpenseByTradePlaceNonExistentCurrency() {
        List<EntityAmountProjection> result = expenseRepository.getExpenseByTradePlace("XXX");
        Assertions.assertNotNull(result);
        Assertions.assertEquals(56, result.size());
    }

}
