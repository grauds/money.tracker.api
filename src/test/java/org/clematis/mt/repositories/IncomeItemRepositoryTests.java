package org.clematis.mt.repositories;

import java.sql.Date;
import java.util.List;

import org.clematis.mt.ClematisMoneyTrackerApplicationTests;
import org.clematis.mt.dto.IncomeMonthlyReport;
import org.clematis.mt.model.IncomeItem;
import org.clematis.mt.model.IncomeMonthly;
import org.clematis.mt.model.MoneyTypeCode;
import org.clematis.mt.repository.IncomeItemRepository;
import org.clematis.mt.repository.IncomeMonthlyRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;


/**
 * Repository tests for IncomeItem data access.
 */
public class IncomeItemRepositoryTests extends ClematisMoneyTrackerApplicationTests {

    @Autowired
    private IncomeItemRepository incomeItemRepository;

    @Autowired
    private IncomeMonthlyRepository incomeMonthlyRepository;

    /**
     * Verifies the total count of income items.
     */
    @Test
    public void testIncome() {
        Iterable<IncomeItem> result
            = incomeItemRepository.findAll(Sort.by("transferdate").descending());
        Assertions.assertEquals(721, result.spliterator().estimateSize());
    }

    /**
     * Verifies sorting of income items by commodity ID and transfer date.
     */
    @Test
    public void testSorting() {
        Page<IncomeItem> result = incomeItemRepository
            .findByCommodityId(654, PageRequest.of(0, 400,
                Sort.by("transferdate").descending()));
        Assertions.assertEquals(576, result.getTotalElements());
    }

    /**
     * Verifies sorting of income items by commodity name.
     */
    @Test
    public void testNameSorting() {
        Page<IncomeItem> result = incomeItemRepository
            .findByCommodityId(654, PageRequest.of(0, 400,
                Sort.by("commodity.name").descending()));
        Assertions.assertEquals(576, result.getTotalElements());
    }

    /**
     * Verifies filtering income items by commodity ID.
     */
    @Test
    public void testCommodityIncome() {
        Page<IncomeItem> result = incomeItemRepository
            .findByCommodityId(654, PageRequest.of(0, 400));
        Assertions.assertEquals(576, result.getTotalElements());
    }

    /**
     * Verifies the daily income total is correctly calculated.
     */
    @Test
    public void testDayTotalSum() {
        Double result = incomeItemRepository.sumDailyIncome(
            Date.valueOf("2017-06-24"), String.valueOf(MoneyTypeCode.RUB)
        );
        Assertions.assertEquals(2100.0, result);
    }

    /**
     * Verifies the commodity income total is correctly calculated.
     */
    @Test
    public void testCommodityTotalSum() {
        Double result = incomeItemRepository.sumCommodityIncome(654, String.valueOf(MoneyTypeCode.RUB));
        Assertions.assertEquals(246436.4580530461, result);
    }

    /**
     * Verifies the commodity quantity total is correctly calculated.
     */
    @Test
    public void testCommodityTotalQty() {
        Long result = incomeItemRepository.sumCommodityQuantity(654);
        Assertions.assertEquals(576, result);
    }

    /**
     * Verifies the commodity group income total is correctly calculated.
     */
    @Test
    public void testCommodityGroupTotalSum() {
        Long result = incomeItemRepository.sumCommodityGroupIncome(278,
            String.valueOf(MoneyTypeCode.RUB));
        Assertions.assertEquals(246436, result);
    }

    /**
     * Verifies the organization group income total is correctly calculated.
     */
    @Test
    public void testOrganizationGroupTotalSum() {
        Long result = incomeItemRepository.sumOrganizationGroupIncome(268,
            String.valueOf(MoneyTypeCode.RUB));
        Assertions.assertEquals(769069, result);
    }

    /**
     * Verifies filtering income items by account ID and summing income with currency conversion.
     */
    @Test
    public void testAccountIncome() {
        Page<IncomeItem> result = incomeItemRepository
            .findByIncomeAccountId(257, PageRequest.of(0, 800));
        Assertions.assertEquals(251, result.getTotalElements());
        Assertions.assertEquals(1, result.getTotalPages());

        double sum = incomeItemRepository.sumAccountIncome(
            257,
            String.valueOf(MoneyTypeCode.RUB)
        );

        Assertions.assertEquals(
            Math.round(
                result.get()
                .filter(p -> p.getIncome().getMoneyType().getCode().equals("RUB"))
                .mapToDouble(IncomeItem::getTotal).sum()
            ),
            Math.round(sum)
        );
    }

    /**
     * Verifies the account income total is correctly calculated.
     */
    @Test
    public void testAccountTotalSum() {
        Double result = incomeItemRepository.sumAccountIncome(257, String.valueOf(MoneyTypeCode.RUB));
        Assertions.assertEquals(398795.0, result);
    }

    /**
     * Verifies the account group income total is correctly calculated.
     */
    @Test
    public void testAccountGroupTotalSum() {
        Long result = incomeItemRepository.sumAccountGroupIncome(257,
            String.valueOf(MoneyTypeCode.RUB));
        Assertions.assertEquals(433107, result);
    }

    /**
     * Verifies filtering income items by tradeplace (organization) ID and summing income.
     */
    @Test
    public void testOrganizationIncome() {
        Page<IncomeItem> result = incomeItemRepository
            .findByTradeplaceId(289, PageRequest.of(0, 800));
        Assertions.assertEquals(644, result.getTotalElements());
        Assertions.assertEquals(1, result.getTotalPages());

        double sum = incomeItemRepository.sumOrganizationIncome(289,
            String.valueOf(MoneyTypeCode.RUB));

        Assertions.assertNotEquals(
            Math.round(result.get()
            .filter(p -> p.getIncome().getMoneyType().getCode().equals("RUB"))
            .mapToDouble(IncomeItem::getTotal).sum()),
            Math.round(sum)
        );
    }

    /**
     * Verifies the organization income total is correctly calculated.
     */
    @Test
    public void testOrganizationTotalSum() {
        Double result = incomeItemRepository.sumOrganizationIncome(289,
            String.valueOf(MoneyTypeCode.RUB));
        Assertions.assertEquals(271886.4580530461, result);
    }

    /**
     * Verifies the monthly income reports are correctly generated.
     */
    @Test
    public void testIncomeReports() {
        Page<IncomeMonthlyReport> reports
            = incomeItemRepository.getIncomeItemReports("EUR", Pageable.ofSize(400));

        Assertions.assertEquals(61, reports.getTotalElements());
        Assertions.assertEquals(1, reports.getTotalPages());

        Assertions.assertEquals("Возврат", reports.getContent().get(0).getCommodity());
        Assertions.assertEquals("2017", reports.getContent().get(0).getAn());
        Assertions.assertEquals("6", reports.getContent().get(0).getMois());
        Assertions.assertEquals(9.19, reports.getContent().get(0).getTotal());
    }

    /**
     * Verifies the income reports from IncomeMonthlyRepository.
     */
    @Test
    public void testIncomeReportsTwo() {
        List<IncomeMonthly> reports
                = incomeMonthlyRepository.getIncomeItemReports("USD", 1, 2010, 1, 2022);

        Assertions.assertEquals(71, reports.size());

        Assertions.assertEquals("CNY", reports.get(0).getCode());
        Assertions.assertEquals(2017, reports.get(0).getAn());
        Assertions.assertEquals(6, reports.get(0).getMois());
        Assertions.assertEquals(14.06, reports.get(0).getTotalConverted());
        Assertions.assertEquals(100, reports.get(0).getTotal());
    }
}
