package org.clematis.mt.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Embeddable;

import com.fasterxml.jackson.annotation.JsonFormat;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Anton Troshin
 */
@Embeddable
@Data
@NoArgsConstructor
@SuppressFBWarnings
public class OperationEntryKey implements Serializable {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Date transferdate;

    private Integer account;

    public OperationEntryKey(Date transferdate, Integer account) {
        this.setTransferDate(transferdate);
        this.setAccount(account);
    }

    public Date getTransferDate() {
        return transferdate != null ? (Date) transferdate.clone() : null;
    }

    public void setTransferDate(Date date) {
        this.transferdate = date != null ? (Date) date.clone() : null;
    }

}