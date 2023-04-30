package org.clematis.mt.repository;

import org.clematis.mt.model.MoneyType;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author Anton Troshin
 */
@RepositoryRestResource(path = "moneyTypes")
public interface MoneyTypeRepository extends PagingAndSortingAndFilteringByNameRepository<MoneyType, Integer> {


}
