package org.clematis.mt.repository;

import org.clematis.mt.model.MonthlyDelta;
import org.clematis.mt.model.MonthlyDeltaKey;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

/**
 * @author Anton Troshin
 */
@RepositoryRestResource(path = "monthlyDeltas")
public interface MonthlyDeltaRepository extends PagingAndSortingRepository<MonthlyDelta, MonthlyDeltaKey> {

    @Query(value =
            "SELECT ROUND(SUM(MD.DELTA), 2) FROM MONTHLY_DELTA as MD where (MD.AN < :an OR (MD.AN = :an "
                    + "AND MD.MOIS < :mois)) AND MD.CODE = :code", nativeQuery = true)
    @RestResource(path = "balance")
    Long getBalanceForCurrency(@Param(value = "an") int an,
                               @Param(value = "mois") int mois,
                               @Param(value = "code") String code);


    @RestResource(path = "history")
    @SuppressWarnings("checkstyle:methodname")
    Page<MonthlyDelta> findMonthlyDeltasByKey_Code(@Param(value = "code") String code, Pageable pageable);
}
