package org.clematis.mt.dto;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.Data;

/**
 * DTO containing application-wide statistics and information.
 */
@Data
@SuppressFBWarnings
public class InfoAbout {
    long expenses;
    long income;
    long organizations;
    long accounts;
    DateRange dates;
    Long expensesNoCommodity;
    Long expensesNoTradeplace;
    Long expensesTradeplace;
}
