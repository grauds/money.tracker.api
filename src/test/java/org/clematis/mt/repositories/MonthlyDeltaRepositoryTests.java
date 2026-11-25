package org.clematis.mt.repositories;

import java.math.BigDecimal;
import java.util.List;

import org.clematis.mt.ClematisMoneyTrackerApplicationTests;
import org.clematis.mt.model.MonthlyDelta;
import org.clematis.mt.projections.AccountSummaryProjection;
import org.clematis.mt.repository.MonthlyDeltaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;


/**
 * Repository tests for MonthlyDelta data access.
 */
@SuppressWarnings("checkstyle:MagicNumber")
public class MonthlyDeltaRepositoryTests extends ClematisMoneyTrackerApplicationTests {

    @Autowired
    private MonthlyDeltaRepository monthlyDeltaRepository;

    /**
     * Verifies the total count of monthly delta records.
     */
    @Test
    void countDeltas() {
        Assertions.assertEquals(27, monthlyDeltaRepository.count());
    }

    /**
     * Verifies the balance calculation for a specific month and year.
     */
    @SuppressWarnings("checkstyle:MultipleStringLiterals")
    @Test
    void countBalances() {
        Assertions.assertEquals(474100,
                monthlyDeltaRepository.getBalanceForCurrency(2018, 2, "RUB"));
    }

    /**
     * Verifies the balance calculation for an edge case month.
     */
    @Test
    void countEdgeCaseBalances() {
        Assertions.assertEquals(0,
                monthlyDeltaRepository.getBalanceForCurrency(2017, 6, "RUB"));
    }

    /**
     * Verifies sorting of monthly deltas by year in descending and ascending order.
     */
    @SuppressWarnings("checkstyle:MultipleStringLiterals")
    @Test
    void sortByYear() {
        Page<MonthlyDelta> page
                = monthlyDeltaRepository.findAll(PageRequest.of(0, 30,
                org.springframework.data.domain.Sort.Direction.DESC, "key.an"));
        Assertions.assertEquals(27, page.getTotalElements());
        Assertions.assertEquals(2018, page.iterator().next().getAn());

        page = monthlyDeltaRepository.findAll(PageRequest.of(0, 30,
                org.springframework.data.domain.Sort.Direction.ASC, "key.an"));
        Assertions.assertEquals(2017, page.iterator().next().getAn());
    }

    /**
     * Verifies finding monthly deltas by currency code (RUB).
     */
    @Test
    void testFindMonthlyDeltasByKeyCode() {
        Page<MonthlyDelta> result = monthlyDeltaRepository
                .findMonthlyDeltasByKey_Code("RUB", PageRequest.of(0, 100));
        Assertions.assertEquals(13, result.getTotalElements());
    }

    /**
     * Verifies finding monthly deltas by currency code (USD).
     */
    @SuppressWarnings("checkstyle:MultipleStringLiterals")
    @Test
    void testFindMonthlyDeltasByKeyCodeUsd() {
        Page<MonthlyDelta> result = monthlyDeltaRepository
                .findMonthlyDeltasByKey_Code("USD", PageRequest.of(0, 100));
        Assertions.assertTrue(result.getTotalElements() > 0);
    }

    /**
     * Verifies finding monthly deltas by currency code with pagination.
     */
    @Test
    void testFindMonthlyDeltasByKeyCodePagination() {
        Page<MonthlyDelta> page = monthlyDeltaRepository
                .findMonthlyDeltasByKey_Code("RUB", PageRequest.of(0, 5));
        Assertions.assertEquals(5, page.getContent().size());
        Assertions.assertTrue(page.getTotalElements() > 5);
        Assertions.assertTrue(page.hasNext());
    }

    /**
     * Verifies the summary in currency for a full date range returns 269 records.
     */
    @SuppressWarnings("checkstyle:MultipleStringLiterals")
    @Test
    void testGetSummaryInCurrency() {
        List<AccountSummaryProjection> result = monthlyDeltaRepository.getSummaryInCurrency(
                "RUB", 1, 2017, 12, 2018);
        Assertions.assertEquals(269, result.size());

        // First record: 2017-06, account 276
        AccountSummaryProjection first = result.get(0);
        Assertions.assertEquals(276, first.getAccountId());
        Assertions.assertEquals("Карт. Сбербанк Иван", first.getAccountName());
        Assertions.assertEquals(2017, first.getYear());
        Assertions.assertEquals(6, first.getMonth());
        Assertions.assertEquals("RUB", first.getCode());
        Assertions.assertEquals(new BigDecimal("10288.81"), first.getDeltaOrig());
        Assertions.assertEquals(new BigDecimal("10288.81"), first.getDeltaTarget());
        Assertions.assertEquals(new BigDecimal("10288.81"), first.getBalanceOrig());
        Assertions.assertEquals(new BigDecimal("10288.81"), first.getBalanceTarget());
    }

    /**
     * Verifies the summary in currency for a single month.
     */
    @Test
    void testGetSummaryInCurrencySingleMonth() {
        List<AccountSummaryProjection> result = monthlyDeltaRepository.getSummaryInCurrency(
                "RUB", 2, 2018, 2, 2018);
        Assertions.assertEquals(13, result.size());

        for (AccountSummaryProjection projection : result) {
            Assertions.assertEquals(2018, projection.getYear());
            Assertions.assertEquals(2, projection.getMonth());
            Assertions.assertNotNull(projection.getDeltaTarget());
            Assertions.assertNotNull(projection.getMonthTotalDeltaTarget());
        }
    }

    /**
     * Verifies the summary in currency for non-existent currency.
     */
    @SuppressWarnings("checkstyle:MultipleStringLiterals")
    @Test
    void testGetSummaryInCurrencyNonExistentCurrency() {
        List<AccountSummaryProjection> result = monthlyDeltaRepository.getSummaryInCurrency(
                "XXX", 1, 2017, 12, 2018);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(269, result.size());
    }

    /**
     * Verifies the summary in currency for USD.
     */
    @Test
    void testGetSummaryInCurrencyUsd() {
        List<AccountSummaryProjection> result = monthlyDeltaRepository.getSummaryInCurrency(
                "USD", 1, 2017, 12, 2018);
        Assertions.assertNotNull(result);

        for (AccountSummaryProjection projection : result) {
            Assertions.assertNotNull(projection.getAccountId());
            Assertions.assertNotNull(projection.getAccountName());
            Assertions.assertNotNull(projection.getDeltaTarget());
        }
    }

}
