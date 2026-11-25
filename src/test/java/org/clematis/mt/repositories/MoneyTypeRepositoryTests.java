package org.clematis.mt.repositories;

import org.clematis.mt.ClematisMoneyTrackerApplicationTests;
import org.clematis.mt.model.MoneyType;
import org.clematis.mt.repository.MoneyTypeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * Repository tests for MoneyType data access.
 */
@SuppressWarnings({"checkstyle:MagicNumber", "checkstyle:MultipleStringLiterals"})
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
    @SuppressWarnings("checkstyle:MultipleStringLiterals")
    @Test
    public void testFindByCode() {
        Assertions.assertEquals("RUB", this.repository.findMoneyTypeByCode("RUB").getCode());
        Assertions.assertEquals("Рубль", this.repository.findMoneyTypeByCode("RUB").getName());
    }

    /**
     * Verifies finding a money type by USD code.
     */
    @Test
    public void testFindByCodeUsd() {
        MoneyType usd = repository.findMoneyTypeByCode("USD");
        Assertions.assertNotNull(usd);
        Assertions.assertEquals("USD", usd.getCode());
        Assertions.assertNotNull(usd.getName());
        Assertions.assertNotNull(usd.getSign());
    }

    /**
     * Verifies finding a money type by EUR code.
     */
    @Test
    public void testFindByCodeEur() {
        MoneyType eur = repository.findMoneyTypeByCode("EUR");
        Assertions.assertNotNull(eur);
        Assertions.assertEquals("EUR", eur.getCode());
        Assertions.assertNotNull(eur.getName());
        Assertions.assertNotNull(eur.getSign());
    }

    /**
     * Verifies finding a money type by non-existent code returns null.
     */
    @Test
    public void testFindByCodeNonExistent() {
        MoneyType result = repository.findMoneyTypeByCode("XXX");
        Assertions.assertNull(result);
    }

    /**
     * Verifies finding a money type by empty code returns null.
     */
    @Test
    public void testFindByCodeEmpty() {
        MoneyType result = repository.findMoneyTypeByCode("");
        Assertions.assertNull(result);
    }

}
