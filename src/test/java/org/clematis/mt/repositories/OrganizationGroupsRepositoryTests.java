package org.clematis.mt.repositories;

import org.clematis.mt.ClematisMoneyTrackerApplicationTests;
import org.clematis.mt.repository.OrganizationGroupRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class OrganizationGroupsRepositoryTests extends ClematisMoneyTrackerApplicationTests {

    @Autowired
    private OrganizationGroupRepository organizationGroupRepository;

    @Test
    void countGroups() {
        // all groups minus one - the same group
        Assertions.assertEquals(0, organizationGroupRepository.findRecursiveByParentId(264).size());
    }
}
