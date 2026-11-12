package org.clematis.mt.repository;

import java.util.Date;
import java.util.Optional;

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


/**
 * Repository for expense item data access with Firebird database queries.
 * Provides methods for retrieving expense items by commodity, organization, account,
 * and date range, as well as aggregate expense calculations by various dimensions.
 */
@RepositoryRestResource(path = "expenseItems", excerptProjection = ExpenseItemEntry.class)
public interface ExpenseItemRepository extends JpaRepository<ExpenseItem, Integer> {

    /**
     * Retrieves expense items filtered by commodity ID.
     *
     * @param id the commodity identifier
     * @param pageable pagination information
     * @return page of expense items for the specified commodity
     */
    @RestResource(path = "commodity")
    Page<ExpenseItem> findByCommodityId(@Param(value = "id") int id, Pageable pageable);

    /**
     * Retrieves expense items filtered by tradeplace (organization) ID.
     *
     * @param id the tradeplace identifier
     * @param pageable pagination information
     * @return page of expense items for the specified tradeplace
     */
    @RestResource(path = "tradeplace")
    Page<ExpenseItem> findByTradeplaceId(@Param(value = "id") int id, Pageable pageable);

    /**
     * Retrieves expense items filtered by expense account ID.
     *
     * @param accountId the account identifier
     * @param pageable pagination information
     * @return page of expense items for the specified account
     */
    @RestResource(path = "account")
    Page<ExpenseItem> findByExpenseAccountId(@Param(value = "id") int accountId, Pageable pageable);

