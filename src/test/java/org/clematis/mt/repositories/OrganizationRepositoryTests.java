package org.clematis.mt.repositories;

import org.clematis.mt.ClematisMoneyTrackerApplicationTests;
import org.clematis.mt.model.Organization;
import org.clematis.mt.repository.OrganizationRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
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
        Assertions.assertEquals(7, organizationRepository.findRecursiveByParentGroupId(264,
            PageRequest.of(0, 50)).getTotalElements());
    }

    @Test
    @Disabled
    public void findOrganizations() {
        Organization organization = new Organization();
        organization.setName("ек");
        ExampleMatcher matcher = ExampleMatcher.matching()
            .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
            .withIgnoreCase();

        Example<Organization> example = Example.of(organization, matcher);

        Page<Organization> organizationList
                = organizationRepository.findAll(example, PageRequest.of(0, 50));
        Assertions.assertEquals(12, organizationList.getTotalElements());
    }
}
