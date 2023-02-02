package org.clematis.mt.repository;

import java.util.List;

import org.clematis.mt.model.views.LastExpenseEntry;
import org.clematis.mt.model.views.LastExpenseItem;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author Anton Troshin
 */
@RepositoryRestResource(path = "lastExpenseItems", excerptProjection = LastExpenseEntry.class)
public interface LastExpenseItemRepository extends PagingAndSortingRepository<LastExpenseItem, Integer> {

    List<LastExpenseEntry> findByCommodityId(@Param(value = "commodityId") int commodityId);

}
