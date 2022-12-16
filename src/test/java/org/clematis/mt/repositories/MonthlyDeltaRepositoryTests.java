package org.clematis.mt.repositories;

import org.clematis.mt.ClematisMoneyTrackerApplicationTests;
import org.clematis.mt.model.views.MonthlyDelta;
import org.clematis.mt.repository.MonthlyDeltaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

/**
 * @author Anton Troshin
 */
public class MonthlyDeltaRepositoryTests extends ClematisMoneyTrackerApplicationTests {

    @Autowired
    private MonthlyDeltaRepository monthlyDeltaRepository;

    @Test
    void countDeltas() {
        Assertions.assertEquals(27, monthlyDeltaRepository.count());
    }

    @Test
    void countBalances() {
        Assertions.assertEquals(484031,
                monthlyDeltaRepository.getBalanceForCurrency(2018, 2, "RUB"));
    }

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
