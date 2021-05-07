package org.clematis.mt.repositories;

import java.util.List;

import org.clematis.mt.ClematisMoneyTrackerApplicationTests;
import org.clematis.mt.model.views.AccountTotal;
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

        Assertions.assertEquals("Долги", totals.get(0).getKey().getName());
        Assertions.assertEquals("Кошелек WebMoney Z", totals.get(1).getKey().getName());
        Assertions.assertEquals("Кошелек USD", totals.get(2).getKey().getName());
        Assertions.assertEquals("Кошелек евро", totals.get(3).getKey().getName());
        Assertions.assertEquals("Кошелек юани", totals.get(4).getKey().getName());
        Assertions.assertEquals("Карточка ВТБ (зарплата старая)", totals.get(5).getKey().getName());
        Assertions.assertEquals("Карточка ВТБ Иван", totals.get(6).getKey().getName());
        Assertions.assertEquals("Карт. Сбербанк Иван", totals.get(7).getKey().getName());
        Assertions.assertEquals("Карточка ВББ Мария", totals.get(8).getKey().getName());
        Assertions.assertEquals("Вклад \"ХХХ\" 06.10", totals.get(9).getKey().getName());
        Assertions.assertEquals("Карт. Сбербанк Мария", totals.get(10).getKey().getName());
        Assertions.assertEquals("Яндекс.Кошелек", totals.get(11).getKey().getName());
        Assertions.assertEquals("Кошелек WebMoney R", totals.get(12).getKey().getName());
        Assertions.assertEquals("Фонд сберегательный", totals.get(13).getKey().getName());
        Assertions.assertEquals("Вклад \"ХХХ\" 15.11", totals.get(14).getKey().getName());
        Assertions.assertEquals("Вклад \"ХХХ\" 22.12", totals.get(15).getKey().getName());
        Assertions.assertEquals("Кошелек", totals.get(16).getKey().getName());

    }
}
