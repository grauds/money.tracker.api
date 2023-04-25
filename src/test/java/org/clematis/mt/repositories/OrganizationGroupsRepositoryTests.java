package org.clematis.mt.repositories;

import org.clematis.mt.ClematisMoneyTrackerApplicationTests;
import org.clematis.mt.repository.OrganizationGroupRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;

public class OrganizationGroupsRepositoryTests extends ClematisMoneyTrackerApplicationTests {

    @Autowired
    private OrganizationGroupRepository organizationGroupRepository;

    @Test
    void countGroups() {
        // all groups minus one - the same group
        Assertions.assertEquals(0,
            organizationGroupRepository
                .findRecursiveByParentId(264, Pageable.ofSize(200)).getTotalElements());
    }

    @Test
    void countPath() {
        // all groups minus one - the same group
        Assertions.assertEquals(1, organizationGroupRepository.findPathById(264).size());
    }
}
