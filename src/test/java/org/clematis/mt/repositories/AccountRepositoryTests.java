package org.clematis.mt.repositories;

import org.clematis.mt.ClematisMoneyTrackerApplicationTests;
import org.clematis.mt.model.Account;
import org.clematis.mt.repository.AccountGroupRepository;
import org.clematis.mt.repository.AccountRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;


/**
 * Repository tests for Account and AccountGroup data access.
 */
public class AccountRepositoryTests extends ClematisMoneyTrackerApplicationTests {

    @Autowired
    protected AccountRepository accountRepository;

    @Autowired
    protected AccountGroupRepository accountGroupsRepository;

    /**
     * Verifies the total count of accounts in the database.
     */
    @Test
    void countAccounts() {
        Assertions.assertEquals(17, accountRepository.count());
    }

    /**
     * Verifies the total count of account groups in the database.
     */
    @Test
    void countAccountGroups() {
        Assertions.assertEquals(6, accountGroupsRepository.count());
    }

    /**
     * Tests finding accounts using example-based matching with case-insensitive contains.
     */
    @Test
    @Disabled
    void findAccounts() {
        Account account = new Account();
        account.setName(".");
        ExampleMatcher matcher = ExampleMatcher.matching()
            .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
            .withIgnoreCase();
        Example<Account> example = Example.of(account, matcher);
        Page<Account> page = accountRepository.findAll(
            example, PageRequest.of(0, 10)
        );
        Assertions.assertEquals(6, page.getTotalElements());
    }
}
