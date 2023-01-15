package org.clematis.mt.repository;

import org.clematis.mt.model.ExpenseItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @RestResource(path = "commodity")
    Page<ExpenseItem> findByCommodityId(@Param(value = "commodityId") int commodityId, Pageable pageable);

    @Query(value = "SELECT SUM(ei.total) "
            + "FROM ExpenseItem as ei LEFT JOIN Expense as e ON e.id=ei.expense.id WHERE ei.commodity.id=:commodityId "
            + "AND e.moneyType.code LIKE :moneyCode")
    @RestResource(path = "sumCommodityExpenses")
    Long sumCommodityExpenses(@Param(value = "commodityId") int commodityId,
                              @Param(value = "moneyCode") String moneyCode);


    @Query(value = "SELECT SUM(ei.qty) "
            + "FROM ExpenseItem as ei LEFT JOIN Expense as e ON e.id=ei.expense.id WHERE ei.commodity.id=:commodityId")
    @RestResource(path = "sumCommodityQuantity")
    Long sumCommodityQuantity(@Param(value = "commodityId") int commodityId);


    @RestResource(path = "tradeplace")
    Page<ExpenseItem> findByTradeplaceId(@Param(value = "tradeplaceId") int tradeplaceId, Pageable pageable);

    @Query(value = "SELECT SUM(ei.total) "
            + "FROM ExpenseItem as ei LEFT JOIN Expense as e ON e.id=ei.expense.id "
            + "WHERE ei.tradeplace.id=:organizationId "
            + "AND e.moneyType.code LIKE :moneyCode")
    @RestResource(path = "sumOrganizationExpenses")
    Long sumOrganizationExpenses(@Param(value = "organizationId") int organizationId,
                               @Param(value = "moneyCode") String moneyCode);


}
