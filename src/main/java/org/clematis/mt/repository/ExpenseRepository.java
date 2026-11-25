package org.clematis.mt.repository;

import java.util.List;

import org.clematis.mt.dto.DateRangeDTO;
import org.clematis.mt.model.Expense;
import org.clematis.mt.projections.AgentCommodityGroupProjection;
import org.clematis.mt.projections.EntityAmountProjection;
import org.clematis.mt.projections.MoneyShareInCurrencyProjection;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;


/**
 * Repository for expense-related data access with Firebird database queries.
 * Provides methods for retrieving expense aggregates by agent, commodity group,
 * date range, and currency.
 */
@RepositoryRestResource(path = "expenses")
public interface ExpenseRepository extends PagingAndSortingRepository<Expense, Integer> {

    /**
     * Retrieves expense aggregates grouped by agent and commodity group within a specified date range.
     * Expenses are converted to the target currency using historical exchange rates.
     *
     * @param code       target currency code for conversion
     * @param moisStart  starting month (1-12)
     * @param anStart    starting year
     * @param moisEnd    ending month (1-12)
     * @param anEnd      ending year
     * @return list of agent/commodity group expense aggregates
     */
    @Query(value = """
        WITH RECURSIVE CATEGORY_TREE (ID, ANCESTOR_NAME, DEPTH) AS (
            SELECT
                c1.ID,
                c1.NAME,
                1
            FROM COMMGROUP c1
            WHERE c1.PARENT IS NULL OR c1.PARENT = 0
        
            UNION ALL
        
            SELECT
                child.ID,
                CASE WHEN parent_tree.DEPTH >= 2 THEN parent_tree.ANCESTOR_NAME ELSE child.NAME END,
                parent_tree.DEPTH + 1
            FROM COMMGROUP child
            JOIN CATEGORY_TREE parent_tree ON child.PARENT = parent_tree.ID
        ),
        
        PRE_AGGREGATED AS (
            SELECT
                u.NAME AS AGENT,
                t.ANCESTOR_NAME AS COMMODITYGROUP,
                m.CODE AS CURRENCY_CODE,
                e.TRANSFERDATE,
                EXTRACT(MONTH FROM e.TRANSFERDATE) AS MOIS,
                EXTRACT(YEAR FROM e.TRANSFERDATE) AS AN,
                SUM(COALESCE(ex.TOTAL, 0)) AS RAW_TOTAL
            FROM EXPENSE e
            INNER JOIN EXPENSEITEM ex ON ex.EXPENSE = e.ID
            LEFT JOIN MONEYTYPE m ON m.ID = e.MONEYTYPE
            LEFT JOIN USERMT u ON e.USERMT = u.ID
            LEFT JOIN COMMODITY c ON ex.COMM = c.ID
            LEFT JOIN CATEGORY_TREE t ON c.PARENT = t.ID
            WHERE e.TRANSFERDATE >= CAST(
                    CAST(:anStart AS VARCHAR(4)) || '-' ||
                    LPAD(CAST(CASE WHEN :moisStart < 1 THEN 1 ELSE :moisStart END AS VARCHAR(2)), 2, '0') || '-01'
                  AS DATE)
              AND e.TRANSFERDATE < DATEADD(1 MONTH TO CAST(
                    CAST(:anEnd AS VARCHAR(4)) || '-' ||
                    LPAD(CAST(CASE WHEN :moisEnd < 1 THEN 1 ELSE :moisEnd END AS VARCHAR(2)), 2, '0') || '-01'
                  AS DATE))

            GROUP BY 1, 2, 3, 4, 5, 6
        )
        
        SELECT
            p.AGENT,
            p.COMMODITYGROUP,
            ROUND(SUM(p.RAW_TOTAL * COALESCE(
                (SELECT FIRST 1 rate FROM CROSS_RATE_2(CAST(:code AS VARCHAR(10)), p.CURRENCY_CODE, p.TRANSFERDATE)), 0
            )), 2) AS TOTAL,
            p.MOIS,
            p.AN
        FROM PRE_AGGREGATED p
        GROUP BY p.AGENT, p.COMMODITYGROUP, p.MOIS, p.AN
        ORDER BY p.AN, p.MOIS, p.COMMODITYGROUP ASC
        
        """,
        nativeQuery = true
    )
    List<AgentCommodityGroupProjection> getAgentCommodityGroups(@Param(value = "code") String code,
                                                                @Param(value = "moisStart") int moisStart,
                                                                @Param(value = "anStart") int anStart,
                                                                @Param(value = "moisEnd") int moisEnd,
                                                                @Param(value = "anEnd") int anEnd);

    /**
     * Retrieves the date range (earliest and latest) of recorded expenses.
     *
     * @return date range containing the start and end dates of expense records
     */
    @Query(value =
        """
            SELECT new org.clematis.mt.dto.DateRangeDTO(MIN(e.transferdate)
            AS start, MAX(e.transferdate) AS end) FROM Expense e
        """
    )
    DateRangeDTO getDatesRange();

    /**
     * Calculates the total expense amount converted to the specified currency.
     *
     * @param code target currency code
     * @return total expense amount in the specified currency, or 0 if no expenses exist
     */
    @RestResource(exported = false)
    @Query(value = """
            SELECT COALESCE(SUM(TOTALCONVERTED), 0)
            FROM GET_EXPENSE_BY_CURRENCY(:code)
        """, nativeQuery = true)
    Double sumTotalInCurrency(@Param("code") String code);

    /**
     * Retrieves expense breakdown by each currency, including amounts in original currency
     * and converted to the target currency.
     *
     * @param code target currency code for conversion
     * @return list of expense shares per currency with original and converted totals
     */
    @RestResource(exported = false)
    @Query(value = """
            SELECT CODE AS code, TOTAL AS total, TOTALCONVERTED AS totalConverted
            FROM GET_EXPENSE_BY_CURRENCY(:code)
        """, nativeQuery = true)
    List<MoneyShareInCurrencyProjection> getExpensesByCurrency(@Param("code") String code);

    /**
     * Retrieves expense breakdown by commodity categories
     * @param code target currency code
     * @return list of expense shares per commodity categories
     */
    @RestResource(exported = false)
    @Query(value = """
            SELECT *
            FROM GET_EXPENSE_BY_CATEGORIES(:code)
            ORDER BY TOTAL DESC
        """, nativeQuery = true)
    List<EntityAmountProjection> getExpenseByCategories(@Param("code") String code);

    /**
     * Retrieves expense breakdown by commodity trade place
     * @param code target currency code
     * @return list of expense shares per commodity  trade place
     */
    @RestResource(exported = false)
    @Query(value = """
            SELECT *
            FROM GET_EXPENSE_BY_TRADE_PLACE(:code)
            ORDER BY TOTAL DESC
        """, nativeQuery = true)
    List<EntityAmountProjection> getExpenseByTradePlace(@Param("code") String code);

}
