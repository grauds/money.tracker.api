package org.clematis.mt.repository;

import java.util.Date;

import org.clematis.mt.model.ExpenseItem;
import org.clematis.mt.model.ExpenseItemEntry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Anton Troshin
 */
@RepositoryRestResource(path = "expenseItems", excerptProjection = ExpenseItemEntry.class)
public interface ExpenseItemRepository extends JpaRepository<ExpenseItem, Integer> {

    @RestResource(path = "commodity")
    Page<ExpenseItem> findByCommodityId(@Param(value = "commodityId") int commodityId, Pageable pageable);

    @RestResource(path = "tradeplace")
    Page<ExpenseItem> findByTradeplaceId(@Param(value = "tradeplaceId") int tradeplaceId, Pageable pageable);

    @RestResource(path = "filtered")
    Page<ExpenseItem> findByTransferdateGreaterThanEqualAndTransferdateLessThanEqual(
            @RequestParam(value = "startDate", required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam(value = "endDate", required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
            Pageable pageable
    );

    @Query(value = "SELECT SUM(ei.total) "
            + "FROM ExpenseItem as ei LEFT JOIN Expense as e ON e.id=ei.expense.id "
            + "WHERE ei.commodity.id=:commodityId "
            + "AND e.moneyType.code LIKE :moneyCode")
    @RestResource(path = "sumCommodityExpenses")
    Double sumCommodityExpenses(@Param(value = "commodityId") int commodityId,
                              @Param(value = "moneyCode") String moneyCode);

    @Query(value = "SELECT SUM(ei.total) "
            + "FROM ExpenseItem as ei LEFT JOIN Expense as e ON e.id=ei.expense.id "
            + "WHERE ei.tradeplace.id=:organizationId "
            + "AND e.moneyType.code LIKE :moneyCode")
    @RestResource(path = "sumOrganizationExpenses")
    Double sumOrganizationExpenses(@Param(value = "organizationId") int organizationId,
                                 @Param(value = "moneyCode") String moneyCode);

    @Query(value = "SELECT SUM(ei.qty) "
            + "FROM ExpenseItem as ei LEFT JOIN Expense as e ON e.id=ei.expense.id WHERE ei.commodity.id=:commodityId")
    @RestResource(path = "sumCommodityQuantity")
    Long sumCommodityQuantity(@Param(value = "commodityId") int commodityId);



}
