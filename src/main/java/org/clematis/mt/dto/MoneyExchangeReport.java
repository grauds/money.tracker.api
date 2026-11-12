package org.clematis.mt.dto;

/**
 * DTO for money exchange report with rates and amounts.
 */
public interface MoneyExchangeReport {

    /**
     * Returns the average exchange rate.
     * @return average rate
     */
    Double getAvgRate();

    /**
     * Returns the current exchange rate.
     * @return current rate
     */
    Double getCurRate();

    /**
     * Returns the delta amount.
     * @return delta amount
     */
    Double getDelta();

    /**
     * Returns the destination amount.
     * @return destination amount
     */
    Double getDestAmount();

    /**
     * Returns the source amount.
     * @return source amount
     */
    Double getSourceAmount();
}
