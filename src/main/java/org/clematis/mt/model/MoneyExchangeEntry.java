package org.clematis.mt.model;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Projection providing entry info for a money exchange operation.
 */
@Projection(name = "moneyExchangeEntry", types = MoneyExchange.class)
public interface MoneyExchangeEntry {

    /** Returns the ID of this money exchange entry. */
    @Value("#{target.id}")
    long getId();

    /** Returns the exchange date of this operation. */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    Date getExchangeDate();

    /** Returns the destination account link for this exchange. */
    AccountLink getDest();

    /** Returns the source account link for this exchange. */
    AccountLink getSource();

    /** Returns the source amount exchanged. */
    Double getSourceamount();

    /** Returns the destination amount received. */
    Double getDestamount();

    /** Returns the source money type entry. */
    MoneyTypeEntry getSourcemoneytype();

    /** Returns the destination money type entry. */
    MoneyTypeEntry getDestmoneytype();

    /** Returns the exchange rate applied. */
    Double getRate();
}
