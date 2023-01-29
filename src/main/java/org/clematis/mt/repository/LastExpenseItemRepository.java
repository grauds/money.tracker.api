package org.clematis.mt.repository;

import java.util.List;

import org.clematis.mt.model.views.LastExpenseItem;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author Anton Troshin
 */
@RepositoryRestResource(path = "lastExpenseItems")
public interface LastExpenseItemRepository extends PagingAndSortingRepository<LastExpenseItem, Integer> {

    List<LastExpenseItem> findByCommodityId(@Param(value = "commodityId") int commodityId);

}
