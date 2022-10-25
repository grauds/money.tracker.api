package org.clematis.mt.repository;

import org.clematis.mt.model.views.MonthlyDelta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author Anton Troshin
 */
@RepositoryRestResource(path = "monthlyDeltas")
public interface MonthlyDeltaRepository extends JpaRepository<MonthlyDelta, Integer> {

}
