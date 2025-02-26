package org.clematis.mt.repository;

import org.clematis.mt.dto.MoneyExchangeReport;
import org.clematis.mt.model.MoneyExchange;
import org.clematis.mt.model.MoneyExchangeEntry;
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
@RepositoryRestResource(path = "exchange", excerptProjection = MoneyExchangeEntry.class)
public interface MoneyExchangeRepository extends PagingAndSortingRepository<MoneyExchange, Integer> {

    @RestResource(path = "events")
    @SuppressWarnings("checkstyle:methodname")
    Page<MoneyExchange> findAllBySourcemoneytype_CodeAndDestmoneytype_Code(@Param(value = "source") String source,
                                                                           @Param(value = "dest") String dest,
                                                                           Pageable pageable);

    @Query(value = """
            SELECT SUM(SOURCEAMOUNT) / SUM(DESTAMOUNT) FROM MONEYEXCHANGE WHERE
                    SOURCEMONEYTYPE = (SELECT ID FROM MONEYTYPE WHERE CODE=:source)
                        AND DESTMONEYTYPE = (SELECT ID FROM MONEYTYPE WHERE CODE=:dest)
            """,
            nativeQuery = true)
    @RestResource(path = "average")
    Double getAverageExchangeRate(@Param(value = "source") String source, @Param(value = "dest") String dest);


    @Query(value = """
        SELECT b.*, b.SOURCEAMOUNT * (b.AVGRATE - b.CURRATE) AS DELTA FROM
        (SELECT a.*, (SELECT * FROM CROSS_RATE(:dest, :source)) AS CURRATE FROM
            (SELECT SUM(SOURCEAMOUNT) AS SOURCEAMOUNT,
                    SUM(DESTAMOUNT) AS DESTAMOUNT,
                    (SUM(DESTAMOUNT) / SUM(SOURCEAMOUNT)) AS AVGRATE
            FROM MONEYEXCHANGE
             WHERE SOURCEMONEYTYPE = (SELECT ID FROM MONEYTYPE WHERE CODE=:source)
              AND DESTMONEYTYPE = (SELECT ID FROM MONEYTYPE WHERE CODE=:dest))
            AS a)
         AS b""", nativeQuery = true)
    @RestResource(path = "report")
    MoneyExchangeReport getExchangeReport(@Param(value = "source") String source, @Param(value = "dest") String dest);
}