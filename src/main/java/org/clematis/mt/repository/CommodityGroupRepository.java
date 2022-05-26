package org.clematis.mt.repository;

import org.clematis.mt.model.CommodityGroup;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author Anton Troshin
 */
@RepositoryRestResource(path = "commodityGroups")
public interface CommodityGroupRepository extends PagingAndSortingAndFilteringByNameRepository<CommodityGroup, Long> {

}
