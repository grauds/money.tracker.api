package org.clematis.mt.projections;

/**
 * Breakdown of income or expense by currency with native and converted amounts.
 */
public interface MoneyShareInCurrencyProjection {

    String getCode();

    Double getTotal();

    Double getTotalConverted();
}
