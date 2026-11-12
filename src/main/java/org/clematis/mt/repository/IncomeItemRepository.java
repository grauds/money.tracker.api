package org.clematis.mt.repository;

import java.util.Date;

import org.clematis.mt.dto.IncomeMonthlyReport;
import org.clematis.mt.model.IncomeItem;
import org.clematis.mt.model.IncomeItemEntry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * Repository for income item data access with Firebird database queries.
 * Provides methods for retrieving income items by commodity, organization, account,
 * and date range, as well as aggregate income calculations by various dimensions.
 */
@RepositoryRestResource(path = "incomeItems", excerptProjection = IncomeItemEntry.class)
public interface IncomeItemRepository extends JpaRepository<IncomeItem, Integer> {

    /**
     * Retrieves income items filtered by commodity ID.
     *
     * @param commodityId the commodity identifier
     * @param pageable pagination information
     * @return page of income items for the specified commodity
     */
    @RestResource(path = "commodity")
    Page<IncomeItem> findByCommodityId(@Param(value = "id") int commodityId, Pageable pageable);

    /**
     * Retrieves income items filtered by tradeplace (organization) ID.
     *
     * @param tradeplaceId the tradeplace identifier
     * @param pageable pagination information
     * @return page of income items for the specified tradeplace
     */
    @RestResource(path = "tradeplace")
    Page<IncomeItem> findByTradeplaceId(@Param(value = "id") int tradeplaceId, Pageable pageable);

    /**
     * Retrieves income items filtered by income account ID.
     *
     * @param accountId the account identifier
     * @param pageable pagination information
     * @return page of income items for the specified account
     */
    @RestResource(path = "account")
    Page<IncomeItem> findByIncomeAccountId(@Param(value = "id") int accountId, Pageable pageable);

    /**
     * Retrieves income items within the specified date range.
     *
     * @param startDate start date (inclusive), if null no lower bound is applied
     * @param endDate end date (inclusive), if null no upper bound is applied
     * @param pageable pagination information
     * @return page of income items within the specified date range
     */
    @RestResource(path = "filtered")
    Page<IncomeItem> findByTransferdateGreaterThanEqualAndTransferdateLessThanEqual(
        @RequestParam(value = "startDate", required = false)
        @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
        @RequestParam(value = "endDate", required = false)
        @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
        Pageable pageable
    );

