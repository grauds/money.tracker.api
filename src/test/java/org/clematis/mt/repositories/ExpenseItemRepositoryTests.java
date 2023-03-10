package org.clematis.mt.repositories;


import org.clematis.mt.ClematisMoneyTrackerApplicationTests;
import org.clematis.mt.model.ExpenseItem;
import org.clematis.mt.model.MoneyTypeCode;
import org.clematis.mt.repository.ExpenseItemRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

public class ExpenseItemRepositoryTests extends ClematisMoneyTrackerApplicationTests {

    @Autowired
    private ExpenseItemRepository expenseItemRepository;

    @Test
    public void testExpenses() {
        Iterable<ExpenseItem> result
                = expenseItemRepository.findAll(Sort.by("transferdate").descending());
        Assertions.assertEquals(2291, result.spliterator().estimateSize());
    }

    @Test
    public void testSorting() {
        Page<ExpenseItem> result = expenseItemRepository
                .findByCommodityId(258, PageRequest.of(0, 400,
                        Sort.by("transferdate").descending()));
        Assertions.assertEquals(323, result.getTotalElements());
    }

    @Test
    public void testCommodityExpenses() {
        Page<ExpenseItem> result = expenseItemRepository
                .findByCommodityId(258, PageRequest.of(0, 400));
        Assertions.assertEquals(323, result.getTotalElements());
    }

    @Test
    public void testCommodityTotalSum() {
        Double result = expenseItemRepository.sumCommodityExpenses(258, String.valueOf(MoneyTypeCode.RUB));
        Assertions.assertEquals(6026, result);
    }

    @Test
    public void testCommodityTotalQty() {
        Long result = expenseItemRepository.sumCommodityQuantity(258);
        Assertions.assertEquals(729, result);
    }

    @Test
    public void testOrganizationExpenses() {
        Page<ExpenseItem> result = expenseItemRepository
                .findByTradeplaceId(316, PageRequest.of(0, 400));
        Assertions.assertEquals(11, result.getTotalElements());
        Assertions.assertEquals(1, result.getTotalPages());

        double sum = expenseItemRepository.sumOrganizationExpenses(316,
                String.valueOf(MoneyTypeCode.RUB));

        Assertions.assertEquals(result.get().mapToDouble(ExpenseItem::getTotal).sum(), sum);
    }

    @Test
    public void testOrganizationTotalSum() {
        Double result = expenseItemRepository.sumOrganizationExpenses(316,
                String.valueOf(MoneyTypeCode.RUB));
        Assertions.assertEquals(661, result);
    }
}
