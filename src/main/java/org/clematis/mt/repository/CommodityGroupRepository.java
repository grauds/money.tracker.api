package org.clematis.mt.repository;

import org.clematis.mt.model.CommodityGroup;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author Anton Troshin
 */
@RepositoryRestResource(collectionResourceRel = "commodityGroups", path = "commodityGroups")
public interface CommodityGroupRepository extends PagingAndSortingRepository<CommodityGroup, Long> {

}
