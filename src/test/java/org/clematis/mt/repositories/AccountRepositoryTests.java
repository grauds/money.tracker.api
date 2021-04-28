package org.clematis.mt.repositories;

import org.clematis.mt.ClematisMoneyTrackerApplicationTests;
import org.clematis.mt.repository.AccountGroupRepository;
import org.clematis.mt.repository.AccountRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class AccountRepositoryTests extends ClematisMoneyTrackerApplicationTests {

    @Autowired
    protected AccountRepository accountRepository;

    @Autowired
    protected AccountGroupRepository accountGroupsRepository;

    @Test
    void countAccounts() {
        Assertions.assertEquals(17, accountRepository.count());
    }

    @Test
    void countAccountGroups() {
        Assertions.assertEquals(6, accountGroupsRepository.count());
    }
}
