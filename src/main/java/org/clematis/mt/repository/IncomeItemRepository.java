package org.clematis.mt.repository;

import java.util.Date;

import org.clematis.mt.dto.IncomeMonthlyReport;
import org.clematis.mt.model.IncomeItem;
import org.clematis.mt.model.IncomeItemEntry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Anton Troshin
 */
@RepositoryRestResource(path = "incomeItems", excerptProjection = IncomeItemEntry.class)
public interface IncomeItemRepository extends PagingAndSortingRepository<IncomeItem, Integer> {

    @RestResource(path = "commodity")
    Page<IncomeItem> findByCommodityId(@Param(value = "commodityId") int commodityId, Pageable pageable);

    @RestResource(path = "tradeplace")
    Page<IncomeItem> findByTradeplaceId(@Param(value = "tradeplaceId") int tradeplaceId, Pageable pageable);

    @RestResource(path = "filtered")
    Page<IncomeItem> findByTransferdateGreaterThanEqualAndTransferdateLessThanEqual(
        @RequestParam(value = "startDate", required = false)
        @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
        @RequestParam(value = "endDate", required = false)
        @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
        Pageable pageable
    );

    @Query(value = """
            SELECT SUM(ei.total)
            FROM IncomeItem as ei LEFT JOIN Income as e ON e.id=ei.income.id
            WHERE ei.commodity.id=:commodityId
            AND e.moneyType.code LIKE :moneyCode
        """)
    @RestResource(path = "sumCommodityIncome")
    Double sumCommodityIncome(@Param(value = "commodityId") int commodityId,
                                @Param(value = "moneyCode") String moneyCode);

    @Query(value = """
            SELECT SUM(ei.total)
            FROM IncomeItem as ei LEFT JOIN Income as e ON e.id=ei.income.id
            WHERE ei.tradeplace.id=:organizationId
            AND e.moneyType.code LIKE :moneyCode
        """)
    @RestResource(path = "sumOrganizationIncome")
    Double sumOrganizationIncome(@Param(value = "organizationId") int organizationId,
                                   @Param(value = "moneyCode") String moneyCode);

    @Query(value = """
            SELECT SUM(ei.qty)
            FROM IncomeItem as ei
            LEFT JOIN Income as e ON e.id=ei.income.id WHERE ei.commodity.id=:commodityId
        """)
    @RestResource(path = "sumCommodityQuantity")
    Long sumCommodityQuantity(@Param(value = "commodityId") int commodityId);
    
    @Query(value =
        """
            SELECT * FROM (SELECT ROUND(SUM(INCOMEITEM.TOTAL *
             (SELECT * FROM CROSS_RATE(:code, M.CODE, INCOMEITEM.TRANSFERDATE))), 2) AS TOTAL,
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
             (SELECT * FROM CROSS_RATE(:code, M.CODE, INCOMEITEM.TRANSFERDATE))), 2) AS TOTAL,
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
