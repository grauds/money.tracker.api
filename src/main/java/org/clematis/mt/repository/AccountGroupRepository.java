package org.clematis.mt.repository;

import org.clematis.mt.model.AccountGroup;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author Anton Troshin
 */
@RepositoryRestResource(collectionResourceRel = "accountGroup", path = "accountGroup")
public interface AccountGroupRepository extends PagingAndSortingRepository<AccountGroup, Long> {

}
