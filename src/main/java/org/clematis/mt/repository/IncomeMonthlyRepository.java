package org.clematis.mt.repository;

import java.util.List;

import org.clematis.mt.model.IncomeMonthly;
import org.clematis.mt.model.IncomeMonthlyKey;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

/**
 * @author Anton Troshin
 */
@RepositoryRestResource(path = "incomeMonthly")
public interface IncomeMonthlyRepository extends PagingAndSortingRepository<IncomeMonthly, IncomeMonthlyKey> {

    @Query(value = "SELECT MOIS, "
            + "       AN, "
            + "       ROUND(TOTAL, 2) AS TOTAL, "
            + "       ROUND(TOTAL * (SELECT * FROM CROSS_RATE(:code, CODE, "
            + "          cast ('1-' || MOIS || '-' || AN as date))), 2) AS TOTAL_CONVERTED, "
            + "       COMM_ID, "
            + "       COMMODITY,"
            + "       CODE "
            + "FROM MONTHLY_INCOME where (AN < :anEnd OR (AN = :anEnd AND MOIS <= :moisEnd)) AND "
            + "                          (AN > :anStart OR (AN = :anStart AND MOIS >= :moisStart))",
            nativeQuery = true)
    @RestResource(path = "report")
    List<IncomeMonthly> getIncomeItemReports(@Param("code") String code,
                                             @Param(value = "moisStart") int moisStart,
                                             @Param(value = "anStart") int anStart,
                                             @Param(value = "moisEnd") int moisEnd,
                                             @Param(value = "anEnd") int anEnd);

}
