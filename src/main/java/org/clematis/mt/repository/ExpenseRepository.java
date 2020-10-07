package org.clematis.mt.repository;

import org.clematis.mt.model.Expense;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author Anton Troshin
 */
@RepositoryRestResource(collectionResourceRel = "expense", path = "expense")
public interface ExpenseRepository extends PagingAndSortingRepository<Expense, Long> {

}
