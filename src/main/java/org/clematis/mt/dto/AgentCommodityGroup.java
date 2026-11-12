package org.clematis.mt.dto;

/**
 * DTO for expense aggregates grouped by agent and commodity group.
 */
public interface AgentCommodityGroup {

    /**
     * Returns the agent name.
     * @return agent name
     */
    String getAgent();

    /**
     * Returns the commodity group name.
     * @return commodity group name
     */
    String getCommodityGroup();

    /**
     * Returns the total expense amount.
     * @return total amount
     */
    Double getTotal();

    /**
     * Returns the month.
     * @return month
     */
    String getMois();

    /**
     * Returns the year.
     * @return year
     */
    String getAn();
}
