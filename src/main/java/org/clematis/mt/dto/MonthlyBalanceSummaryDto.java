package org.clematis.mt.dto;

import java.math.BigDecimal;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import lombok.Getter;

@Relation(collectionRelation = "monthlyDeltas", itemRelation = "monthlyDelta")
@Getter
public class MonthlyBalanceSummaryDto extends RepresentationModel<MonthlyBalanceSummaryDto> {

    private final Integer accountId;
    private final String accountName;
    private final Integer year;
    private final Integer month;
    private final String code;
    private final BigDecimal deltaOrig;
    private final BigDecimal deltaTarget;
    private final BigDecimal balanceOrig;
    private final BigDecimal balanceTarget;
    private final BigDecimal monthTotalDeltaTarget;
    private final BigDecimal monthTotalBalanceTarget;

    public MonthlyBalanceSummaryDto(CurrencySummaryProjection p) {
        this.accountId = p.getAccountId();
        this.accountName = p.getAccountName();
        this.year = p.getYear();
        this.month = p.getMonth();
        this.code = p.getCode();
        this.deltaOrig = p.getDeltaOrig();
        this.deltaTarget = p.getDeltaTarget();
        this.balanceOrig = p.getBalanceOrig();
        this.balanceTarget = p.getBalanceTarget();
        this.monthTotalDeltaTarget = p.getMonthTotalDeltaTarget();
        this.monthTotalBalanceTarget = p.getMonthTotalBalanceTarget();
    }
}
