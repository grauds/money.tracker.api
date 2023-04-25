package org.clematis.mt.repositories;

import org.clematis.mt.ClematisMoneyTrackerApplicationTests;
import org.clematis.mt.model.Commodity;
import org.clematis.mt.repository.CommodityRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public class CommodityRepositoryTests extends ClematisMoneyTrackerApplicationTests {

    @Autowired
    private CommodityRepository commodityRepository;

    @Test
    void countCommodities() {
        Assertions.assertEquals(1369, commodityRepository.count());
    }

    @Test
    public void findCommodities() {
        Page<Commodity> commodityList
                = commodityRepository.findByNameContainingIgnoreCase("ек", PageRequest.of(0, 50));
        Assertions.assertEquals(40, commodityList.getTotalElements());
    }

    @Test
    void countRecursiveCommodities() {
        Assertions.assertEquals(5,
            commodityRepository.findCommoditiesRecursiveByGroupId(303,
                Pageable.ofSize(200)).getTotalElements());
    }
}
