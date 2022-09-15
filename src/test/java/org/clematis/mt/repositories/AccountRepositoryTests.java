package org.clematis.mt.repositories;

import org.clematis.mt.ClematisMoneyTrackerApplicationTests;
import org.clematis.mt.model.Account;
import org.clematis.mt.repository.AccountGroupRepository;
import org.clematis.mt.repository.AccountRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

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

    @Test
    public void findAccounts() {
        Page<Account> accountList
                = accountRepository.findByNameContainingIgnoreCase(".", PageRequest.of(0, 10));
        Assertions.assertEquals(6, accountList.getTotalElements());
    }
}
