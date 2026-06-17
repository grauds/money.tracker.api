package org.clematis.mt.repository;

import org.clematis.mt.model.NamedEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * This is a trivial extension of the basic Spring repository to add search by name
 *
 * @param <T>
 * @param <ID>
 * @author Anton Troshin
 */
@NoRepositoryBean
public interface PagingAndSortingAndFilteringByNameRepository<T extends NamedEntity, ID>
    extends JpaRepository<T, ID> {

}
