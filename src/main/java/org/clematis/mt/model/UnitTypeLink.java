package org.clematis.mt.model;

import org.springframework.data.rest.core.config.Projection;

/**
 * Projection providing link info for a unit type.
 */
@Projection(name = "unitTypeLink", types = UnitType.class)
public interface UnitTypeLink {

    /** Returns the short name of this unit type. */
    String getShortName();
}
