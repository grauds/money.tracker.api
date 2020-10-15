package org.clematis.mt.repository;

import org.clematis.mt.model.Income;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author Anton Troshin
 */
@RepositoryRestResource(collectionResourceRel = "income", path = "income")
public interface IncomeRepository extends PagingAndSortingRepository<Income, Long> {

}
