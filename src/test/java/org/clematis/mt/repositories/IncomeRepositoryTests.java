package org.clematis.mt.repositories;

import java.util.List;

import org.clematis.mt.ClematisMoneyTrackerApplicationTests;
import org.clematis.mt.model.MoneyTypeCode;
import org.clematis.mt.projections.EntityAmountProjection;
import org.clematis.mt.projections.MoneyShareInCurrencyProjection;
import org.clematis.mt.repository.IncomeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * Repository tests for Income data access.
 */
@SuppressWarnings({"checkstyle:MagicNumber", "checkstyle:MultipleStringLiterals"})
public class IncomeRepositoryTests extends ClematisMoneyTrackerApplicationTests {

    @Autowired
    private IncomeRepository incomeRepository;

    /**
     * Verifies the total income amount in RUB.
     */
    @Test
    public void testSumTotalInCurrency() {
        Double result = incomeRepository.sumTotalInCurrency(String.valueOf(MoneyTypeCode.RUB));
        Assertions.assertEquals(830193.63, result);
    }

    /**
     * Verifies the total income amount in USD.
     */
    @Test
    public void testSumTotalInCurrencyUsd() {
        Double result = incomeRepository.sumTotalInCurrency(String.valueOf(MoneyTypeCode.USD));
        Assertions.assertEquals(27163.34, result);
    }

    /**
     * Verifies income breakdown by currency in RUB.
     */
    @Test
    public void testGetIncomeByCurrency() {
        List<MoneyShareInCurrencyProjection> result
            = incomeRepository.getIncomeByCurrency(String.valueOf(MoneyTypeCode.RUB));
        Assertions.assertNotNull(result);
        Assertions.assertFalse(result.isEmpty());

        for (MoneyShareInCurrencyProjection row : result) {
            Assertions.assertNotNull(row.getCode());
            Assertions.assertNotNull(row.getTotal());
            Assertions.assertNotNull(row.getTotalConverted());
            Assertions.assertTrue(row.getTotal() >= 0);
            Assertions.assertTrue(row.getTotalConverted() > 0);
        }
    }

    /**
     * Verifies income breakdown by currency in USD.
     */
    @Test
    public void testGetIncomeByCurrencyUsd() {
        List<MoneyShareInCurrencyProjection> result
            = incomeRepository.getIncomeByCurrency(String.valueOf(MoneyTypeCode.USD));
        Assertions.assertNotNull(result);
        Assertions.assertFalse(result.isEmpty());

        for (MoneyShareInCurrencyProjection row : result) {
            Assertions.assertNotNull(row.getCode());
            Assertions.assertNotNull(row.getTotal());
            Assertions.assertNotNull(row.getTotalConverted());
        }
    }

    /**
     * Verifies income breakdown by categories in RUB returns 4 items.
     */
    @Test
    public void testGetIncomeByCategories() {
        List<EntityAmountProjection> result = incomeRepository.getIncomeByCategories("RUB");
        Assertions.assertEquals(4, result.size());

        Assertions.assertEquals("Доходы", result.get(0).getName());
        Assertions.assertEquals(555579.87, result.get(0).getTotal(), 0.01);
        Assertions.assertEquals(142, result.get(0).getTxCount());

        Assertions.assertEquals("Разное", result.get(1).getName());
        Assertions.assertEquals(246436.46, result.get(1).getTotal(), 0.01);
        Assertions.assertEquals(576, result.get(1).getTxCount());

        Assertions.assertEquals("Служебные", result.get(2).getName());
        Assertions.assertEquals(27957.3, result.get(2).getTotal(), 0.01);
        Assertions.assertEquals(2, result.get(2).getTxCount());

        Assertions.assertEquals("Лекарства", result.get(3).getName());
        Assertions.assertEquals(220.0, result.get(3).getTotal(), 0.01);
        Assertions.assertEquals(1, result.get(3).getTxCount());
    }

    /**
     * Verifies income breakdown by categories in USD returns 4 items.
     */
    @Test
    public void testGetIncomeByCategoriesUsd() {
        List<EntityAmountProjection> result = incomeRepository.getIncomeByCategories("USD");
        Assertions.assertEquals(4, result.size());

        Assertions.assertEquals("Доходы", result.get(0).getName());
        Assertions.assertEquals(18063.49, result.get(0).getTotal(), 0.01);
        Assertions.assertEquals(142, result.get(0).getTxCount());
    }

    /**
     * Verifies income breakdown by categories in EUR returns 4 items.
     */
    @Test
    public void testGetIncomeByCategoriesEur() {
        List<EntityAmountProjection> result = incomeRepository.getIncomeByCategories("EUR");
        Assertions.assertEquals(4, result.size());
    }

    /**
     * Verifies income breakdown by organizations in RUB returns 7 items.
     */
    @Test
    public void testGetIncomeByOrganizations() {
        List<EntityAmountProjection> result = incomeRepository.getIncomeByOrganizations("RUB");
        Assertions.assertEquals(7, result.size());

        Assertions.assertEquals("Работа 1", result.get(0).getName());
        Assertions.assertEquals(294366.46, result.get(0).getTotal(), 0.01);
        Assertions.assertEquals(24, result.get(0).getTxCount());

        Assertions.assertEquals("Мое дело", result.get(1).getName());
        Assertions.assertEquals(271886.46, result.get(1).getTotal(), 0.01);
        Assertions.assertEquals(644, result.get(1).getTxCount());
    }

    /**
     * Verifies income breakdown by organizations in USD returns 7 items.
     */
    @Test
    public void testGetIncomeByOrganizationsUsd() {
        List<EntityAmountProjection> result = incomeRepository.getIncomeByOrganizations("USD");
        Assertions.assertEquals(7, result.size());

        Assertions.assertEquals("Работа 1", result.get(0).getName());
        Assertions.assertEquals(9562.38, result.get(0).getTotal(), 0.01);
        Assertions.assertEquals(24, result.get(0).getTxCount());
    }

    /**
     * Verifies income breakdown by categories for non-existent currency returns 4 items.
     */
    @SuppressWarnings("checkstyle:MultipleStringLiterals")
    @Test
    public void testGetIncomeByCategoriesNonExistentCurrency() {
        List<EntityAmountProjection> result = incomeRepository.getIncomeByCategories("XXX");
        Assertions.assertNotNull(result);
        Assertions.assertEquals(4, result.size());
    }

}
