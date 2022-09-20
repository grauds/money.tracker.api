package org.clematis.mt.repositories;

import org.clematis.mt.ClematisMoneyTrackerApplicationTests;
import org.clematis.mt.model.Organization;
import org.clematis.mt.repository.OrganizationRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public class OrganizationRepositoryTests extends ClematisMoneyTrackerApplicationTests {

    @Autowired
    private OrganizationRepository organizationRepository;

    @Test
    void countOrganizations() {
        Assertions.assertEquals(99, organizationRepository.count());
    }

    @Test
    void countRecursiveOrganizations() {
        Assertions.assertEquals(7, organizationRepository.findRecursiveByGroupId(264).size());
    }

    @Test
    public void findOrganizations() {
        Page<Organization> organizationList
                = organizationRepository.findByNameContainingIgnoreCase("ек", PageRequest.of(0, 50));
        Assertions.assertEquals(12, organizationList.getTotalElements());
    }
}
