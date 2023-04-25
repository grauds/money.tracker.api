package org.clematis.mt.repository;

import org.clematis.mt.model.Organization;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

/**
 * @author Anton Troshin
 */
@RepositoryRestResource(path = "organizations")
@SecurityRequirement(name = "bearer")
public interface OrganizationRepository extends PagingAndSortingAndFilteringByNameRepository<Organization, Integer> {

    @Query(value = "SELECT * FROM (WITH recursive w1(id, parent, name) AS\n"
            + "(SELECT c.id, c.parent, c.name\n"
            + "    FROM ORGANIZATIONGROUP as c\n"
            + "    WHERE c.id = :id\n"
            + "\tUNION ALL\n"
            + "\tSELECT c2.id, c2.parent, c2.name\n"
            + "\tFROM w1 JOIN ORGANIZATIONGROUP as c2 ON c2.parent=w1.id\n"
            + ")\n"
            + "SELECT * FROM ORGANIZATION WHERE PARENT IN (SELECT w1.id FROM w1) ORDER BY NAME)",
            countQuery = "SELECT COUNT(*) FROM (WITH recursive w1(id, parent, name) AS\n"
                + "(SELECT c.id, c.parent, c.name\n"
                + "    FROM ORGANIZATIONGROUP as c\n"
                + "    WHERE c.id = :id\n"
                + "\tUNION ALL\n"
                + "\tSELECT c2.id, c2.parent, c2.name\n"
                + "\tFROM w1 JOIN ORGANIZATIONGROUP as c2 ON c2.parent=w1.id\n"
                + ")\n"
                + "SELECT * FROM ORGANIZATION WHERE PARENT IN (SELECT w1.id FROM w1) ORDER BY NAME)",
            nativeQuery = true)
    @Operation(summary = "Find organizations recursively by parent group id")
    @RestResource(path = "recursiveByParentGroupId")
    Page<Organization> findRecursiveByParentGroupId(@Param("id") Integer id, Pageable pageable);
}
