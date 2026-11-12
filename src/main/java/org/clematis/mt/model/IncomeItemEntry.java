package org.clematis.mt.model;


import java.util.Date;

import org.springframework.data.rest.core.config.Projection;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Projection providing entry info for an income item.
 */
@Projection(name = "IncomeItemEntry", types = IncomeItem.class)
public interface IncomeItemEntry {

    /** Returns the transfer date of this income item. */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    Date getTransferDate();

    /** Returns the commodity link for this income item. */
    CommodityLink getCommodity();

    /** Returns the tradeplace (organization) link for this income item. */
    OrganizationLink getTradeplace();

    /** Returns the price of this income item. */
    Double getPrice();

    /** Returns the total amount of this income item. */
    Double getTotal();

    /** Returns the quantity of this income item. */
    Double getQty();

    /** Returns the parent income entry for this item. */
    IncomeEntry getIncome();
}
