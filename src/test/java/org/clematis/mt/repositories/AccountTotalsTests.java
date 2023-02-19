package org.clematis.mt.repositories;

import java.util.List;

import org.clematis.mt.ClematisMoneyTrackerApplicationTests;
import org.clematis.mt.model.AccountTotal;
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

    @Test
    public void getBalances() {
        List<AccountTotal> totals = accountTotalRepository.findAll();
        Assertions.assertEquals(17, totals.size());

        Assertions.assertEquals("Долги", totals.get(0).getName());
        Assertions.assertEquals("Кошелек WebMoney Z", totals.get(1).getName());
        Assertions.assertEquals("Кошелек USD", totals.get(2).getName());
        Assertions.assertEquals("Кошелек евро", totals.get(3).getName());
        Assertions.assertEquals("Кошелек юани", totals.get(4).getName());
        Assertions.assertEquals("Карточка ВТБ (зарплата старая)", totals.get(5).getName());
        Assertions.assertEquals("Карточка ВТБ Иван", totals.get(6).getName());
        Assertions.assertEquals("Карт. Сбербанк Иван", totals.get(7).getName());
        Assertions.assertEquals("Карточка ВББ Мария", totals.get(8).getName());
        Assertions.assertEquals("Вклад \"ХХХ\" 06.10", totals.get(9).getName());
        Assertions.assertEquals("Карт. Сбербанк Мария", totals.get(10).getName());
        Assertions.assertEquals("Яндекс.Кошелек", totals.get(11).getName());
        Assertions.assertEquals("Кошелек WebMoney R", totals.get(12).getName());
        Assertions.assertEquals("Фонд сберегательный", totals.get(13).getName());
        Assertions.assertEquals("Вклад \"ХХХ\" 15.11", totals.get(14).getName());
        Assertions.assertEquals("Вклад \"ХХХ\" 22.12", totals.get(15).getName());
        Assertions.assertEquals("Кошелек", totals.get(16).getName());

    }

    @Test
    public void getBalancesInCode() {
        List<AccountTotal> totals = accountTotalRepository.getAccountTotals("RUB");
        Assertions.assertEquals(17, totals.size());

        Assertions.assertEquals(-10000, totals.get(0).getBalance());
        Assertions.assertEquals(2003.84, totals.get(1).getBalance());
        Assertions.assertEquals(2261.32, totals.get(2).getBalance());
        Assertions.assertEquals(3784.61, totals.get(3).getBalance());
        Assertions.assertEquals(7667.42, totals.get(4).getBalance());
        Assertions.assertEquals(7686.04, totals.get(5).getBalance());
        Assertions.assertEquals(7933.92, totals.get(6).getBalance());
        Assertions.assertEquals(8656.75, totals.get(7).getBalance());
        Assertions.assertEquals(8938.27, totals.get(8).getBalance());
        Assertions.assertEquals(10000.0, totals.get(9).getBalance());
        Assertions.assertEquals(10308.78, totals.get(10).getBalance());
        Assertions.assertEquals(13852.25, totals.get(11).getBalance());
        Assertions.assertEquals(21917.67, totals.get(12).getBalance());
        Assertions.assertEquals(52605.0, totals.get(13).getBalance());
        Assertions.assertEquals(70000.0, totals.get(14).getBalance());
        Assertions.assertEquals(80000.0, totals.get(15).getBalance());
        Assertions.assertEquals(289019.27, totals.get(16).getBalance());
    }

    @Test
    public void getTotalBalanceInCode() {
        Assertions.assertEquals(586635, accountTotalRepository.getBalanceForCurrency("RUB"));
    }
}
