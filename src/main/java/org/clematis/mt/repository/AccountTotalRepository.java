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

    @Query(value = "select s.AID as ID, s.NAME, s.CODE, s.TOTAL, "
            + " ROUND(s.TOTAL / (CASE WHEN s.RATE IS NULL THEN "
            + " (CASE WHEN s.RATE_RUB IS NULL THEN 1 ELSE s.RATE_RUB END) ELSE s.RATE END), 2) as BALANCE from  "
            + " (SELECT atl.ID as AID, atl.NAME, atl.CODE, atl.TOTAL, mt.ID, "
            + "       (SELECT first 1 * from (SELECT ROUND(mtr1.RATE/mtr2.RATE, 15) as RATE  "
            + "                        FROM MONEYTYPERATE as mtr1  "
            + "                        LEFT JOIN MONEYTYPERATE as mtr2  "
            + "                         ON mtr1.RATEDATE = mtr2.RATEDATE  "
            + "                             AND mtr1.SELLMONEYTYPE != 1  "
            + "                             AND mtr2.SELLMONEYTYPE != 1  "
            + "                             AND mtr1.SELLMONEYTYPE = mt.ID  "
            + "                             AND mtr2.SELLMONEYTYPE = (SELECT ID FROM MONEYTYPE WHERE CODE=:code)  "
            + "                        WHERE (mtr1.RATE IS NOT NULL AND mtr2.RATE IS NOT NULL) "
            + " ORDER BY mtr1.RATEDATE DESC)  "
            + "                    ) as RATE,  "
            + "       (SELECT first 1 * from (select ROUND(mtr.RATE, 15) as RATE  "
            + "                       FROM MONEYTYPERATE as mtr  "
            + "                       WHERE (mtr.BUYMONEYTYPE = (SELECT ID FROM MONEYTYPE WHERE CODE=:code) "
            + " AND mtr.SELLMONEYTYPE = mt.ID)  "
            + "                       ORDER BY mtr.RATEDATE DESC)  "
            + "                    ) as RATE_RUB  "
            + "    FROM ACCOUNTS_TOTAL_LAST as atl  "
            + "    LEFT JOIN MONEYTYPE as mt on atl.CODE = mt.CODE) as s", nativeQuery = true)
    @RestResource(path = "code")
    List<AccountTotal> getAccountTotals(@Param(value = "code") String code);


    @Query(value = "select ROUND(sum(s.TOTAL / (CASE WHEN s.RATE IS NULL THEN"
            + " (1/(CASE WHEN s.RATE_RUB IS NULL THEN 1 ELSE s.RATE_RUB END)) ELSE s.RATE END)), 2) as BALANCE from  "
            + "(SELECT atl.ID as AID, atl.NAME, atl.CODE, atl.TOTAL, mt.ID,  "
            + "       (SELECT first 1 * from (SELECT ROUND(mtr1.RATE/mtr2.RATE, 15) as RATE  "
            + "                        FROM MONEYTYPERATE as mtr1  "
            + "                        LEFT JOIN MONEYTYPERATE as mtr2  "
            + "                         ON mtr1.RATEDATE = mtr2.RATEDATE  "
            + "                             AND mtr1.SELLMONEYTYPE != 1  "
            + "                             AND mtr2.SELLMONEYTYPE != 1  "
            + "                             AND mtr1.SELLMONEYTYPE = mt.ID  "
            + "                             AND mtr2.SELLMONEYTYPE = (SELECT ID FROM MONEYTYPE WHERE CODE=:code)  "
            + "                        WHERE (mtr1.RATE IS NOT NULL AND mtr2.RATE IS NOT NULL) "
            + " ORDER BY mtr1.RATEDATE DESC)  "
            + "                    ) as RATE,  "
            + "       (SELECT first 1 * from (select ROUND(mtr.RATE, 15) as RATE  "
            + "                       FROM MONEYTYPERATE as mtr  "
            + "                       WHERE (mtr.BUYMONEYTYPE = mt.ID AND mtr.SELLMONEYTYPE = "
            + " (SELECT ID FROM MONEYTYPE WHERE CODE=:code))  "
            + "                       ORDER BY mtr.RATEDATE DESC)  "
            + "                    ) as RATE_RUB  "
            + "    FROM ACCOUNTS_TOTAL_LAST as atl  "
            + "    LEFT JOIN MONEYTYPE as mt on atl.CODE = mt.CODE) as s", nativeQuery = true)
    @RestResource(path = "balance")
    Long getBalanceForCurrency(@Param(value = "code") String code);

}
