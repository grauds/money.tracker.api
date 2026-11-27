package org.clematis.mt.dto;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO containing application-wide statistics and information.
 */
@Getter
@Setter
@SuppressFBWarnings
public class GeneralStatsDTO {
    long expenses;
    long income;
    long organizations;
    long accounts;
    DateRangeDTO dates;
    Long expensesNoCommodity;
    Long expensesNoTradeplace;
    Long expensesTradeplace;
}
