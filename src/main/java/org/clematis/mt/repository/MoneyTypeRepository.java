package org.clematis.mt.repository;

import org.clematis.mt.model.MoneyType;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

/**
 * @author Anton Troshin
 */
@RepositoryRestResource(path = "moneyTypes")
public interface MoneyTypeRepository extends PagingAndSortingAndFilteringByNameRepository<MoneyType, Integer> {

    @RestResource(path = "findByCode")
    MoneyType findMoneyTypeByCode(@Param("code") String code);
}
