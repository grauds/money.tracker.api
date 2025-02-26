package org.clematis.mt.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * A base inerface for read only repositories as views for example
 *
 * @author Anton Troshin
 * @param <T>
 * @param <ID>
 */
@NoRepositoryBean
public interface ReadOnlyRepository<T, ID> extends JpaRepository<T, ID> {

    @Override
    Optional<T> findById(ID id);

    @Override
    List<T> findAll();
}
