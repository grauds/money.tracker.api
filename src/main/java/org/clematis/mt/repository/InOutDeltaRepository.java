package org.clematis.mt.repository;

import java.util.List;

import org.clematis.mt.model.InOutDelta;
import org.clematis.mt.model.InOutDeltaEntry;
import org.clematis.mt.model.InOutDeltaKey;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

/**
 * @author Anton Troshin
 */
@RepositoryRestResource(path = "inOutDeltas", excerptProjection = InOutDeltaEntry.class)
public interface InOutDeltaRepository extends PagingAndSortingRepository<InOutDelta, InOutDeltaKey> {

    @Query(value = "SELECT COMM_ID, SUM(ETOTAL) AS ETOTAL, SUM(ITOTAL) AS ITOTAL, ROUND(SUM(DELTA), 2) AS DELTA,"
            + " MID, MCODE "
            + "FROM"
            + "    (SELECT COMM_ID, ETOTAL, ITOTAL,"
            + "       DELTA * (SELECT * FROM CROSS_RATE(:code, MCODE)) AS DELTA,"
            + "       M.ID as MID, M.CODE AS MCODE "
            + "     FROM IN_OUT_DELTA as IOD"
            + "     LEFT JOIN MONEYTYPE M on M.CODE=:code"
            + "    )"
            + "    AS A GROUP BY COMM_ID, MID, MCODE ORDER BY DELTA DESC", nativeQuery = true)
    @RestResource(path = "code")
    List<InOutDelta> getDeltas(@Param(value = "code") String code);
}
