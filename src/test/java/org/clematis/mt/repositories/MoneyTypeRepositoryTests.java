package org.clematis.mt.repositories;

import org.clematis.mt.ClematisMoneyTrackerApplicationTests;
import org.clematis.mt.repository.MoneyTypeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * Repository tests for MoneyType data access.
 */
public class MoneyTypeRepositoryTests extends ClematisMoneyTrackerApplicationTests {

    @Autowired
    private MoneyTypeRepository repository;

    /**
     * Verifies the count of available money types.
     */
    @Test
    public void testAvailableMoneyTypes() {
        Assertions.assertEquals(5, repository.findAll().size());
    }

    /**
     * Verifies finding a money type by its code.
     */
    @Test
    public void testFindByCode() {
        Assertions.assertEquals("RUB", this.repository.findMoneyTypeByCode("RUB").getCode());
        Assertions.assertEquals("Рубль", this.repository.findMoneyTypeByCode("RUB").getName());
    }
}
