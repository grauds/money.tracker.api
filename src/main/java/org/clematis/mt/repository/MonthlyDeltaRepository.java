package org.clematis.mt.repository;

import org.clematis.mt.model.views.MonthlyDelta;
import org.clematis.mt.model.views.MonthlyDeltaKey;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Anton Troshin
 */
@RepositoryRestResource(path = "monthlyDeltas")
public interface MonthlyDeltaRepository extends PagingAndSortingRepository<MonthlyDelta, MonthlyDeltaKey> {

    @Query(value =
            "SELECT ROUND(SUM(MD.DELTA), 2) FROM MONTHLY_DELTA as MD where (MD.AN < :an OR (MD.AN = :an "
                    + "AND MD.MOIS <= :mois)) AND MD.CODE = :code", nativeQuery = true)
    @RestResource(path = "balance")
    Long getBalanceForCurrency(@RequestParam(value = "an", defaultValue = "2002") int an,
                               @RequestParam(value = "mois", defaultValue = "1") int mois,
                               @RequestParam(value = "code", defaultValue = "RUB") String code);


    @RestResource(path = "history")
    @SuppressWarnings("checkstyle:methodname")
    Page<MonthlyDelta> findMonthlyDeltasByKey_Code(
            @RequestParam(value = "code", defaultValue = "RUB") String code, Pageable pageable);
}
