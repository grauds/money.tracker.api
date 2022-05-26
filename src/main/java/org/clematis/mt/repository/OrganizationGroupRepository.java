package org.clematis.mt.repository;

import org.clematis.mt.model.OrganizationGroup;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author Anton Troshin
 */
@RepositoryRestResource(path = "organizationGroups")
public interface OrganizationGroupRepository
        extends PagingAndSortingAndFilteringByNameRepository<OrganizationGroup, Long> {

}
