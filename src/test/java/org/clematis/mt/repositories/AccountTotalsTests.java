package org.clematis.mt.repositories;

import org.clematis.mt.ClematisMoneyTrackerApplicationTests;
import org.clematis.mt.repository.AccountTotalRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Anton Troshin
 */
public class AccountTotalsTests extends ClematisMoneyTrackerApplicationTests {

    @Autowired
    private AccountTotalRepository accountTotalRepository;

    @Test
    public void countAccounts() {
        Assertions.assertEquals(17, accountTotalRepository.count());
    }
}
