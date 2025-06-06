package org.clematis.mt.model;

import java.util.Date;

import org.springframework.data.rest.core.config.Projection;

import com.fasterxml.jackson.annotation.JsonFormat;
/**
 * @author Anton Troshin
 */
@Projection(name = "ExpenseItemEntry", types = ExpenseItem.class)
public interface ExpenseItemEntry {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    Date getTransferDate();

    CommodityLink getCommodity();

    OrganizationLink getTradeplace();

    Double getPrice();

    Double getQty();

    ExpenseEntry getExpense();
}
