package org.clematis.mt.repositories;

import java.util.List;

import org.clematis.mt.ClematisMoneyTrackerApplicationTests;
import org.clematis.mt.model.views.LastExpenseItem;
import org.clematis.mt.repository.LastExpenseItemRepository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class LastExpenseItemRepositoryTests extends ClematisMoneyTrackerApplicationTests {

    @Autowired
    private LastExpenseItemRepository lastExpenseItemRepository;

    @Test
    public void testLastCommodityExpenses() {
        List<LastExpenseItem> result = lastExpenseItemRepository.findByCommId(258);
        Assertions.assertEquals(1, result.size());
    }
}