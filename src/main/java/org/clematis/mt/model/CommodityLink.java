package org.clematis.mt.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

/**
 * Projection providing link info for a commodity.
 */
@Projection(name = "commodityLink", types = Commodity.class)
public interface CommodityLink {

    /** Returns the commodity ID. */
    @Value("#{target.id}")
    long getId();

    /** Returns the commodity name. */
    String getName();

    /** Returns the unit type link for this commodity. */
    UnitTypeLink getUnittype();

}
