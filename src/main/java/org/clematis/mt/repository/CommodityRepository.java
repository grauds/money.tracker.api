package org.clematis.mt.repository;

import org.clematis.mt.model.Commodity;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "commodity", path = "commodity")
public interface CommodityRepository extends PagingAndSortingRepository<Commodity, Long> {

}
