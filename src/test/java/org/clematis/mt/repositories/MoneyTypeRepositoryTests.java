package org.clematis.mt.repositories;

import org.clematis.mt.ClematisMoneyTrackerApplicationTests;
import org.clematis.mt.repository.MoneyTypeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class MoneyTypeRepositoryTests extends ClematisMoneyTrackerApplicationTests {

    @Autowired
    private MoneyTypeRepository repository;

    @Test
    public void testAvailableMoneyTypes() {
        Assertions.assertEquals(5, repository.findAll().size());
    }

    @Test
    public void testFindByCode() {
        Assertions.assertEquals("RUB", this.repository.findMoneyTypeByCode("RUB").getCode());
        Assertions.assertEquals("Рубль", this.repository.findMoneyTypeByCode("RUB").getName());
    }
}
