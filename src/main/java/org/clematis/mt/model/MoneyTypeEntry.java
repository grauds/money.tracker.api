package org.clematis.mt.model;

import org.springframework.data.rest.core.config.Projection;
/**
 * Projection providing entry info for a money type.
 */
@Projection(name = "moneyTypeEntry", types = MoneyType.class)
public interface MoneyTypeEntry {

    /** Returns the code of this money type. */
    String getCode();

    /** Returns the sign symbol of this money type. */
    String getSign();
}
