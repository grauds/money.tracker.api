package org.clematis.mt.repository;

import java.util.List;

import org.clematis.mt.dto.NamedEntityLink;
import org.clematis.mt.model.CommodityGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
        value = """
            SELECT * FROM (WITH RECURSIVE w1(id, parent, name) AS
            (SELECT c.id, c.parent, c.name
                FROM COMMGROUP as c
                WHERE c.id = :id
            UNION ALL
            SELECT c2.id, c2.parent, c2.name
            FROM w1 JOIN COMMGROUP as c2 ON c2.parent=w1.id
            )
            SELECT * FROM w1 WHERE w1.id <> :id ORDER BY name)""",
        countQuery = """
            SELECT COUNT(*) FROM (WITH RECURSIVE w1(id, parent, name) AS
            (SELECT c.id, c.parent, c.name
                FROM COMMGROUP as c
                WHERE c.id = :id
            UNION ALL
            SELECT c2.id, c2.parent, c2.name
            FROM w1 JOIN COMMGROUP as c2 ON c2.parent=w1.id
            )
            SELECT * FROM w1 WHERE w1.id <> :id ORDER BY name)""",
        nativeQuery = true
    )
    @RestResource(path = "recursiveByParentId")
    Page<CommodityGroup> findRecursiveByParentId(@Param("id") Integer id, Pageable pageable);

    @Query(
        value = """
            SELECT * FROM (WITH RECURSIVE w1(id, type, parent, name) AS
             (SELECT c.id, 0 as type, c.parent, CAST(c.name as VARCHAR(128))
                 FROM COMMGROUP as c
                 WHERE c.id = :id
             UNION ALL
             SELECT c2.id, 0 as type, c2.parent, CAST(c2.name as VARCHAR(128))
                 FROM w1 JOIN COMMGROUP as c2 ON c2.PARENT=w1.id
                 WHERE c2.id <> :id
             UNION ALL
             SELECT c3.id, 1 as type, c3.parent, c3.name
                 FROM w1 JOIN COMMODITY as c3 ON c3.PARENT=w1.id
            )
            SELECT * FROM w1)""",
        countQuery = """
            SELECT COUNT(*) FROM (WITH RECURSIVE w1(id, type, parent, name) AS
             (SELECT c.id, 0 as type, c.parent, CAST(c.name as VARCHAR(128))
                 FROM COMMGROUP as c
                 WHERE c.id = :id
             UNION ALL
             SELECT c2.id, 0 as type, c2.parent, CAST(c2.name as VARCHAR(128))
                 FROM w1 JOIN COMMGROUP as c2 ON c2.PARENT=w1.id
                 WHERE c2.id <> :id
             UNION ALL
             SELECT c3.id, 1 as type, c3.parent, c3.name
                 FROM w1 JOIN COMMODITY as c3 ON c3.PARENT=w1.id
            )
            SELECT * FROM w1)""",
        nativeQuery = true
    )
    @Deprecated(since = "This won't work: https://github.com/spring-projects/spring-data-rest/issues/1213")
    @RestResource(path = "recursiveWithCommoditiesByParentId")
    Page<NamedEntityLink> getCommodityGroupChildren(@Param("id") Integer id, Pageable pageable);

    @Query(
            value = """
                SELECT * FROM (WITH RECURSIVE w1(id, parent, name) AS
                (SELECT c.id, c.parent, c.name
                    FROM COMMGROUP as c
                    WHERE c.id = :id
                UNION ALL
                SELECT c2.id, c2.parent, c2.name
                FROM w1 JOIN COMMGROUP as c2 ON c2.id=w1.parent
                )
                SELECT * FROM w1 WHERE w1.id <> :id)""",
            nativeQuery = true
    )
    @RestResource(path = "pathById")
    List<CommodityGroup> findPathById(@Param("id") Integer id);
}

