package org.clematis.mt.repository;

import org.clematis.mt.model.Commodity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

/**
 * @author Anton Troshin
 */
@RepositoryRestResource(path = "commodities")
public interface CommodityRepository extends PagingAndSortingAndFilteringByNameRepository<Commodity, Integer> {
    @Query(
        value = "SELECT * FROM (WITH RECURSIVE w1(id, parent, name) AS\n"
            + "(SELECT c.id, c.parent, c.name\n"
            + "    FROM COMMGROUP as c\n"
            + "    WHERE c.id = :id\n"
            + "UNION ALL\n"
            + "SELECT c2.id, c2.parent, c2.name\n"
            + "FROM w1 JOIN COMMGROUP as c2 ON c2.parent=w1.id\n"
            + ")\n"
            + "SELECT * FROM COMMODITY WHERE PARENT IN (SELECT w1.id FROM w1))",
        countQuery = "SELECT COUNT(*) FROM (WITH RECURSIVE w1(id, parent, name) AS\n"
            + "(SELECT c.id, c.parent, c.name\n"
            + "    FROM COMMGROUP as c\n"
            + "    WHERE c.id = :id\n"
            + "UNION ALL\n"
            + "SELECT c2.id, c2.parent, c2.name\n"
            + "FROM w1 JOIN COMMGROUP as c2 ON c2.parent=w1.id\n"
            + ")\n"
            + "SELECT * FROM COMMODITY WHERE PARENT IN (SELECT w1.id FROM w1))",
        nativeQuery = true)
    @RestResource(path = "recursiveCommoditiesByGroupId")
    Page<Commodity> findCommoditiesRecursiveByGroupId(@Param("id") Integer id, Pageable pageable);
}