    /**
     * Retrieves expense items within the specified date range.
     *
     * @param startDate start date (inclusive), if null no lower bound is applied
     * @param endDate end date (inclusive), if null no upper bound is applied
     * @param pageable pagination information
     * @return page of expense items within the specified date range
     */
    @RestResource(path = "filtered")
    Page<ExpenseItem> findByTransferdateGreaterThanEqualAndTransferdateLessThanEqual(
        @Param("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Optional<Date> startDate,
        @Param("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Optional<Date> endDate,
        Pageable pageable
    );

    /**
     * Calculates the total daily expenses converted to the specified currency.
     *
     * @param targetDate the date for which to calculate total expenses
     * @param moneyCode the target currency code
     * @return total expenses on the specified date converted to the target currency
     */
    @Query(value = """
        SELECT SUM(ei.total / NULLIF((SELECT RATE FROM CROSS_RATE_2(m.code, :moneyCode, e.TRANSFERDATE)), 0))
            FROM ExpenseItem as ei
                 JOIN Expense as e ON e.id = ei.expense
                 JOIN MoneyType m ON m.id = e.moneyType
            WHERE CAST(e.TRANSFERDATE AS DATE) = :targetDate
        """, nativeQuery = true)
    @RestResource(path = "sumDailyExpenses")
    Double sumDailyExpenses(
        @Param(value = "targetDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date targetDate,
        @Param(value = "moneyCode") String moneyCode
    );

    /**
     * Calculates the total expenses for a specific commodity converted to the specified currency.
     *
     * @param id the commodity identifier
     * @param moneyCode the target currency code
     * @return total expenses for the commodity converted to the target currency
     */
    @Query(value = """
        SELECT SUM(ei.total /  NULLIF((SELECT * FROM CROSS_RATE_2(m.code, :moneyCode, e.TRANSFERDATE)), 0))
            FROM ExpenseItem as ei
                JOIN Expense as e ON e.id=ei.expense
                JOIN MoneyType m ON m.id = e.moneyType
            WHERE ei.COMM=:id
        """, nativeQuery = true)
    @RestResource(path = "sumCommodityExpenses")
    Double sumCommodityExpenses(
        @Param(value = "id") int id,
        @Param(value = "moneyCode") String moneyCode
    );

    /**
     * Calculates the total expenses for a specific organization (tradeplace) converted to the specified currency.
     *
     * @param id the organization identifier
     * @param moneyCode the target currency code
     * @return total expenses for the organization converted to the target currency
     */
    @Query(value = """
        SELECT SUM(ei.total / NULLIF((SELECT RATE FROM CROSS_RATE_2(m.code, :moneyCode, e.TRANSFERDATE)), 0))
            FROM ExpenseItem as ei
                 JOIN Expense as e ON e.id = ei.expense
                 JOIN MoneyType m ON m.id = e.moneyType
            WHERE ei.tradeplace = :id
        """, nativeQuery = true)
    @RestResource(path = "sumOrganizationExpenses")
    Double sumOrganizationExpenses(
        @Param(value = "id") int id,
        @Param(value = "moneyCode") String moneyCode
    );

    /**
     * Calculates the total expenses for a specific account converted to the specified currency.
     *
     * @param id the account identifier
     * @param moneyCode the target currency code
     * @return total expenses for the account converted to the target currency
     */
    @Query(value = """
        SELECT SUM(ei.total / NULLIF((SELECT RATE FROM CROSS_RATE_2(m.code, :moneyCode, e.TRANSFERDATE)), 0))
            FROM ExpenseItem as ei
                 JOIN Expense as e ON e.id = ei.expense
                 JOIN MoneyType m ON m.id = e.moneyType
            WHERE e.ACCOUNT = :id
        """, nativeQuery = true)
    @RestResource(path = "sumAccountExpenses")
    Double sumAccountExpenses(
        @Param(value = "id") int id,
        @Param(value = "moneyCode") String moneyCode
    );

    /**
     * Calculates the total expenses for a commodity group (including all subgroups)
     * converted to the specified currency.
     *
     * @param id the commodity group identifier
     * @param moneyCode the target currency code
     * @return total expenses for the commodity group and all subgroups converted to the target currency
     */
    @Query(value = """
            WITH RECURSIVE w1(id, parent, name) AS
            (
                SELECT c.id, c.parent, c.name
                FROM COMMGROUP as c
                WHERE c.id = :id
                UNION ALL
                SELECT c2.id, c2.parent, c2.name
                FROM w1 JOIN COMMGROUP as c2 ON c2.parent = w1.id
            )
            SELECT SUM(ei.TOTAL / NULLIF((SELECT RATE FROM CROSS_RATE_2(m.code, :moneyCode, e.TRANSFERDATE)), 0))
            FROM EXPENSEITEM as ei
                 JOIN EXPENSE as e ON e.id = ei.expense
                 JOIN MONEYTYPE m ON m.id = e.moneyType
            WHERE ei.COMM IN (
                SELECT ID FROM COMMODITY WHERE PARENT IN (SELECT w1.id FROM w1)
            )
         """, nativeQuery = true)
    @RestResource(path = "sumCommodityGroupExpenses")
    Long sumCommodityGroupExpenses(@Param(value = "id") int id,
                                   @Param(value = "moneyCode") String moneyCode
    );

    /**
     * Calculates the total expenses for an organization group (including all subgroups)
     * converted to the specified currency.
     *
     * @param id the organization group identifier
     * @param moneyCode the target currency code
     * @return total expenses for the organization group and all subgroups converted to the target currency
     */
    @Query(value = """
            WITH RECURSIVE w1(id, parent, name) AS
            (
                SELECT c.id, c.parent, c.name
                FROM ORGANIZATIONGROUP as c
                WHERE c.id = :id
                UNION ALL
                SELECT c2.id, c2.parent, c2.name
                FROM w1 JOIN ORGANIZATIONGROUP as c2 ON c2.parent = w1.id
            )
            SELECT SUM(ei.TOTAL / NULLIF((SELECT RATE FROM CROSS_RATE_2(m.code, :moneyCode, e.TRANSFERDATE)), 0))
            FROM EXPENSEITEM as ei
                 JOIN EXPENSE as e ON e.id = ei.expense
                 JOIN MONEYTYPE m ON m.id = e.moneyType
            WHERE ei.TRADEPLACE IN (
                SELECT ID FROM ORGANIZATION WHERE PARENT IN (SELECT w1.id FROM w1)
            )
         """, nativeQuery = true)
    @RestResource(path = "sumOrganizationGroupExpenses")
    Long sumOrganizationGroupExpenses(@Param(value = "id") int id,
                                   @Param(value = "moneyCode") String moneyCode
    );

    /**
     * Calculates the total expenses for an account group (including all subgroups) converted to the specified currency.
     *
     * @param id the account group identifier
     * @param moneyCode the target currency code
     * @return total expenses for the account group and all subgroups converted to the target currency
     */
    @Query(value = """
            WITH RECURSIVE w1(id, parent, name) AS
            (
                SELECT c.id, c.parent, c.name
                FROM ACCOUNTGROUP as c
                WHERE c.id = :id
                UNION ALL
                SELECT c2.id, c2.parent, c2.name
                FROM w1 JOIN ACCOUNTGROUP as c2 ON c2.parent = w1.id
            )
            SELECT SUM(ei.TOTAL / NULLIF((SELECT RATE FROM CROSS_RATE_2(m.code, :moneyCode, e.TRANSFERDATE)), 0))
            FROM EXPENSEITEM as ei
                 JOIN EXPENSE as e ON e.id = ei.expense
                 JOIN MONEYTYPE m ON m.id = e.moneyType
            WHERE e.ACCOUNT IN (
                SELECT ID FROM ACCOUNT WHERE PARENT IN (SELECT w1.id FROM w1)
            )
         """, nativeQuery = true)
    @RestResource(path = "sumAccountGroupExpenses")
    Long sumAccountGroupExpenses(@Param(value = "id") int id,
                                   @Param(value = "moneyCode") String moneyCode
    );


    /**
     * Calculates the total quantity of a specific commodity from expense items.
     *
     * @param id the commodity identifier
     * @return total quantity of the commodity from expense items
     */
    @Query(value = """
        SELECT SUM(ei.qty)
            FROM ExpenseItem as ei
            LEFT JOIN Expense as e ON e.id = ei.expense.id
        WHERE ei.commodity.id=:id
        """
    )
    @RestResource(path = "sumCommodityQuantity")
    Long sumCommodityQuantity(@Param(value = "id") int id);


    /**
     * Counts the number of expense items that have no commodity assigned.
     *
     * @return count of expense items with no commodity
     */
    @Query(value = "SELECT COUNT(1) FROM ExpenseItem e WHERE e.commodity IS NULL")
    Long countItemsWithNoCommodity();

    /**
     * Counts the number of expense items with no tradeplace (organization) assigned.
     *
     * @return count of expense items with no tradeplace
     */
    @Query(value = "SELECT COUNT(1) FROM ExpenseItem e WHERE e.tradeplace = 1")
    Long countItemsWithNoTradeplace();

    /**
     * Counts the number of expense items with a tradeplace (organization) assigned.
     *
     * @return count of expense items with a tradeplace
     */
    @Query(value = "SELECT COUNT(1) FROM ExpenseItem e WHERE e.tradeplace <> 1")
    Long countItemsWithTradeplace();
}
