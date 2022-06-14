package org.clematis.mt.repository;

import org.clematis.mt.model.Account;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author Anton Troshin
 */
@RepositoryRestResource(path = "accounts")
public interface AccountRepository extends PagingAndSortingAndFilteringByNameRepository<Account, Integer> {


}
