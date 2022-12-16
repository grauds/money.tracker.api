package org.clematis.mt.repository;

import org.clematis.mt.model.views.OperationEntry;
import org.clematis.mt.model.views.OperationEntryKey;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author Anton Troshin
 */
@RepositoryRestResource(path = "operations")
public interface OperationEntryRepository extends PagingAndSortingRepository<OperationEntry, OperationEntryKey> {
}
