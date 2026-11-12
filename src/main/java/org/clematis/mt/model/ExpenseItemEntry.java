package org.clematis.mt.model;

import java.util.Date;

import org.springframework.data.rest.core.config.Projection;

import com.fasterxml.jackson.annotation.JsonFormat;
/**
 * Projection providing entry info for an expense item.
 */
@Projection(name = "ExpenseItemEntry", types = ExpenseItem.class)
public interface ExpenseItemEntry {

    /** Returns the transfer date of this expense item. */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    Date getTransferDate();

    /** Returns the commodity link for this expense item. */
    CommodityLink getCommodity();

    /** Returns the tradeplace (organization) link for this expense item. */
    OrganizationLink getTradeplace();

    /** Returns the price of this expense item. */
    Double getPrice();

    /** Returns the quantity of this expense item. */
    Double getQty();

    /** Returns the parent expense entry for this item. */
    ExpenseEntry getExpense();
}
