package org.clematis.mt.repositories;

import org.clematis.mt.ClematisMoneyTrackerApplicationTests;
import org.clematis.mt.repository.CommodityRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class CommodityRepositoryTests extends ClematisMoneyTrackerApplicationTests {

    @Autowired
    private CommodityRepository commodityRepository;

    @Test
    void countCommodities() throws Exception {
        Assertions.assertEquals(1369, commodityRepository.count());
    }
}
