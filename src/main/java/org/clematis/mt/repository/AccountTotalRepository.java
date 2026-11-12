package org.clematis.mt.repository;

import java.util.List;

import org.clematis.mt.model.AccountTotal;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;


/**
 * Repository for account total data access with Firebird database queries.
 * Provides methods for retrieving account totals and balances by currency.
 */
@RepositoryRestResource(path = "accountsTotals")
public interface AccountTotalRepository extends ReadOnlyRepository<AccountTotal, Integer> {

    /**
     * Retrieves all account totals with balances converted to the specified currency.
     *
     * @param code target currency code for conversion
     * @return list of account totals with converted balances, ordered by balance
     */
    @Query(value = """
            select atl.ID, atl.NAME, atl.CODE, atl.TOTAL,
            ROUND(atl.TOTAL / (SELECT * FROM CROSS_RATE_2(mt.CODE, :code)), 2) as BALANCE
            FROM ACCOUNTS_TOTAL_LAST as atl LEFT JOIN MONEYTYPE as mt on atl.CODE = mt.CODE
            ORDER BY BALANCE
        """, nativeQuery = true)
    @RestResource(path = "code")
    List<AccountTotal> getAccountTotals(@Param(value = "code") String code);


    /**
     * Retrieves the total balance across all accounts converted to the specified currency.
     *
     * @param code target currency code for conversion
     * @return total balance in the specified currency
     */
    @Query(value = """
            select ROUND(sum(atl.TOTAL / (SELECT * FROM CROSS_RATE_2(mt.CODE, :code))), 2) as BALANCE  \
            FROM ACCOUNTS_TOTAL_LAST as atl LEFT JOIN MONEYTYPE as mt on atl.CODE = mt.CODE
        """, nativeQuery = true)
    @RestResource(path = "balance")
    Long getBalanceForCurrency(@Param(value = "code") String code);


    /**
     * Retrieves the historical total balance for accounts within a specified number of days ago,
     * converted to the target currency.
     *
     * @param code target currency code for conversion
     * @param days number of days back from current date to calculate historical balance
     * @return historical total balance in the specified currency
     */
    @Query(//https://github.com/FirebirdSQL/firebird/issues/5668
        value = """
            SELECT ROUND(sum(atl.TOTAL / (SELECT * FROM CROSS_RATE_2(mt.CODE, :code))), 2) as BALANCE
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
