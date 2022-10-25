package org.clematis.mt.repositories;

import org.clematis.mt.ClematisMoneyTrackerApplicationTests;
import org.clematis.mt.repository.CommodityGroupRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
/**
 * @author Anton Troshin
 */
public class CommodityGroupsRepositoryTests extends ClematisMoneyTrackerApplicationTests {

    @Autowired
    private CommodityGroupRepository commodityGroupRepository;

    @Test
    void countGroups() {
        // all groups minus one - the same group
        Assertions.assertEquals(0, commodityGroupRepository.findRecursiveByParentId(303).size());
    }

    @Test
    void countPath() {
        // all groups minus one - the same group
        Assertions.assertEquals(0, commodityGroupRepository.findPathById(303).size());
    }
}
