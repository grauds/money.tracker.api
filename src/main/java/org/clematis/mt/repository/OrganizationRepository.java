package org.clematis.mt.repository;

import org.clematis.mt.model.Organization;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author Anton Troshin
 */
@RepositoryRestResource(collectionResourceRel = "organization", path = "organization")
public interface OrganizationRepository extends PagingAndSortingRepository<Organization, Long> {

}
