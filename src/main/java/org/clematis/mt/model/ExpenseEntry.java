package org.clematis.mt.model;

import org.springframework.data.rest.core.config.Projection;
/**
 * @author Anton Troshin
 */
@Projection(name = "expenseEntry", types = Expense.class)
public interface ExpenseEntry {

    MoneyTypeEntry getMoneyType();


}

