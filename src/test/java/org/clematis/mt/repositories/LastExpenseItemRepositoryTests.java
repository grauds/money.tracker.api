package org.clematis.mt.repositories;

import java.util.List;

import org.clematis.mt.ClematisMoneyTrackerApplicationTests;
import org.clematis.mt.model.LastExpenseEntry;
import org.clematis.mt.model.LastExpenseItem;
import org.clematis.mt.repository.LastExpenseItemRepository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class LastExpenseItemRepositoryTests extends ClematisMoneyTrackerApplicationTests {

    @Autowired
    private LastExpenseItemRepository lastExpenseItemRepository;

    @Test
    public void testLastCommodityExpenses() {
        List<LastExpenseEntry> result = lastExpenseItemRepository.findByCommodityId(258);
        Assertions.assertEquals(1, result.size());
    }

    @Test
    public void testSearchByName() {
        Page<LastExpenseItem> result = lastExpenseItemRepository
                .findByCommodity_NameStartingWithIgnoreCase("Н", Pageable.ofSize(10));
        Assertions.assertEquals(16, result.getTotalElements());
    }
}
