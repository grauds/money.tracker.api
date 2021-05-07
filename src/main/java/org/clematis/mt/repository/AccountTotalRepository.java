package org.clematis.mt.repository;

import org.clematis.mt.model.views.AccountTotal;
import org.clematis.mt.model.views.AccountTotalKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author Anton Troshin
 */
@RepositoryRestResource(collectionResourceRel = "accountsTotals", path = "accountsTotals")
public interface AccountTotalRepository extends JpaRepository<AccountTotal, AccountTotalKey> {
}