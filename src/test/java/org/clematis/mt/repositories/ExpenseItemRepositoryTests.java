package org.clematis.mt.repositories;


import java.util.List;
import org.clematis.mt.ClematisMoneyTrackerApplicationTests;
import org.clematis.mt.model.ExpenseItem;
import org.clematis.mt.model.MoneyTypeCode;
import org.clematis.mt.repository.ExpenseItemRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;

public class ExpenseItemRepositoryTests extends ClematisMoneyTrackerApplicationTests {

    @Autowired
    private ExpenseItemRepository expenseItemRepository;

    @Test
    public void testExpenses() {
        Iterable<ExpenseItem> result
                = expenseItemRepository.findAll(Sort.by("transferdate").ascending());
        Assertions.assertEquals(2291, result.spliterator().estimateSize());
    }

    @Test
    public void testCommodityExpenses() {
        List<ExpenseItem> result = expenseItemRepository.findByCommodityId(258);
        Assertions.assertEquals(323, result.size());
    }

    @Test
    public void testCommodityTotalSum() {
        Long result = expenseItemRepository.sumCommodityExpenses(258, String.valueOf(MoneyTypeCode.RUB));
        Assertions.assertEquals(6026, result);
    }
}
