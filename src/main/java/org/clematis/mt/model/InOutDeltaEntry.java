package org.clematis.mt.model;

import org.springframework.data.rest.core.config.Projection;

/**
 * Projection providing entry info for in/out delta between income and expenses.
 */
@Projection(name = "inOutDeltaEntry", types = InOutDelta.class)
public interface InOutDeltaEntry {

    /** Returns the commodity link for this delta. */
    CommodityLink getCommodity();

    /** Returns the total expenses amount for this delta. */
    Double getEtotal();

    /** Returns the total income amount for this delta. */
    Double getItotal();

    /** Returns the delta (income minus expenses) for this entry. */
    Double getDelta();

    /** Returns the money type entry for this delta. */
    MoneyTypeEntry getMoneyType();
}
