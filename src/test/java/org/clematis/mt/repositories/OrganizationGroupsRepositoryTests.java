package org.clematis.mt.repositories;

import org.clematis.mt.ClematisMoneyTrackerApplicationTests;
import org.clematis.mt.repository.OrganizationGroupRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;


/**
 * Repository tests for OrganizationGroup data access.
 */
public class OrganizationGroupsRepositoryTests extends ClematisMoneyTrackerApplicationTests {

    @Autowired
    private OrganizationGroupRepository organizationGroupRepository;

    /**
     * Verifies the count of recursive organization group children for a given parent ID.
     */
    @Test
    void countGroups() {
        // all groups minus one - the same group
        Assertions.assertEquals(0,
            organizationGroupRepository
                .findRecursiveByParentId(264, Pageable.ofSize(200)).getTotalElements());
    }

    /**
     * Verifies the path retrieval for an organization group by its ID.
     */
    @Test
    void countPath() {
        // all groups minus one - the same group
        Assertions.assertEquals(1, organizationGroupRepository.findPathById(264).size());
    }
}
