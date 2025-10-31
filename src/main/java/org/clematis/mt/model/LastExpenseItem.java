package org.clematis.mt.model;


import java.util.Date;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
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
@SuppressFBWarnings
public class LastExpenseItem extends IdAware {

    @ManyToOne
    @JoinColumn(name = "COMM_ID")
    Commodity commodity;

    Double price;

    String currency;

    Double qty;

    String unit;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    Date transactionDate;

    @ManyToOne
    @JoinColumn(name = "ORG_ID")
    Organization organization;

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
        return Objects.equals(commodity, that.commodity)
                && daysAgo == that.daysAgo
                && Objects.equals(price, that.price)
                && Objects.equals(currency, that.currency)
                && Objects.equals(qty, that.qty)
                && Objects.equals(unit, that.unit)
                && Objects.equals(transactionDate, that.transactionDate)
                && Objects.equals(organization, that.organization);
    }

    @Override
    public int hashCode() {
        return Objects.hash(commodity, price, currency, qty, unit, transactionDate, organization, daysAgo);
    }
}
