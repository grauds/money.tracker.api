package org.clematis.mt.repository;

import java.util.List;

import org.clematis.mt.dto.EntityAmountProjection;
import org.clematis.mt.dto.MoneyShareInCurrency;
import org.clematis.mt.model.Income;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;


/**
 * Repository for income data access with Firebird database queries.
 */
@RepositoryRestResource(path = "income")
public interface IncomeRepository extends PagingAndSortingRepository<Income, Integer> {

    /**
     * Calculates total income converted to the specified currency.
     * @param code target currency code
     * @return total income in the specified currency
     */
    @RestResource(exported = false)
    @Query(value = """
            SELECT COALESCE(SUM(TOTALCONVERTED), 0)
            FROM GET_INCOME_BY_CURRENCY(:code)
        """, nativeQuery = true)
    Double sumTotalInCurrency(@Param("code") String code);

    /**
     * Retrieves income breakdown by currency with converted totals.
     * @param code target currency code
     * @return income shares per currency
     */
    @RestResource(exported = false)
    @Query(value = """
            SELECT CODE AS code, TOTAL AS total, TOTALCONVERTED AS totalConverted
            FROM GET_INCOME_BY_CURRENCY(:code)
        """, nativeQuery = true)
    List<MoneyShareInCurrency> getIncomeByCurrency(@Param("code") String code);

    /**
     * Retrieves income breakdown by organizations.
     * @param code target currency code
     * @return income shares per organization
     */
    @RestResource(exported = false)
    @Query(value = """
            SELECT *
            FROM GET_INCOME_BY_TRADE_PLACE(:code)
            ORDER BY TOTAL DESC
        """, nativeQuery = true)
    List<EntityAmountProjection> getIncomeByOrganizations(@Param("code") String code);

}
