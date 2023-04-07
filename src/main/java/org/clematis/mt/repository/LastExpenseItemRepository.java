package org.clematis.mt.repository;

import java.util.List;

import org.clematis.mt.model.LastExpenseEntry;
import org.clematis.mt.model.LastExpenseItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

/**
 * @author Anton Troshin
 */
@RepositoryRestResource(path = "lastExpenseItems", excerptProjection = LastExpenseEntry.class)
public interface LastExpenseItemRepository extends PagingAndSortingRepository<LastExpenseItem, Integer> {

    // not working with projections due to https://github.com/spring-projects/spring-data-rest/issues/1213
    @SuppressWarnings("checkstyle:methodname")
    @RestResource(path = "findByNameStarting")
    Page<LastExpenseItem> findByCommodity_NameStartingWithIgnoreCase(@Param("name") String name, Pageable pageable);

    @SuppressWarnings("checkstyle:methodname")
    @RestResource(path = "findByNameContaining")
    Page<LastExpenseItem> findByCommodity_NameContainingIgnoreCase(@Param("name") String name, Pageable pageable);

    @SuppressWarnings("checkstyle:methodname")
    @RestResource(path = "findByNameEnding")
    Page<LastExpenseItem> findByCommodity_NameEndingWithIgnoreCase(@Param("name") String name, Pageable pageable);

    List<LastExpenseEntry> findByCommodityId(@Param(value = "commodityId") int commodityId);

}
