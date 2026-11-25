package org.clematis.mt.repository;

import java.util.List;

import org.clematis.mt.model.MonthlyDelta;
import org.clematis.mt.model.MonthlyDeltaKey;
import org.clematis.mt.projections.AccountSummaryProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

/**
 * Repository for MonthlyDelta data access.
 */
@RepositoryRestResource(path = "monthlyDeltas")
public interface MonthlyDeltaRepository extends PagingAndSortingRepository<MonthlyDelta, MonthlyDeltaKey> {

    /**
     * Get the total balance for a given currency up to the specified month and year.
     */
    @Query(value =
        """
            SELECT ROUND(COALESCE(SUM(MD.DELTA), 0), 2)
            FROM MONTHLY_DELTA AS MD
            WHERE (MD.AN < :an OR (MD.AN = :an AND MD.MOIS < :mois)) AND MD.CODE = :code
        """, nativeQuery = true)
    @RestResource(path = "balance")
    Long getBalanceForCurrency(@Param(value = "an") int an,
                               @Param(value = "mois") int mois,
                               @Param(value = "code") String code);

    /**
     * Get the summary of monthly deltas for a given account in a given period
     * @param code the currency code
     * @param moisStart the start month
     * @param anStart the start year
     * @param moisEnd the end month
     * @param anEnd the end year
     * @return a list of CurrencySummaryProjections
     */
    @Query(value = """
            SELECT
                s.AN AS "year",
                s.MOIS AS "month",
                s.ACCOUNT_ID AS "accountId",
                s.ACCOUNT_NAME AS "accountName",
                s.CODE AS "code",
                s.DELTA_ORIG AS "deltaOrig",
                s.DELTA_TARGET AS "deltaTarget",
                s.BALANCE_ORIG AS "balanceOrig",
                s.BALANCE_TARGET AS "balanceTarget",
                s.MONTH_TOTAL_DELTA_TARGET AS "monthTotalDeltaTarget",
                s.MONTH_TOTAL_BALANCE_TARGET AS "monthTotalBalanceTarget"
            FROM GET_MONTHLY_ACCOUNT_SUMMARY(
                CAST(:code AS VARCHAR(16)),
                CAST(:moisStart AS INTEGER),
                CAST(:anStart AS INTEGER),
                CAST(:moisEnd AS INTEGER),
                CAST(:anEnd AS INTEGER)
            ) s
            ORDER BY s.AN ASC, s.MOIS ASC, s.ACCOUNT_NAME ASC
        """, nativeQuery = true)
    @RestResource(exported = false)
    List<AccountSummaryProjection> getSummaryInCurrency(
        @Param(value = "code") String code,
        @Param(value = "moisStart") int moisStart,
        @Param(value = "anStart") int anStart,
        @Param(value = "moisEnd") int moisEnd,
        @Param(value = "anEnd") int anEnd);

    /**
     * Find monthly deltas paginated by currency code.
     */
    @RestResource(path = "history")
    @SuppressWarnings("checkstyle:methodname")
    Page<MonthlyDelta> findMonthlyDeltasByKey_Code(@Param(value = "code") String code, Pageable pageable);
}
