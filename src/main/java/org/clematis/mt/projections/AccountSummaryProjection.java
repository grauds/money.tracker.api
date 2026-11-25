package org.clematis.mt.projections;

import java.math.BigDecimal;

/**
 * Represents a projection of currency summary data for an account.
 */
public interface AccountSummaryProjection {
    Integer getAccountId();
    String getAccountName();
    Integer getYear();
    Integer getMonth();
    String getCode();
    BigDecimal getDeltaOrig();
    BigDecimal getDeltaTarget();
    BigDecimal getBalanceOrig();
    BigDecimal getBalanceTarget();
    BigDecimal getMonthTotalDeltaTarget();
    BigDecimal getMonthTotalBalanceTarget();
}
