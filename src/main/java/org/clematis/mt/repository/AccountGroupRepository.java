package org.clematis.mt.repository;

import org.clematis.mt.model.AccountGroup;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author Anton Troshin
 */
@RepositoryRestResource(path = "accountGroups")
public interface AccountGroupRepository extends PagingAndSortingAndFilteringByNameRepository<AccountGroup, Long> {

}
