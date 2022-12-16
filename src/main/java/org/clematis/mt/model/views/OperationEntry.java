package org.clematis.mt.model.views;

import java.util.Objects;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Anton Troshin
 */
@Table(name = "ALL_OPERATIONS_HISTORY")
@Entity
@Getter
@Setter
public class OperationEntry {

    @EmbeddedId OperationEntryKey key = new OperationEntryKey();

    private Double amount;

    private Integer moneySign;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OperationEntry that = (OperationEntry) o;
        return Objects.equals(key, that.key)
                && Objects.equals(amount, that.amount)
                && Objects.equals(moneySign, that.moneySign);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, amount, moneySign);
    }
}