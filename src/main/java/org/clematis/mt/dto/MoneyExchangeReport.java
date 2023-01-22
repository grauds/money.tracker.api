package org.clematis.mt.dto;

/**
 * @author Anton Troshin
 */
public interface MoneyExchangeReport {

    Double getAvgRate();

    Double getCurRate();

    Double getDelta();

    Double getDestAmount();

    Double getSourceAmount();
}
