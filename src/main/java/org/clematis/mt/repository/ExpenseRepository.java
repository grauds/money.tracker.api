package org.clematis.mt.repository;

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

    @Query(value = "WITH RECURSIVE w1(id, parent, name) AS\n"
            + "(SELECT c.id, c.parent, c.name\n"
            + "    FROM COMMGROUP as c\n"
            + "    WHERE c.id = :commodityGroupId\n"
            + "    UNION ALL\n"
            + "    SELECT c2.id, c2.parent, c2.name\n"
            + "    FROM w1 JOIN COMMGROUP as c2 ON c2.parent=w1.id\n"
            + ")\n"
            + "SELECT SUM(ei.TOTAL) FROM EXPENSEITEM as ei "
            + "LEFT JOIN EXPENSE as e ON e.id=ei.expense WHERE ei.COMM IN "
            + "(SELECT ID FROM COMMODITY WHERE PARENT IN (SELECT w1.id FROM w1)) "
            + "AND e.MONEYTYPE=(SELECT ID FROM MONEYTYPE WHERE MONEYTYPE.CODE LIKE :moneyCode)", nativeQuery = true)
    @RestResource(path = "sumCommodityGroupExpenses")
    Long sumCommodityGroupExpenses(@Param(value = "commodityGroupId") int commodityGroupId,
                              @Param(value = "moneyCode") String moneyCode);
}
