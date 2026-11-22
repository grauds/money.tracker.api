package org.clematis.mt.repository;

import java.util.List;

import org.clematis.mt.model.LastExpenseEntry;
import org.clematis.mt.model.LastExpenseItem;
import org.clematis.mt.model.QLastExpenseItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.querydsl.binding.SingleValueBinding;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.core.types.dsl.StringPath;

/**
 * Repository for LastExpenseItem data access.
 */
@RepositoryRestResource(path = "lastExpenseItems", excerptProjection = LastExpenseEntry.class)
public interface LastExpenseItemRepository extends
    PagingAndSortingRepository<LastExpenseItem, Integer>,
    QuerydslPredicateExecutor<LastExpenseItem>,
    QuerydslBinderCustomizer<QLastExpenseItem> {

    @Override
    default void customize(QuerydslBindings bindings, QLastExpenseItem root) {
        // Applies case-insensitive "contains" to ALL String fields (including commodity.name)
        bindings.bind(String.class)
            .first((SingleValueBinding<StringPath, String>) StringExpression::containsIgnoreCase);

        // Or, if "contains" is ONLY on commodity.name:
        // bindings.bind(root.commodity.name).first(StringExpression::containsIgnoreCase);
    }

    /**
     * Find expense items by commodity name starting with the given value (case-insensitive).
     */
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
