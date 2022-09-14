package org.clematis.mt.repository;

import org.clematis.mt.model.NamedEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

/**
 * This is a trivial extension of the basic Spring repository to add search by the common fields
 * @param <T>
 * @param <ID>
 * @author Anton Troshin
 */
@NoRepositoryBean
public interface PagingAndSortingAndFilteringByNameRepository<T extends NamedEntity, ID>
        extends PagingAndSortingRepository<T, ID> {

    @RestResource(path = "findByNameStarting")
    Page<T> findByNameStartingWithIgnoreCase(@Param("name") String name, Pageable pageable);

    @RestResource(path = "findByNameContaining")
    Page<T> findByNameContainingIgnoreCase(@Param("name") String name, Pageable pageable);

    @RestResource(path = "findByNameEnding")
    Page<T> findByNameEndingWithIgnoreCase(@Param("name") String name, Pageable pageable);

}
