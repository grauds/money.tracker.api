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


/**
 * Repository tests for LastExpenseItem data access.
 */
@SuppressWarnings({"checkstyle:MagicNumber", "checkstyle:MultipleStringLiterals"})
public class LastExpenseItemRepositoryTests extends ClematisMoneyTrackerApplicationTests {

    @Autowired
    private LastExpenseItemRepository lastExpenseItemRepository;

    /**
     * Verifies retrieving the last expense for a specific commodity.
     */
    @Test
    public void testLastCommodityExpenses() {
        List<LastExpenseEntry> result = lastExpenseItemRepository.findByCommodityId(258);
        Assertions.assertEquals(1, result.size());
    }

    /**
     * Verifies searching expense items by commodity name prefix.
     */
    @Test
    public void testSearchByName() {
        Page<LastExpenseItem> result = lastExpenseItemRepository
                .findByCommodity_NameStartingWithIgnoreCase("Н", Pageable.ofSize(10));
        Assertions.assertEquals(16, result.getTotalElements());
    }

    /**
     * Verifies searching expense items by commodity name containing a substring.
     */
    @Test
    public void testSearchByNameContaining() {
        Page<LastExpenseItem> result = lastExpenseItemRepository
                .findByCommodity_NameContainingIgnoreCase("a", Pageable.ofSize(10));
        Assertions.assertEquals(57, result.getTotalElements());
    }

    /**
     * Verifies searching expense items by commodity name ending with a substring.
     */
    @Test
    public void testSearchByNameEnding() {
        Page<LastExpenseItem> result = lastExpenseItemRepository
                .findByCommodity_NameEndingWithIgnoreCase("a", Pageable.ofSize(10));
        Assertions.assertEquals(3, result.getTotalElements());
    }

    /**
     * Verifies searching expense items by commodity name containing returns empty for no match.
     */
    @Test
    public void testSearchByNameContainingNoMatch() {
        Page<LastExpenseItem> result = lastExpenseItemRepository
                .findByCommodity_NameContainingIgnoreCase("zzzzzzzzzzzz", Pageable.ofSize(10));
        Assertions.assertEquals(0, result.getTotalElements());
    }

    /**
     * Verifies searching expense items by commodity name ending returns empty for no match.
     */
    @Test
    public void testSearchByNameEndingNoMatch() {
        Page<LastExpenseItem> result = lastExpenseItemRepository
                .findByCommodity_NameEndingWithIgnoreCase("zzzzzzzzzzzz", Pageable.ofSize(10));
        Assertions.assertEquals(0, result.getTotalElements());
    }

    /**
     * Verifies searching expense items by commodity name starting with empty string.
     */
    @Test
    public void testSearchByNameStartingEmpty() {
        Page<LastExpenseItem> result = lastExpenseItemRepository
                .findByCommodity_NameStartingWithIgnoreCase("", Pageable.ofSize(10));
        Assertions.assertEquals(697, result.getTotalElements());
    }

}
