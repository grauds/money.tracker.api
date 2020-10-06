package org.clematis.mt.repository;

import org.clematis.mt.model.CommodityGroup;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "commodityGroup", path = "commodityGroup")
public interface CommodityGroupRepository extends PagingAndSortingRepository<CommodityGroup, Long> {

}
