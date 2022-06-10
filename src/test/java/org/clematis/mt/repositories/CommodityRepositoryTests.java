package org.clematis.mt.repositories;

import java.util.List;

import org.clematis.mt.ClematisMoneyTrackerApplicationTests;
import org.clematis.mt.model.Commodity;
import org.clematis.mt.repository.CommodityRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class CommodityRepositoryTests extends ClematisMoneyTrackerApplicationTests {

    @Autowired
    private CommodityRepository commodityRepository;

    @Test
    void countCommodities() {
        Assertions.assertEquals(1369, commodityRepository.count());
    }

    @Test
    void countRecursiveCommodities() {
        Assertions.assertEquals(5, commodityRepository.findRecursiveByGroupId(303).size());
    }

    @Test
    public void findCommodities() {
        List<Commodity> commodityList = commodityRepository.findByNameContains("ек");
        Assertions.assertEquals(39, commodityList.size());
    }
}
