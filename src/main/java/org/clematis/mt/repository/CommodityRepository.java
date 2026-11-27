package org.clematis.mt.repository;

import java.util.List;

import org.clematis.mt.model.Commodity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

/**
 * Repository for Commodity data access.
 */
@RepositoryRestResource(path = "commodities")
public interface CommodityRepository extends PagingAndSortingAndFilteringByNameRepository<Commodity, Integer> {
    /**
     * Find all commodities recursively by parent group ID.
     */
    @Query(
        value = """
            SELECT * FROM (WITH RECURSIVE w1(id, parent, name) AS
            (SELECT c.id, c.parent, c.name
                FROM COMMGROUP as c
                WHERE c.id = :id
            UNION ALL
            SELECT c2.id, c2.parent, c2.name
            FROM w1 JOIN COMMGROUP as c2 ON c2.parent=w1.id
            )
            SELECT * FROM COMMODITY WHERE PARENT IN (SELECT w1.id FROM w1))""",
        countQuery = """
            SELECT COUNT(*) FROM (WITH RECURSIVE w1(id, parent, name) AS
            (SELECT c.id, c.parent, c.name
                FROM COMMGROUP as c
                WHERE c.id = :id
            UNION ALL
            SELECT c2.id, c2.parent, c2.name
            FROM w1 JOIN COMMGROUP as c2 ON c2.parent=w1.id
            )
            SELECT * FROM COMMODITY WHERE PARENT IN (SELECT w1.id FROM w1))""",
        nativeQuery = true)
    @RestResource(path = "recursiveCommoditiesByGroupId")
    Page<Commodity> findCommoditiesRecursiveByGroupId(@Param("id") Integer id, Pageable pageable);

    /**
     * Retrieves the latest purchase price point for each organization
     * for a given commodity.
     */
    @RestResource(exported = false)
    @Query(
        value = """
            SELECT o.NAME AS tradeplaceName,
                   o.ID AS tradeplaceId,
                   item.TRANSFERDATE AS transferDate,
                   item.PRICE AS price,
                   m.CODE AS currencyCode
            FROM EXPENSEITEM item
            JOIN COMMODITY c ON c.ID = item.COMM
            JOIN ORGANIZATION o ON o.ID = item.TRADEPLACE
            JOIN EXPENSE ex ON ex.ID = item.EXPENSE
            JOIN MONEYTYPE m ON m.ID = ex.MONEYTYPE
            JOIN (
                SELECT COMM, TRADEPLACE, MAX(TRANSFERDATE) AS maxDate
                FROM EXPENSEITEM
                WHERE COMM = :commodityId
                GROUP BY COMM, TRADEPLACE
            ) latest ON latest.COMM = item.COMM
                     AND latest.TRADEPLACE = item.TRADEPLACE
                     AND latest.maxDate = item.TRANSFERDATE
            JOIN (
                SELECT COMM, TRADEPLACE, TRANSFERDATE, MAX(ID) AS maxId
                FROM EXPENSEITEM
                WHERE COMM = :commodityId
                GROUP BY COMM, TRADEPLACE, TRANSFERDATE
            ) dedup ON dedup.COMM = item.COMM
                     AND dedup.TRADEPLACE = item.TRADEPLACE
                     AND dedup.TRANSFERDATE = item.TRANSFERDATE
                     AND dedup.maxId = item.ID
            WHERE c.ID = :commodityId
            ORDER BY o.NAME ASC
            """,
        nativeQuery = true)
    List<Object[]> findPurchasePricePointsByCommodityId(@Param("commodityId") Integer commodityId);


}
