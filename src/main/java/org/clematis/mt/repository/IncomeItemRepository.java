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


@RepositoryRestResource(path = "incomeItems", excerptProjection = IncomeItemEntry.class)
public interface IncomeItemRepository extends JpaRepository<IncomeItem, Integer> {

    @RestResource(path = "commodity")
    Page<IncomeItem> findByCommodityId(@Param(value = "id") int commodityId, Pageable pageable);

    @RestResource(path = "tradeplace")
    Page<IncomeItem> findByTradeplaceId(@Param(value = "id") int tradeplaceId, Pageable pageable);

    @RestResource(path = "filtered")
    Page<IncomeItem> findByTransferdateGreaterThanEqualAndTransferdateLessThanEqual(
        @RequestParam(value = "startDate", required = false)
        @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
        @RequestParam(value = "endDate", required = false)
        @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
        Pageable pageable
    );

    @Query(value = """
        SELECT SUM(ei.total /  NULLIF((SELECT * FROM CROSS_RATE(m.code, :moneyCode, e.TRANSFERDATE)), 0))
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

    @Query(value = """
        SELECT SUM(ei.total / NULLIF((SELECT RATE FROM CROSS_RATE(m.code, :moneyCode, e.TRANSFERDATE)), 0))
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
            SELECT SUM(ei.TOTAL / NULLIF((SELECT RATE FROM CROSS_RATE(m.code, :moneyCode, e.TRANSFERDATE)), 0))
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
            SELECT SUM(ei.TOTAL / NULLIF((SELECT RATE FROM CROSS_RATE(m.code, :moneyCode, e.TRANSFERDATE)), 0))
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

    @Query(value = """
        SELECT SUM(ei.qty)
            FROM IncomeItem as ei
            LEFT JOIN Income as e ON e.id = ei.income.id
        WHERE ei.commodity.id=:id
        """
    )
    @RestResource(path = "sumCommodityQuantity")
    Long sumCommodityQuantity(@Param(value = "id") int id);


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
