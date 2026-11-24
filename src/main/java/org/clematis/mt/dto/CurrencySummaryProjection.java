package org.clematis.mt.dto;

import java.math.BigDecimal;

public interface CurrencySummaryProjection {
    Integer getYear();
    Integer getMonth();
    Integer getAccountId();
    String getAccountName();
    String getCode();
    BigDecimal getDeltaOrig();
    BigDecimal getDeltaTarget();
    BigDecimal getBalanceOrig();
    BigDecimal getBalanceTarget();
    BigDecimal getMonthTotalDeltaTarget();
    BigDecimal getMonthTotalBalanceTarget();
}
