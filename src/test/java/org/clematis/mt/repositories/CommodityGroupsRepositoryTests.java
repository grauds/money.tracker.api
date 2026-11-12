package org.clematis.mt.repositories;

import org.clematis.mt.ClematisMoneyTrackerApplicationTests;
import org.clematis.mt.repository.CommodityGroupRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;


/**
 * Repository tests for CommodityGroup data access.
 */
public class CommodityGroupsRepositoryTests extends ClematisMoneyTrackerApplicationTests {

    @Autowired
    private CommodityGroupRepository commodityGroupRepository;

    /**
     * Verifies the count of recursive commodity group children for a given parent ID.
     */
    @Test
    void countGroups() {
        // all groups minus one - the same group
        Assertions.assertEquals(0,
            commodityGroupRepository.findRecursiveByParentId(303, Pageable.ofSize(20)).getTotalElements());
    }

    /**
     * Tests counting commodity groups with associated commodities.
     */
    @Disabled
    @Test
    void countGroupsWithCommodities() {
        // all groups minus one - the same group
        Assertions.assertEquals(6,
            commodityGroupRepository.getCommodityGroupChildren(303,
                Pageable.ofSize(200)).getTotalElements());
    }


    /**
     * Verifies the path retrieval for a commodity group by its ID.
     */
    @Test
    void countPath() {
        // all groups minus one - the same group
        Assertions.assertEquals(0, commodityGroupRepository.findPathById(303).size());
    }
}
