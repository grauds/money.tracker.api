package org.clematis.mt.repository;

import org.clematis.mt.model.views.MonthlyDelta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Anton Troshin
 */
@RepositoryRestResource(path = "monthlyDeltas")
public interface MonthlyDeltaRepository extends JpaRepository<MonthlyDelta, Integer> {

    @Query(value =
            "SELECT ROUND(SUM(MD.DELTA), 2) FROM MONTHLY_DELTA as MD where MD.AN <= :an "
                    + "AND MD.MOIS <= :mois AND MD.CODE = :code", nativeQuery = true)
    @RestResource(path = "balance")
    Long getBalanceForCurrency(@RequestParam(value = "an", defaultValue = "0") int an,
                               @RequestParam(value = "mois", defaultValue = "0") int mois,
                               @RequestParam(value = "code", defaultValue = "RUB") String code);
}
