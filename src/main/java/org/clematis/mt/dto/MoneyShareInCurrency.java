package org.clematis.mt.dto;

/**
 * Breakdown of income or expense by currency with native and converted amounts.
 */
public interface MoneyShareInCurrency {

    String getCode();

    Double getTotal();

    Double getTotalConverted();
}
