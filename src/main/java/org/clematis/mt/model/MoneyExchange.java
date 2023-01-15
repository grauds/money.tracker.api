package org.clematis.mt.model;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * Money exchange event
 * @author Anton Troshin
 */
@Entity
@Getter
@Setter
@Table(name = "MONEYEXCHANGE")
public class MoneyExchange extends IdAware {

    private Date exchangedate;

    @ManyToOne(optional = false)
    @JoinColumn(name = "SOURCE")
    private Account source;

    @ManyToOne(optional = false)
    @JoinColumn(name = "DEST")
    private Account dest;

    @ManyToOne(optional = false)
    @JoinColumn(name = "USERMT")
    private User usermt;

    @ManyToOne
    @JoinColumn(name = "TRADEPLACE")
    private Organization tradeplace;

    private Double feepercent;

    private Double rate;

    private Double sourceamount;

    private Double destamount;

    @ManyToOne(optional = false)
    @JoinColumn(name = "SOURCEMONEYTYPE")
    private MoneyType sourcemoneytype;

    @ManyToOne(optional = false)
    @JoinColumn(name = "DESTMONEYTYPE")
    private MoneyType destmoneytype;

    private String remarks;

    private Integer feecomm;

    private Double fee;

    private Integer calcfield;

    private Integer comm;

    public Date getExchangeDate() {
        return exchangedate != null ? (Date) exchangedate.clone() : null;
    }

    public void setExchangeDate(Date exchangedate) {
        this.exchangedate = exchangedate != null ? (Date) exchangedate.clone() : null;
    }
}
