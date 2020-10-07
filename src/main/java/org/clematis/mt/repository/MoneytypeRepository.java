package org.clematis.mt.repository;

import org.clematis.mt.model.MoneyType;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author Anton Troshin
 */
@RepositoryRestResource(collectionResourceRel = "moneytype", path = "moneytype")
public interface MoneytypeRepository extends PagingAndSortingRepository<MoneyType, Long> {

}
