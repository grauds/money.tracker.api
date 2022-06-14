package org.clematis.mt.repository;

import java.util.List;

import org.clematis.mt.model.CommodityGroup;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

/**
 * @author Anton Troshin
 */
@RepositoryRestResource(path = "commodityGroups")
public interface CommodityGroupRepository
        extends PagingAndSortingAndFilteringByNameRepository<CommodityGroup, Integer> {

    @Query(
        value = "WITH RECURSIVE w1(id, parent, name) AS\n"
              + "(SELECT c.id, c.parent, c.name\n"
              + "    FROM COMMGROUP as c\n"
              + "    WHERE c.id = :id\n"
              + "\tUNION ALL\n"
              + "\tSELECT c2.id, c2.parent, c2.name\n"
              + "\tFROM w1 JOIN COMMGROUP as c2 ON c2.parent=w1.id\n"
              + ")\n"
              + "SELECT * FROM w1 ORDER BY name",
        nativeQuery = true
    )
    @RestResource(path = "recursiveByParentId")
    List<CommodityGroup> findRecursiveByParentId(@Param("id") Integer id);
}
