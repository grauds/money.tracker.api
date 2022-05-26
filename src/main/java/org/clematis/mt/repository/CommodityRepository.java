package org.clematis.mt.repository;

import org.clematis.mt.model.Commodity;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
/**
 * @author Anton Troshin
 */
@RepositoryRestResource(path = "commodities")
public interface CommodityRepository extends PagingAndSortingAndFilteringByNameRepository<Commodity, Long> {


}
