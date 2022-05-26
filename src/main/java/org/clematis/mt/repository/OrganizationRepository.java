package org.clematis.mt.repository;

import org.clematis.mt.model.Organization;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author Anton Troshin
 */
@RepositoryRestResource(path = "organizations")
public interface OrganizationRepository extends PagingAndSortingAndFilteringByNameRepository<Organization, Long> {

}
