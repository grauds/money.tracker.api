package org.clematis.mt.repository;

import org.clematis.mt.model.MoneyExchange;
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
@RepositoryRestResource(path = "exchange")
public interface MoneyExchangeRepository extends PagingAndSortingRepository<MoneyExchange, Integer> {

    @RestResource(path = "findAllForCurrencies")
    @SuppressWarnings("checkstyle:methodname")
    Page<MoneyExchange> findAllBySourcemoneytype_CodeAndDestmoneytype_Code(@Param(value = "source") String source,
                                                                           @Param(value = "dest") String dest,
                                                                           Pageable pageable);

    @Query(value = "select SUM(SOURCEAMOUNT) / SUM(DESTAMOUNT)"
            + " from MONEYEXCHANGE where"
            + " SOURCEMONEYTYPE = (SELECT ID FROM MONEYTYPE WHERE CODE=:source)"
            + " AND DESTMONEYTYPE = (SELECT ID FROM MONEYTYPE WHERE CODE=:dest)",
            nativeQuery = true)
    @RestResource(path = "average")
    Double getAverageExchangeRate(@Param(value = "source") String source, @Param(value = "dest") String dest);
}