package org.clematis.mt.model;

import org.springframework.data.rest.core.config.Projection;

/**
 * @author Anton Troshin
 */
@Projection(name = "incomeEntry", types = Income.class)
public interface IncomeEntry {

    MoneyTypeEntry getMoneyType();


}

