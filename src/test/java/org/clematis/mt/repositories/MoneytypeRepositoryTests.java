package org.clematis.mt.repositories;

import org.clematis.mt.ClematisMoneyTrackerApplicationTests;
import org.clematis.mt.repository.MoneyTypeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class MoneytypeRepositoryTests extends ClematisMoneyTrackerApplicationTests {

    @Autowired
    private MoneyTypeRepository repository;

    @Test
    public void getAvailableMoneyTypes() {
        Assertions.assertEquals(5, repository.findAll().size());
    }
}
