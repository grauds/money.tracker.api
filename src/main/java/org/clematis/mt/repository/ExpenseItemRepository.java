package org.clematis.mt.repository;

import java.util.List;

import org.clematis.mt.model.ExpenseItem;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

/**
 * @author Anton Troshin
 */
@RepositoryRestResource(path = "expenseItems")
public interface ExpenseItemRepository extends PagingAndSortingRepository<ExpenseItem, Integer> {

    List<ExpenseItem> findByCommodityId(@Param(value = "commodityId") int commodityId);

    @Query(value = "SELECT SUM(ei.total) "
            + "FROM ExpenseItem as ei LEFT JOIN Expense as e ON e.id=ei.expense.id WHERE ei.commodity.id=:commodityId "
            + "AND e.moneyType.code LIKE :moneyCode")
    @RestResource(path = "sumCommodityExpenses")
    Long sumCommodityExpenses(@Param(value = "commodityId") int commodityId,
                              @Param(value = "moneyCode") String moneyCode);
}
