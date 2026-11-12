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

    @RestResource(exported = false)
    @Query(value = """
            SELECT COALESCE(SUM(TOTALCONVERTED), 0)
            FROM GET_INCOME_BY_CURRENCY(:code)
        """, nativeQuery = true)
    Double sumTotalInCurrency(@Param("code") String code);

    @RestResource(exported = false)
    @Query(value = """
            SELECT CODE AS code, TOTAL AS total, TOTALCONVERTED AS totalConverted
            FROM GET_INCOME_BY_CURRENCY(:code)
        """, nativeQuery = true)
    List<MoneyShareInCurrency> getIncomeByCurrency(@Param("code") String code);

}