    /**
     * Calculates the total daily income converted to the specified currency.
     *
     * @param targetDate the date for which to calculate total income
     * @param moneyCode the target currency code
     * @return total income on the specified date converted to the target currency
     */
    @Query(value = """
        SELECT SUM(ei.total / NULLIF((SELECT RATE FROM CROSS_RATE_2(m.code, :moneyCode, e.TRANSFERDATE)), 0))
            FROM IncomeItem as ei
                 JOIN Income as e ON e.id = ei.income
                 JOIN MoneyType m ON m.id = e.moneyType
            WHERE CAST(e.TRANSFERDATE AS DATE) = :targetDate
        """, nativeQuery = true)
    @RestResource(path = "sumDailyIncome")
    Double sumDailyIncome(
        @Param(value = "targetDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date targetDate,
        @Param(value = "moneyCode") String moneyCode
    );

    /**
     * Calculates the total income for a specific commodity converted to the specified currency.
     *
     * @param id the commodity identifier
     * @param moneyCode the target currency code
     * @return total income for the commodity converted to the target currency
     */
    @Query(value = """
        SELECT SUM(ei.total /  NULLIF((SELECT * FROM CROSS_RATE_2(m.code, :moneyCode, e.TRANSFERDATE)), 0))
            FROM IncomeItem as ei
                JOIN Income as e ON e.id=ei.income
                JOIN MoneyType m ON m.id = e.moneyType
                WHERE ei.COMM=:id
        """, nativeQuery = true)
    @RestResource(path = "sumCommodityIncome")
    Double sumCommodityIncome(
        @Param(value = "id") int id,
        @Param(value = "moneyCode") String moneyCode
    );

    /**
     * Calculates the total income for a specific organization (tradeplace) converted to the specified currency.
     *
     * @param id the organization identifier
     * @param moneyCode the target currency code
     * @return total income for the organization converted to the target currency
     */
    @Query(value = """
        SELECT SUM(ei.total / NULLIF((SELECT RATE FROM CROSS_RATE_2(m.code, :moneyCode, e.TRANSFERDATE)), 0))
            FROM IncomeItem as ei
                 JOIN Income as e ON e.id = ei.income
                 JOIN MoneyType m ON m.id = e.moneyType
            WHERE ei.tradeplace = :id
        """, nativeQuery = true)
    @RestResource(path = "sumOrganizationIncome")
    Double sumOrganizationIncome(
        @Param(value = "id") int id,
        @Param(value = "moneyCode") String moneyCode
    );

    /**
     * Calculates the total income for a specific account converted to the specified currency.
     *
     * @param id the account identifier
     * @param moneyCode the target currency code
     * @return total income for the account converted to the target currency
     */
    @Query(value = """
        SELECT SUM(ei.total /  NULLIF((SELECT * FROM CROSS_RATE_2(m.code, :moneyCode, e.TRANSFERDATE)), 0))
            FROM IncomeItem as ei
                JOIN Income as e ON e.id=ei.income
                JOIN MoneyType m ON m.id = e.moneyType
                WHERE e.ACCOUNT=:id
        """, nativeQuery = true)
    @RestResource(path = "sumAccountIncome")
    Double sumAccountIncome(
        @Param(value = "id") int id,
        @Param(value = "moneyCode") String moneyCode
    );

    /**
     * Calculates the total income for a commodity group (including all subgroups) converted to the specified currency.
     *
     * @param id the commodity group identifier
     * @param moneyCode the target currency code
     * @return total income for the commodity group and all subgroups converted to the target currency
     */
    @Query(value = """
            WITH RECURSIVE w1(id, parent, name) AS
            (
                SELECT c.id, c.parent, c.name
                FROM COMMGROUP as c
                WHERE c.id = :id
                UNION ALL
                SELECT c2.id, c2.parent, c2.name
                FROM w1 JOIN COMMGROUP as c2 ON c2.parent = w1.id
            )
            SELECT SUM(ei.TOTAL / NULLIF((SELECT RATE FROM CROSS_RATE_2(m.code, :moneyCode, e.TRANSFERDATE)), 0))
            FROM INCOMEITEM as ei
                 JOIN INCOME as e ON e.id = ei.INCOME
                 JOIN MONEYTYPE m ON m.id = e.moneyType
            WHERE ei.COMM IN (
                SELECT ID FROM COMMODITY WHERE PARENT IN (SELECT w1.id FROM w1)
            )
         """, nativeQuery = true)
    @RestResource(path = "sumCommodityGroupIncome")
    Long sumCommodityGroupIncome(@Param(value = "id") int id,
                                 @Param(value = "moneyCode") String moneyCode);

    /**
     * Calculates the total income for an organization group (including all subgroups)
     * converted to the specified currency.
     *
     * @param id the organization group identifier
     * @param moneyCode the target currency code
     * @return total income for the organization group and all subgroups converted to the target currency
     */
    @Query(value = """
            WITH RECURSIVE w1(id, parent, name) AS
            (
                SELECT c.id, c.parent, c.name
                FROM ORGANIZATIONGROUP as c
                WHERE c.id = :id
                UNION ALL
                SELECT c2.id, c2.parent, c2.name
                FROM w1 JOIN ORGANIZATIONGROUP as c2 ON c2.parent = w1.id
            )
            SELECT SUM(ei.TOTAL / NULLIF((SELECT RATE FROM CROSS_RATE_2(m.code, :moneyCode, e.TRANSFERDATE)), 0))
            FROM INCOMEITEM as ei
                 JOIN INCOME as e ON e.id = ei.INCOME
                 JOIN MONEYTYPE m ON m.id = e.moneyType
            WHERE ei.TRADEPLACE IN (
                SELECT ID FROM ORGANIZATION WHERE PARENT IN (SELECT w1.id FROM w1)
            )
         """, nativeQuery = true)
    @RestResource(path = "sumOrganizationGroupIncome")
    Long sumOrganizationGroupIncome(@Param(value = "id") int id,
                                    @Param(value = "moneyCode") String moneyCode);

    /**
     * Calculates the total income for an account group (including all subgroups) converted to the specified currency.
     *
     * @param id the account group identifier
     * @param moneyCode the target currency code
     * @return total income for the account group and all subgroups converted to the target currency
     */
    @Query(value = """
            WITH RECURSIVE w1(id, parent, name) AS
            (
                SELECT c.id, c.parent, c.name
                FROM ACCOUNTGROUP as c
                WHERE c.id = :id
                UNION ALL
                SELECT c2.id, c2.parent, c2.name
                FROM w1 JOIN ACCOUNTGROUP as c2 ON c2.parent = w1.id
            )
            SELECT SUM(ei.TOTAL / NULLIF((SELECT RATE FROM CROSS_RATE_2(m.code, :moneyCode, e.TRANSFERDATE)), 0))
            FROM INCOMEITEM as ei
                 JOIN INCOME as e ON e.id = ei.INCOME
                 JOIN MONEYTYPE m ON m.id = e.moneyType
            WHERE e.ACCOUNT IN (
                SELECT ID FROM ACCOUNT WHERE PARENT IN (SELECT w1.id FROM w1)
            )
         """, nativeQuery = true)
    @RestResource(path = "sumAccountGroupIncome")
    Long sumAccountGroupIncome(@Param(value = "id") int id,
                               @Param(value = "moneyCode") String moneyCode);

    /**
     * Calculates the total quantity of a specific commodity from income items.
     *
     * @param id the commodity identifier
     * @return total quantity of the commodity from income items
     */
    @Query(value = """
        SELECT SUM(ei.qty)
            FROM IncomeItem as ei
            LEFT JOIN Income as e ON e.id = ei.income.id
        WHERE ei.commodity.id=:id
        """
    )
    @RestResource(path = "sumCommodityQuantity")
    Long sumCommodityQuantity(@Param(value = "id") int id);


    /**
     * Retrieves monthly income reports grouped by commodity, converted to the specified currency.
     *
     * @param code target currency code for conversion
     * @param pageable pagination information
     * @return page of monthly income reports
     * @deprecated This method is deprecated due to a Spring Data REST issue
     */
    @Query(value =
        """
            SELECT * FROM (SELECT ROUND(SUM(INCOMEITEM.TOTAL *
             (SELECT * FROM CROSS_RATE_2(:code, M.CODE, INCOMEITEM.TRANSFERDATE))), 2) AS TOTAL,
                EXTRACT(YEAR FROM INCOMEITEM.TRANSFERDATE) as AN,
                EXTRACT(MONTH FROM INCOMEITEM.TRANSFERDATE) as MOIS,
                C.NAME AS COMMODITY
             FROM INCOMEITEM
                     LEFT JOIN INCOME I on INCOMEITEM.INCOME = I.ID
                     LEFT JOIN COMMODITY C on INCOMEITEM.COMM = C.ID
                     LEFT JOIN ACCOUNT A on I.ACCOUNT = A.ID
                     LEFT JOIN MONEYTYPE M on I.MONEYTYPE = M.ID
             GROUP BY AN, MOIS, COMMODITY)""",
        countQuery = """
            SELECT COUNT(*) FROM (SELECT ROUND(SUM(INCOMEITEM.TOTAL *
             (SELECT * FROM CROSS_RATE_2(:code, M.CODE, INCOMEITEM.TRANSFERDATE))), 2) AS TOTAL,
                EXTRACT(YEAR FROM INCOMEITEM.TRANSFERDATE) as AN,
                EXTRACT(MONTH FROM INCOMEITEM.TRANSFERDATE) as MOIS,
                C.NAME AS COMMODITY
             FROM INCOMEITEM
                     LEFT JOIN INCOME I on INCOMEITEM.INCOME = I.ID
                     LEFT JOIN COMMODITY C on INCOMEITEM.COMM = C.ID
                     LEFT JOIN ACCOUNT A on I.ACCOUNT = A.ID
                     LEFT JOIN MONEYTYPE M on I.MONEYTYPE = M.ID
             GROUP BY AN, MOIS, COMMODITY)""",
        nativeQuery = true)
    @Deprecated(since = "This won't work: https://github.com/spring-projects/spring-data-rest/issues/1213")
    @RestResource(path = "report")
    Page<IncomeMonthlyReport> getIncomeItemReports(@Param("code") String code, Pageable pageable);

}
