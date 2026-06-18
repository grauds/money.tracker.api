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
    Page<ExpenseItem> findByCommodityId(@Param(value = "id") int id, Pageable pageable);

    @RestResource(path = "tradeplace")
    Page<ExpenseItem> findByTradeplaceId(@Param(value = "id") int id, Pageable pageable);

    @RestResource(path = "filtered")
    Page<ExpenseItem> findByTransferdateGreaterThanEqualAndTransferdateLessThanEqual(
            @RequestParam(value = "startDate", required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam(value = "endDate", required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
            Pageable pageable
    );

    @Query(value = """
            SELECT SUM(ei.total)
            FROM ExpenseItem as ei LEFT JOIN Expense as e ON e.id=ei.expense.id
            WHERE ei.commodity.id=:id 
            AND e.moneyType.code LIKE :moneyCode
        """)
    @RestResource(path = "sumCommodityExpenses")
    Double sumCommodityExpenses(@Param(value = "id") int id,
                              @Param(value = "moneyCode") String moneyCode);

    @Query(value = """
            SELECT SUM(ei.total)
            FROM ExpenseItem as ei LEFT JOIN Expense as e ON e.id=ei.expense.id
            WHERE ei.tradeplace.id=:id
            AND e.moneyType.code LIKE :moneyCode
        """)
    @RestResource(path = "sumOrganizationExpenses")
    Double sumOrganizationExpenses(@Param(value = "id") int id,
                                 @Param(value = "moneyCode") String moneyCode);

    @Query(value = """
            WITH RECURSIVE w1(id, parent, name) AS
            (SELECT c.id, c.parent, c.name
                FROM COMMGROUP as c
                WHERE c.id = :id
                UNION ALL
                SELECT c2.id, c2.parent, c2.name
                FROM w1 JOIN COMMGROUP as c2 ON c2.parent=w1.id
            )
            SELECT SUM(ei.TOTAL) FROM EXPENSEITEM as ei
            LEFT JOIN EXPENSE as e ON e.id=ei.expense WHERE ei.COMM IN
            (SELECT ID FROM COMMODITY WHERE PARENT IN (SELECT w1.id FROM w1))
            AND e.MONEYTYPE=(SELECT ID FROM MONEYTYPE WHERE MONEYTYPE.CODE LIKE :moneyCode)
        """, nativeQuery = true)
    @RestResource(path = "sumCommodityGroupExpenses")
    Long sumCommodityGroupExpenses(@Param(value = "id") int id,
                                   @Param(value = "moneyCode") String moneyCode);

    @Query(value = """
            SELECT SUM(ei.qty)
            FROM ExpenseItem as ei LEFT JOIN Expense as e ON e.id=ei.expense.id
            WHERE ei.commodity.id=:id
        """
    )
    @RestResource(path = "sumCommodityQuantity")
    Long sumCommodityQuantity(@Param(value = "id") int id);


    @Query(value = "SELECT COUNT(1) FROM ExpenseItem e WHERE e.commodity IS NULL")
    Long countItemsWithNoCommodity();

    @Query(value = "SELECT COUNT(1) FROM ExpenseItem e WHERE e.tradeplace = 1")
    Long countItemsWithNoTradeplace();

    @Query(value = "SELECT COUNT(1) FROM ExpenseItem e WHERE e.tradeplace <> 1")
    Long countItemsWithTradeplace();
}
