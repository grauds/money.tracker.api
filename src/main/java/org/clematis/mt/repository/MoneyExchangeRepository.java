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

    @Query(value = "select SUM(SOURCEAMOUNT) / SUM(DESTAMOUNT) from MONEYEXCHANGE where"
            + " SOURCEMONEYTYPE = (SELECT ID FROM MONEYTYPE WHERE CODE=:source)"
            + " AND DESTMONEYTYPE = (SELECT ID FROM MONEYTYPE WHERE CODE=:dest)",
            nativeQuery = true)
    @RestResource(path = "average")
    Double getAverageExchangeRate(@Param(value = "source") String source, @Param(value = "dest") String dest);


    @Query(value = "select b.*, b.SOURCEAMOUNT * (b.CURRATE - b.AVGRATE) as DELTA from "
            + "(select a.*, (SELECT * FROM CROSS_RATE(:dest, :source)) as CURRATE from "
            + "    (select SUM(SOURCEAMOUNT) as SOURCEAMOUNT, "
            + "            SUM(DESTAMOUNT) as DESTAMOUNT, "
            + "            (SUM(DESTAMOUNT) / SUM(SOURCEAMOUNT)) as AVGRATE "
            + "    from MONEYEXCHANGE "
            + "     where SOURCEMONEYTYPE = (SELECT ID FROM MONEYTYPE WHERE CODE=:source) "
            + "      AND DESTMONEYTYPE = (SELECT ID FROM MONEYTYPE WHERE CODE=:dest)) "
            + "    as a) "
            + " as b", nativeQuery = true)
    @RestResource(path = "report")
    MoneyExchangeReport getExchangeReport(@Param(value = "source") String source, @Param(value = "dest") String dest);
}