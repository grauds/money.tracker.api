package org.clematis.mt.repository;

import java.util.List;

import org.clematis.mt.model.AccountTotal;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
/**
 * @author Anton Troshin
 */
@RepositoryRestResource(path = "accountsTotals")
public interface AccountTotalRepository extends ReadOnlyRepository<AccountTotal, Integer> {

    @Query(value = """
            select atl.ID, atl.NAME, atl.CODE, atl.TOTAL,
            ROUND(atl.TOTAL / (SELECT * FROM CROSS_RATE(mt.CODE, :code)), 2) as BALANCE
            FROM ACCOUNTS_TOTAL_LAST as atl LEFT JOIN MONEYTYPE as mt on atl.CODE = mt.CODE
            ORDER BY BALANCE
        """, nativeQuery = true)
    @RestResource(path = "code")
    List<AccountTotal> getAccountTotals(@Param(value = "code") String code);


    @Query(value = """
            select ROUND(sum(atl.TOTAL / (SELECT * FROM CROSS_RATE(mt.CODE, :code))), 2) as BALANCE  \
            FROM ACCOUNTS_TOTAL_LAST as atl LEFT JOIN MONEYTYPE as mt on atl.CODE = mt.CODE
        """, nativeQuery = true)
    @RestResource(path = "balance")
    Long getBalanceForCurrency(@Param(value = "code") String code);


    @Query(//https://github.com/FirebirdSQL/firebird/issues/5668
        value = """
            SELECT ROUND(sum(atl.TOTAL / (SELECT * FROM CROSS_RATE(mt.CODE, :code))), 2) as BALANCE
            FROM (
                   SELECT ACCOUNT as ID, acc.NAME, BALANCE, ROUND(TOTAL, 2) as TOTAL, CODE
                   FROM (SELECT ACCOUNT, SUM (CASE WHEN MONEY_SIGN = 1
                                             THEN AMOUNT
                                         ELSE AMOUNT * (-1) END
                                    ) AS TOTAL
                         FROM (
                                  SELECT *
                                  FROM ALL_OPERATIONS_HISTORY\
                                  WHERE ALL_OPERATIONS_HISTORY.TRANSFERDATE
            < dateadd (cast(:days as integer) * (-1) day to current_date)
                              )
                         GROUP BY ACCOUNT)
                            JOIN ACCOUNT AS acc ON acc.ID = ACCOUNT
                            JOIN MONEYTYPE AS mt ON acc.MONEYTYPE = mt.ID
                   ORDER BY TOTAL
               ) as atl LEFT JOIN MONEYTYPE as mt on atl.CODE = mt.CODE
            """, nativeQuery = true)
    @RestResource(path = "balanceHistory")
    Long getBalanceForCurrency(@Param(value = "code") String code, @Param(value = "days") int days);

}
