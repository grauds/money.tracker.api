package org.clematis.mt.repository;

import java.util.List;

import org.clematis.mt.model.OrganizationGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

/**
 * @author Anton Troshin
 */
@RepositoryRestResource(path = "organizationGroups")
public interface OrganizationGroupRepository
        extends PagingAndSortingAndFilteringByNameRepository<OrganizationGroup, Integer> {

    @Query(
            value = """
                SELECT * FROM (WITH RECURSIVE w1(id, parent, name) AS
                (SELECT c.id, c.parent, c.name
                    FROM ORGANIZATIONGROUP as c
                    WHERE c.id = :id
                UNION ALL
                SELECT c2.id, c2.parent, c2.name
                FROM w1 JOIN ORGANIZATIONGROUP as c2 ON c2.parent=w1.id
                )
                SELECT * FROM w1 WHERE w1.id <> :id ORDER BY name)""",
            countQuery = """
                SELECT COUNT(*) FROM (WITH RECURSIVE w1(id, parent, name) AS
                (SELECT c.id, c.parent, c.name
                    FROM ORGANIZATIONGROUP as c
                    WHERE c.id = :id
                UNION ALL
                SELECT c2.id, c2.parent, c2.name
                FROM w1 JOIN ORGANIZATIONGROUP as c2 ON c2.parent=w1.id
                )
                SELECT * FROM w1 WHERE w1.id <> :id ORDER BY name)""",
            nativeQuery = true
    )
    @RestResource(path = "recursiveByParentId")
    Page<OrganizationGroup> findRecursiveByParentId(@Param("id") Integer id, Pageable pageable);

    @Query(
            value = """
                WITH RECURSIVE w1(id, parent, name) AS
                (
                    SELECT c.id, c.parent, c.name
                        FROM ORGANIZATIONGROUP as c
                        WHERE c.id = :id
                    UNION ALL
                    SELECT c2.id, c2.parent, c2.name
                        FROM w1 JOIN ORGANIZATIONGROUP as c2 ON c2.id=w1.parent
                )
                SELECT * FROM w1 WHERE w1.id <> :id
            """,
            nativeQuery = true
    )
    @RestResource(path = "pathById")
    List<OrganizationGroup> findPathById(@Param("id") Integer id);
}
