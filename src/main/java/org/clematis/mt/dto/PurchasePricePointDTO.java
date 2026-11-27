package org.clematis.mt.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.Data;

/**
 * DTO representing a purchase price point for a commodity.
 * Contains price information along with the tradeplace (organization) details.
 */
@Data
@SuppressFBWarnings
public class PurchasePricePointDTO {

    private String tradeplaceName;

    private Integer tradeplaceId;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Date transferDate;

    private Double price;

    private String currencyCode;
}
