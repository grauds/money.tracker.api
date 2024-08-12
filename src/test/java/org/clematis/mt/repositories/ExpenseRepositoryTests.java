package org.clematis.mt.repositories;

import java.util.List;

import org.clematis.mt.ClematisMoneyTrackerApplicationTests;
import org.clematis.mt.dto.AgentCommodityGroup;
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
    public void testCommodityGroupTotalSum() {
        Long result = expenseRepository.sumCommodityGroupExpenses(287,
            String.valueOf(MoneyTypeCode.RUB));
        Assertions.assertEquals(16162, result);
    }

    @Test
    public void testAgentCommodityTotalSum() {
        List<AgentCommodityGroup> groups = expenseRepository.getAgentCommodityGroups("RUB",
            3, 2018, 3, 2019);
        Assertions.assertEquals(8, groups.size());
    }

}
