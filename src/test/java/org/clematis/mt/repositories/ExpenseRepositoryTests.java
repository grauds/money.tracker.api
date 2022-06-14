package org.clematis.mt.repositories;


import java.util.List;

import org.clematis.mt.ClematisMoneyTrackerApplicationTests;
import org.clematis.mt.model.Expense;
import org.clematis.mt.model.MoneyTypeCode;
import org.clematis.mt.repository.ExpenseRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
public class ExpenseRepositoryTests extends ClematisMoneyTrackerApplicationTests {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Test
    public void countTotalsForCommodity() {
        List<Expense> result = expenseRepository
                .findByIdAndMoneyTypeCode(258, String.valueOf(MoneyTypeCode.RUB));
    }

}
