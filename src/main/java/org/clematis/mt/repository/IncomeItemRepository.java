package org.clematis.mt.repository;

import org.clematis.mt.model.IncomeItem;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author Anton Troshin
 */
@RepositoryRestResource(collectionResourceRel = "incomeItems", path = "incomeItems")
public interface IncomeItemRepository extends PagingAndSortingRepository<IncomeItem, Long> {

}
