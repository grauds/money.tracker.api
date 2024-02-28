package org.clematis.mt.model;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.Table;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Anton Troshin
 */
@Table(name = "ACCOUNTS_TOTAL_LAST")
@Entity
@Getter
@Setter
@SuppressFBWarnings
public class AccountTotal extends NamedEntity {

    private Double balance;

    private Double total;

    private String code;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AccountTotal that = (AccountTotal) o;
        return Objects.equals(balance, that.balance)
                && Objects.equals(total, that.total)
                && Objects.equals(code, that.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(balance, total, code);
    }
}
