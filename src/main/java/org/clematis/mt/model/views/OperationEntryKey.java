package org.clematis.mt.model.views;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Embeddable;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;
/**
 * @author Anton Troshin
 */
@Embeddable
@Getter
@Setter
public class OperationEntryKey implements Serializable {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private Date transferdate;

    private Integer account;

    private Double amount;

    private Integer moneySign;

    public Date getTransferDate() {
        return transferdate != null ? (Date) transferdate.clone() : null;
    }

    public void setTransferDate(Date date) {
        this.transferdate = date != null ? (Date) date.clone() : null;
    }

}