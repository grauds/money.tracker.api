package org.clematis.mt.model;

import org.springframework.data.rest.core.config.Projection;

/**
 * @author Anton Troshin
 */
@Projection(name = "unitTypeLink", types = UnitType.class)
public interface UnitTypeLink {

    String getShortName();
}
