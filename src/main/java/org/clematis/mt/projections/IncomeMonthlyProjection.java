package org.clematis.mt.projections;

/**
 * A projection for the monthly income report grouped by commodity.
 */
public interface IncomeMonthlyProjection {

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
