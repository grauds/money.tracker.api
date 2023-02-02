package org.clematis.mt.model.views;


import java.util.Date;

import org.clematis.mt.model.CommodityLink;
import org.clematis.mt.model.OrganizationLink;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

import com.fasterxml.jackson.annotation.JsonFormat;
/**
 *
 * @author Anton Troshin
 */
@Projection(name = "entry", types = LastExpenseItem.class)
public interface LastExpenseEntry {

    @Value("#{target.id}")
    long getId();

    int getDaysAgo();

    double getPrice();

    String getCurrency();

    OrganizationLink getOrganization();

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    Date getTransactionDate();

    CommodityLink getCommodity();

    Double getQty();

    String getUnit();

}
