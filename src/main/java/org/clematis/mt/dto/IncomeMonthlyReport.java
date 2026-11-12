package org.clematis.mt.dto;

/**
 * DTO for monthly income report grouped by commodity.
 */
@Deprecated
public interface IncomeMonthlyReport {

    /**
     * Returns the year.
     * @return year
     */
    String getAn();

    /**
     * Returns the month.
     * @return month
     */
    String getMois();

    /**
     * Returns the commodity name.
     * @return commodity name
     */
    String getCommodity();

    /**
     * Returns the total income amount.
     * @return total amount
     */
    Double getTotal();
}
