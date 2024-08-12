package org.clematis.mt.repository;

import java.util.List;

import org.clematis.mt.dto.AgentCommodityGroup;
import org.clematis.mt.model.Expense;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

/**
 * @author Anton Troshin
 */
@RepositoryRestResource(path = "expenses")
public interface ExpenseRepository extends PagingAndSortingRepository<Expense, Integer> {

    @Query(value = "WITH RECURSIVE w1(id, parent, name) AS "
            + "(SELECT c.id, c.parent, c.name "
            + "    FROM COMMGROUP as c "
            + "    WHERE c.id = :commodityGroupId "
            + "    UNION ALL "
            + "    SELECT c2.id, c2.parent, c2.name "
            + "    FROM w1 JOIN COMMGROUP as c2 ON c2.parent=w1.id "
            + ") "
            + "SELECT SUM(ei.TOTAL) FROM EXPENSEITEM as ei "
            + "LEFT JOIN EXPENSE as e ON e.id=ei.expense WHERE ei.COMM IN "
            + "(SELECT ID FROM COMMODITY WHERE PARENT IN (SELECT w1.id FROM w1)) "
            + "AND e.MONEYTYPE=(SELECT ID FROM MONEYTYPE WHERE MONEYTYPE.CODE LIKE :moneyCode)", nativeQuery = true)
    @RestResource(path = "sumCommodityGroupExpenses")
    Long sumCommodityGroupExpenses(@Param(value = "commodityGroupId") int commodityGroupId,
                              @Param(value = "moneyCode") String moneyCode);

    @Query(value = "SELECT * FROM ( "
            + "    SELECT  "
            + "        u.NAME AS AGENT,  "
            + "        cgp.NAME AS COMMODITYGROUP, "
            + "        ROUND(SUM(ex.TOTAL * (SELECT * FROM CROSS_RATE(:code, m.CODE, e.TRANSFERDATE))), 2) AS TOTAL, "
            + "        EXTRACT(MONTH FROM e.TRANSFERDATE) AS MOIS, "
            + "        EXTRACT(YEAR FROM e.TRANSFERDATE) AS AN "
            + "    FROM EXPENSEITEM ex  "
            + "    LEFT JOIN EXPENSE e ON ex.EXPENSE = e.ID  "
            + "    LEFT JOIN MONEYTYPE m ON m.ID = e.MONEYTYPE  "
            + "    LEFT JOIN COMMODITY c ON ex.COMM = c.ID  "
            + "    LEFT JOIN COMMGROUP cg ON c.PARENT = cg.ID    "
            + "    LEFT JOIN COMMGROUP cgp ON cg.PARENT = cgp.ID "
            + "    LEFT JOIN USERMT u ON e.USERMT=u.ID "
            + "    GROUP BY u.NAME, cgp.NAME, MOIS, AN ORDER BY AN, MOIS ASC"
            + ") "
            + "WHERE (AN < :anEnd OR (AN = :anEnd AND MOIS <= :moisEnd)) AND "
            + "      (AN > :anStart OR (AN = :anStart AND MOIS >= :moisStart))", nativeQuery = true)
    List<AgentCommodityGroup> getAgentCommodityGroups(@Param(value = "code") String code,
                                                      @Param(value = "moisStart") int moisStart,
                                                      @Param(value = "anStart") int anStart,
                                                      @Param(value = "moisEnd") int moisEnd,
                                                      @Param(value = "anEnd") int anEnd);
}
