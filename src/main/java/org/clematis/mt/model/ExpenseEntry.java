package org.clematis.mt.model;

import org.springframework.data.rest.core.config.Projection;
/**
 * Projection providing entry info for an expense.
 */
@Projection(name = "expenseEntry", types = Expense.class)
public interface ExpenseEntry {

    /** Returns the money type entry for this expense. */
    MoneyTypeEntry getMoneyType();


}

