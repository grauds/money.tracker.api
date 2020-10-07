package org.clematis.mt.repository;

import org.clematis.mt.model.OrganizationGroup;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author Anton Troshin
 */
@RepositoryRestResource(collectionResourceRel = "organizationGroup", path = "organizationGroup")
public interface OrganizationGroupRepository extends PagingAndSortingRepository<OrganizationGroup, Long> {

}
