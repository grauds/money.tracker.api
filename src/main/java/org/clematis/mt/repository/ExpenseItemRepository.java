package org.clematis.mt.repository;

import org.clematis.mt.model.ExpenseItem;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author Anton Troshin
 */
@RepositoryRestResource(path = "expenseItems")
public interface ExpenseItemRepository extends PagingAndSortingRepository<ExpenseItem, Long> {

}
