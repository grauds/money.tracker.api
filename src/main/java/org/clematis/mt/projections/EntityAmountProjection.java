package org.clematis.mt.projections;

/**
 * Represents a projection of an entity's amount and transaction count.
 */
public interface EntityAmountProjection {
    String getName();
    Double getTotal();
    Integer getTxCount();
}