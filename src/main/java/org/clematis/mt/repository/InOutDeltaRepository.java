package org.clematis.mt.repository;

import org.clematis.mt.model.InOutDelta;
import org.clematis.mt.model.InOutDeltaEntry;
import org.clematis.mt.model.InOutDeltaKey;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author Anton Troshin
 */
@RepositoryRestResource(path = "inOutDeltas", excerptProjection = InOutDeltaEntry.class)
public interface InOutDeltaRepository extends PagingAndSortingRepository<InOutDelta, InOutDeltaKey> {
}
