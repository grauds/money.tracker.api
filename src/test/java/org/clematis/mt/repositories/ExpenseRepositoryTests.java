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

public class ExpenseRepositoryTests extends ClematisMoneyTrackerApplicationTests {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Test
    public void testGetAll() {
        Iterable<Expense> result = expenseRepository.findAll();
        Assertions.assertEquals(681, result.spliterator().estimateSize());
    }

    @Test
    public void testAgentCommodityTotalSum() {
        List<AgentCommodityGroup> groups = expenseRepository.getAgentCommodityGroups("RUB",
            3, 2018, 3, 2019);
        Assertions.assertEquals(142, groups.size());
    }

    @Test
    public void testAgentCommodityTotalSumInvalidDates() {
        List<AgentCommodityGroup> groups = expenseRepository.getAgentCommodityGroups("RUB",
            3, 2026, 3, 2030);
        Assertions.assertEquals(0, groups.size());
    }

    @Test
    public void testDates() {
        DateRange range = expenseRepository.getDatesRange();
        Assertions.assertEquals(Date.valueOf("2017-06-11"), range.getStart());
        Assertions.assertEquals(Date.valueOf("2018-06-09"), range.getEnd());
    }

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

    @Test
    public void testSumTotalInCurrency() {
        Double result = expenseRepository.sumTotalInCurrency(String.valueOf(MoneyTypeCode.RUB));
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result > 0);
    }

    @Test
    public void testSumTotalInCurrencyUsd() {
        Double result = expenseRepository.sumTotalInCurrency(String.valueOf(MoneyTypeCode.USD));
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result > 0);
    }

}
