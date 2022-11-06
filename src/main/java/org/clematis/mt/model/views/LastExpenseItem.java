package org.clematis.mt.model.views;


import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.clematis.mt.model.IdAware;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

/**
 * A record about one of the expenses, for logging purposes
 * @author Anton Troshin
 */
@Table(name = "LAST_EXPENSEITEMS")
@Entity
@Getter
@Setter
public class LastExpenseItem extends IdAware {

    long commId;

    String name;

    Double price;

    String currency;

    Double qty;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    Date transactionDate;

    String org;

    int daysAgo;

    public Date getTransactionDate() {
        return transactionDate != null ? (Date) transactionDate.clone() : null;
    }

    public void setTransactionDate(Date date) {
        this.transactionDate = date != null ? (Date) date.clone() : null;
    }

}
