package org.clematis.mt.repository;

import java.util.List;

import org.clematis.mt.dto.MoneyShareInCurrency;
import org.clematis.mt.model.Income;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;


@RepositoryRestResource(path = "income")
public interface IncomeRepository extends PagingAndSortingRepository<Income, Integer> {

    @RestResource(path = "total")
    @Query(value = """
        SELECT SUM(e.total / NULLIF((SELECT RATE FROM CROSS_RATE(m.code, :code, e.transferdate)), 0))
        FROM Income as e
             JOIN MoneyType m ON m.id = e.moneyType
        """, nativeQuery = true)
    Double sumTotalInCurrency(@Param("code") String code);

    @RestResource(path = "totalByCurrency")
    @Query(value = """
        SELECT m.CODE as CODE,
               ROUND(SUM(e.total), 2) as TOTAL,
               ROUND(SUM(
                       e.total / NULLIF((SELECT RATE FROM CROSS_RATE(m.code, :code, e.transferdate)), 0)
               ), 2) as TOTALCONVERTED
        FROM Income as e
             JOIN MoneyType m ON m.id = e.moneyType
        GROUP BY m.CODE
        """, nativeQuery = true)
    List<MoneyShareInCurrency> getIncomeByCurrency(@Param("code") String code);
}
