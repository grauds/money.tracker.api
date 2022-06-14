package org.clematis.mt.repository;

import java.util.List;

import org.clematis.mt.model.Expense;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author Anton Troshin
 */
@RepositoryRestResource(path = "expenses")
public interface ExpenseRepository extends PagingAndSortingRepository<Expense, Integer> {

    List<Expense> findByIdAndMoneyTypeCode(int id, String moneyTypeCode);
}
