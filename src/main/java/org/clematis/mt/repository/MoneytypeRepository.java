package org.clematis.mt.repository;

import org.clematis.mt.model.MoneyType;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author Anton Troshin
 */
@RepositoryRestResource(collectionResourceRel = "moneytypes", path = "moneytypes")
public interface MoneytypeRepository extends PagingAndSortingRepository<MoneyType, Long> {

}
