package org.clematis.mt.repositories;

import org.clematis.mt.ClematisMoneyTrackerApplicationTests;
import org.clematis.mt.model.MoneyTypeCode;
import org.clematis.mt.repository.IncomeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class IncomeRepositoryTests extends ClematisMoneyTrackerApplicationTests {

    @Autowired
    private IncomeRepository incomeRepository;

    @Test
    public void testCommodityGroupTotalSum() {
        Long result = incomeRepository.sumCommodityGroupIncome(278,
            String.valueOf(MoneyTypeCode.RUB));
        Assertions.assertEquals(234088, result);
    }
}
