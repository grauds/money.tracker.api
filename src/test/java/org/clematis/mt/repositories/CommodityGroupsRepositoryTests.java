package org.clematis.mt.repositories;

import org.clematis.mt.ClematisMoneyTrackerApplicationTests;
import org.clematis.mt.repository.CommodityGroupRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class CommodityGroupsRepositoryTests extends ClematisMoneyTrackerApplicationTests {

    @Autowired
    private CommodityGroupRepository commodityGroupRepository;

    @Test
    void countGroups() {
        Assertions.assertEquals(1, commodityGroupRepository.findRecursiveByParentId(303).size());
    }
}
