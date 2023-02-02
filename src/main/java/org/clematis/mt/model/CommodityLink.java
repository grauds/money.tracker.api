package org.clematis.mt.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

/**
 * @author Anton Troshin
 */
@Projection(name = "commodityLink", types = Commodity.class)
public interface CommodityLink {

    @Value("#{target.id}")
    long getId();

    String getName();
}
