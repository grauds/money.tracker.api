package org.clematis.mt.model;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * @author Anton Troshin
 */
@Projection(name = "moneyExchangeEntry", types = MoneyExchange.class)
public interface MoneyExchangeEntry {

    @Value("#{target.id}")
    long getId();

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    Date getExchangeDate();

    AccountLink getDest();

    AccountLink getSource();

    Double getSourceamount();

    Double getDestamount();

    MoneyTypeEntry getSourcemoneytype();

    MoneyTypeEntry getDestmoneytype();

    Double getRate();
}
