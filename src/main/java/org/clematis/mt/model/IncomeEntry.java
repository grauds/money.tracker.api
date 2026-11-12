package org.clematis.mt.model;

import org.springframework.data.rest.core.config.Projection;

/**
 * Projection providing entry info for an income.
 */
@Projection(name = "incomeEntry", types = Income.class)
public interface IncomeEntry {

    /** Returns the money type entry for this income. */
    MoneyTypeEntry getMoneyType();


}

