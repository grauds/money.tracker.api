package org.clematis.mt.repositories;

import org.clematis.mt.ClematisMoneyTrackerApplicationTests;
import org.clematis.mt.dto.MoneyExchangeReport;
import org.clematis.mt.repository.MoneyExchangeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class MoneyExchangeRepositoryTests extends ClematisMoneyTrackerApplicationTests {

    @Autowired
    private MoneyExchangeRepository moneyExchangeRepository;

    @Test
    void findAll() {
        Assertions.assertEquals(5, this.moneyExchangeRepository.count());
    }

    @Test
    void getAverageRate() {

        Assertions.assertEquals(0.03988699100577624,
                this.moneyExchangeRepository.getAverageExchangeRate("USD", "RUB"));

        Assertions.assertEquals(0.02857142857142857,
                this.moneyExchangeRepository.getAverageExchangeRate("EUR", "RUB"));
    }

    @Test
    void testExchangeReport() {
        //334.4599981117249,8385.19,0.03988699100577624,0.033166395807768,-1247685.6220242358
        MoneyExchangeReport report = this.moneyExchangeRepository.getExchangeReport("USD", "RUB");
        Assertions.assertEquals(-1699.1134030666158,  report.getDelta());
        Assertions.assertEquals(30.151,  report.getCurRate());
        Assertions.assertEquals(25.070830734140486,  report.getAvgRate());
        Assertions.assertEquals(8385.19,  report.getDestAmount());
        Assertions.assertEquals(334.4599981117249,  report.getSourceAmount());
    }
}
