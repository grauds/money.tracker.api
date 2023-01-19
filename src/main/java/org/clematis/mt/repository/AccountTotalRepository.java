package org.clematis.mt.repository;

import java.util.List;

import org.clematis.mt.model.views.AccountTotal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

/**
 * @author Anton Troshin
 */
@RepositoryRestResource(path = "accountsTotals")
public interface AccountTotalRepository extends JpaRepository<AccountTotal, Integer> {

    @Query(value = "select atl.ID, atl.NAME, atl.CODE, atl.TOTAL, mt.ID, "
            + "ROUND(atl.TOTAL / CASE WHEN M.RATE IS NULL THEN 1 ELSE M.RATE END, 2) as BALANCE "
            + "FROM ACCOUNTS_TOTAL_LAST as atl "
            + "LEFT JOIN MONEYTYPE as mt on atl.CODE = mt.CODE "
            + "LEFT JOIN MONEYTYPERATE M on mt.ID = M.SELLMONEYTYPE "
            + "and M.BUYMONEYTYPE = (SELECT ID FROM MONEYTYPE WHERE CODE=:code) "
            + "and M.RATEDATE = (SELECT MAX(c.RATEDATE) "
            + "FROM MONEYTYPERATE as c WHERE c.BUYMONEYTYPE = (SELECT ID FROM MONEYTYPE WHERE CODE=:code) "
            + "AND c.SELLMONEYTYPE = mt.ID) "
            + "ORDER BY BALANCE", nativeQuery = true)
    @RestResource(path = "code")
    List<AccountTotal> getAccountTotals(@Param(value = "code") String code);


    @Query(value = "select ROUND(sum(atl.TOTAL / CASE WHEN M.RATE IS NULL THEN 1 ELSE M.RATE END), 2) "
            + "FROM ACCOUNTS_TOTAL_LAST as atl "
            + "    LEFT JOIN MONEYTYPE as mt on atl.CODE = mt.CODE "
            + "    LEFT JOIN MONEYTYPERATE M on mt.ID = M.SELLMONEYTYPE "
            + "                             and M.BUYMONEYTYPE = (SELECT ID FROM MONEYTYPE WHERE "
            + "ID=(SELECT ID FROM MONEYTYPE WHERE CODE=:code)) "
            + "                             and M.RATEDATE = (SELECT MAX(c.RATEDATE) FROM MONEYTYPERATE as c "
            + "                                     WHERE c.BUYMONEYTYPE = (SELECT ID FROM MONEYTYPE WHERE "
            + "ID=(SELECT ID FROM MONEYTYPE WHERE CODE=:code)) "
            + "                                     AND c.SELLMONEYTYPE = mt.ID)", nativeQuery = true)
    @RestResource(path = "balance")
    Long getBalanceForCurrency(@Param(value = "code") String code);

}
