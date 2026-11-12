package org.clematis.mt.repositories;

import org.clematis.mt.ClematisMoneyTrackerApplicationTests;
import org.clematis.mt.model.MonthlyDelta;
import org.clematis.mt.repository.MonthlyDeltaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;


/**
 * Repository tests for MonthlyDelta data access.
 */
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
    @Test
    void sortByYear() {
        Page<MonthlyDelta> page
                = monthlyDeltaRepository.findAll(PageRequest.of(0, 30,
                Sort.Direction.DESC, "key.an"));
        Assertions.assertEquals(27, page.getTotalElements());
        Assertions.assertEquals(2018, page.iterator().next().getAn());

        page = monthlyDeltaRepository.findAll(PageRequest.of(0, 30,
                Sort.Direction.ASC, "key.an"));
        Assertions.assertEquals(2017, page.iterator().next().getAn());
    }
}
