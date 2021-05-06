package org.clematis.mt.model.views;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.clematis.mt.model.Account;
/**
 * @author Anton Troshin
 */
@Embeddable
public class OperationEntryKey implements Serializable {

    private Date transferdate;

    @ManyToOne
    @JoinColumn(name = "ACCOUNT")
    private Account account;

    private Double amount;

    private Integer moneySign;

    public Date getTransferDate() {
        return (Date) transferdate.clone();
    }

    public void setTransferDate(Date date) {
        this.transferdate = (Date) date.clone();
    }

}