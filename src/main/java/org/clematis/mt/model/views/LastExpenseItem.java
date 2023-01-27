package org.clematis.mt.model.views;


import java.util.Date;
import java.util.Objects;

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

    String unit;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LastExpenseItem that = (LastExpenseItem) o;
        return commId == that.commId
                && daysAgo == that.daysAgo
                && Objects.equals(name, that.name)
                && Objects.equals(price, that.price)
                && Objects.equals(currency, that.currency)
                && Objects.equals(qty, that.qty)
                && Objects.equals(unit, that.unit)
                && Objects.equals(transactionDate, that.transactionDate)
                && Objects.equals(org, that.org);
    }

    @Override
    public int hashCode() {
        return Objects.hash(commId, name, price, currency, qty, unit, transactionDate, org, daysAgo);
    }
}
