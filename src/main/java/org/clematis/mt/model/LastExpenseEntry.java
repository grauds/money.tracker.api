package org.clematis.mt.model;


import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

import com.fasterxml.jackson.annotation.JsonFormat;
/**
 * Projection providing entry info for the last expense item for a commodity.
 */
@Projection(name = "lastExpenseEntry", types = LastExpenseItem.class)
public interface LastExpenseEntry {

    /** Returns the ID of this last expense entry. */
    @Value("#{target.id}")
    long getId();

    /** Returns the number of days since this expense occurred. */
    int getDaysAgo();

    /** Returns the price of the last expense item. */
    Double getPrice();

    /** Returns the currency of the last expense. */
    String getCurrency();

    /** Returns the organization (tradeplace) of the last expense. */
    OrganizationLink getOrganization();

    /** Returns the transaction date of the last expense. */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    Date getTransactionDate();

    /** Returns the commodity of the last expense. */
    CommodityLink getCommodity();

    /** Returns the quantity of the last expense item. */
    Double getQty();

    /** Returns the unit of measurement for the last expense. */
    String getUnit();

}
