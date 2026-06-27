package org.clematis.mt.repository;

import io.swagger.v3.oas.annotations.Operation;

import org.clematis.mt.model.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

/**
 * @author Anton Troshin
 */
@RepositoryRestResource(path = "accounts")
public interface AccountRepository extends PagingAndSortingAndFilteringByNameRepository<Account, Integer> {

    @Query(value = """
                SELECT * FROM (WITH recursive w1(id, parent, name) AS
                (
                    SELECT c.id, c.parent, c.name
                        FROM ACCOUNTGROUP as c
                        WHERE c.id = :id
                    UNION ALL
                    SELECT c2.id, c2.parent, c2.name
                        FROM w1 JOIN ACCOUNTGROUP as c2 ON c2.parent=w1.id
                )
                SELECT * FROM ACCOUNT WHERE PARENT IN (SELECT w1.id FROM w1) ORDER BY NAME)""",
        countQuery = """
                SELECT COUNT(*) FROM (WITH recursive w1(id, parent, name) AS
                (
                    SELECT c.id, c.parent, c.name
                        FROM ACCOUNTGROUP as c
                        WHERE c.id = :id
                    UNION ALL
                    SELECT c2.id, c2.parent, c2.name
                        FROM w1 JOIN ACCOUNTGROUP as c2 ON c2.parent=w1.id
                )
                SELECT * FROM ACCOUNT WHERE PARENT IN (SELECT w1.id FROM w1) ORDER BY NAME)""",
        nativeQuery = true)
    @Operation(summary = "Find accounts recursively by parent group id")
    @RestResource(path = "recursiveAccountsByGroupId")
    Page<Account> findAccountsRecursiveByGroupId(@Param("id") Integer id, Pageable pageable);

}
