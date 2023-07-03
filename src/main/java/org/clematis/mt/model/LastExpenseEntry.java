package org.clematis.mt.model;


import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

import com.fasterxml.jackson.annotation.JsonFormat;
/**
 * @author Anton Troshin
 */
@Projection(name = "lastExpenseEntry", types = LastExpenseItem.class)
public interface LastExpenseEntry {

    @Value("#{target.id}")
    long getId();

    int getDaysAgo();

    Double getPrice();

    String getCurrency();

    OrganizationLink getOrganization();

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    Date getTransactionDate();

    CommodityLink getCommodity();

    Double getQty();

    String getUnit();

}
