package org.clematis.mt.repository;

import java.util.List;

import org.clematis.mt.dto.AgentCommodityGroup;
import org.clematis.mt.dto.DateRange;
import org.clematis.mt.model.Expense;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author Anton Troshin
 */
@RepositoryRestResource(path = "expenses")
public interface ExpenseRepository extends PagingAndSortingRepository<Expense, Integer> {

    @Query(value = """
        WITH RECURSIVE CATEGORY_TREE (ID, PARENT, NAME, ANCESTOR_ID, DEPTH) AS (
            -- Base case: Root categories (Level 1)
            SELECT
                c1.ID, 
                c1.PARENT, 
                c1.NAME, 
                c1.ID, 
                1
            FROM COMMGROUP c1
            WHERE c1.PARENT IS NULL OR c1.PARENT = 0
            
            UNION ALL
            
            -- Recursive case: Digging into children while pinning the Level 3 ancestor
            SELECT 
                child.ID, 
                child.PARENT, 
                child.NAME,
                CASE WHEN parent_tree.DEPTH >= 2 THEN parent_tree.ANCESTOR_ID ELSE child.ID END,
                parent_tree.DEPTH + 1
            FROM COMMGROUP child
            JOIN CATEGORY_TREE parent_tree ON child.PARENT = parent_tree.ID
        ),
        
        COMMODITY_MAPPING AS (
            SELECT 
                c.ID AS COMM_ID,
                cg.NAME AS FINAL_GROUP_NAME
            FROM COMMODITY c
            LEFT JOIN CATEGORY_TREE t ON c.PARENT = t.ID
            LEFT JOIN COMMGROUP cg ON t.ANCESTOR_ID = cg.ID
        )

        SELECT
            u.NAME AS AGENT,
            cm.FINAL_GROUP_NAME AS COMMODITYGROUP,
            ROUND(SUM(COALESCE(ex.TOTAL, 0) * COALESCE(
                    (SELECT FIRST 1 rate FROM CROSS_RATE(:code, m.CODE, e.TRANSFERDATE)), 0
            )), 2) AS TOTAL,
            EXTRACT(MONTH FROM e.TRANSFERDATE) AS MOIS,
            EXTRACT(YEAR FROM e.TRANSFERDATE) AS AN
        FROM EXPENSEITEM ex
        LEFT JOIN COMMODITY_MAPPING cm ON ex.COMM = cm.COMM_ID
        LEFT JOIN EXPENSE e ON ex.EXPENSE = e.ID
        LEFT JOIN MONEYTYPE m ON m.ID = e.MONEYTYPE
        LEFT JOIN USERMT u ON e.USERMT = u.ID
        WHERE
            (EXTRACT(YEAR FROM e.TRANSFERDATE) > :anStart OR (EXTRACT(YEAR FROM e.TRANSFERDATE) = :anStart
                                                            AND EXTRACT(MONTH FROM e.TRANSFERDATE) >= :moisStart))
            AND 
            (EXTRACT(YEAR FROM e.TRANSFERDATE) < :anEnd OR (EXTRACT(YEAR FROM e.TRANSFERDATE) = :anEnd 
                                                            AND EXTRACT(MONTH FROM e.TRANSFERDATE) <= :moisEnd))
        GROUP BY u.NAME, cm.FINAL_GROUP_NAME, EXTRACT(MONTH FROM e.TRANSFERDATE), EXTRACT(YEAR FROM e.TRANSFERDATE)
        ORDER BY AN, MOIS, COMMODITYGROUP ASC
        """,
        nativeQuery = true
    )
    List<AgentCommodityGroup> getAgentCommodityGroups(@Param(value = "code") String code,
                                                      @Param(value = "moisStart") int moisStart,
                                                      @Param(value = "anStart") int anStart,
                                                      @Param(value = "moisEnd") int moisEnd,
                                                      @Param(value = "anEnd") int anEnd);

    @Query(value =
        """
            SELECT new org.clematis.mt.dto.DateRange(MIN(e.transferdate)
            AS start, MAX(e.transferdate) AS end) FROM Expense e
        """
    )
    DateRange getDatesRange();
}
