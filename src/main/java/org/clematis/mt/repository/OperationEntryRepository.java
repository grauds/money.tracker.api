package org.clematis.mt.repository;

import org.clematis.mt.model.views.OperationEntry;
import org.clematis.mt.model.views.OperationEntryKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author Anton Troshin
 */
@RepositoryRestResource(collectionResourceRel = "operations", path = "operations")
public interface OperationEntryRepository extends JpaRepository<OperationEntry, OperationEntryKey> {
}