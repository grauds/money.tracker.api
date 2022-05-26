package org.clematis.mt.repository;

import java.util.List;

import org.clematis.mt.model.NamedEntity;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

/**
 * This is a trivial extension of the basic Spring repository to add search by the common fields
 * @param <T>
 * @param <ID>
 * @author Anton Troshin
 */
@NoRepositoryBean
public interface PagingAndSortingAndFilteringByNameRepository<T extends NamedEntity, ID>
        extends PagingAndSortingRepository<T, ID> {

    List<T> findAllByNameContainingIgnoreCase(@Param("name") String name);

    List<T> findByNameContains(@Param("name") String name);
}
