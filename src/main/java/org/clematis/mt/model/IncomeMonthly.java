package org.clematis.mt.model;

import java.io.Serializable;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Anton Troshin
 */
@Entity
@Table(name = "MONTHLY_INCOME")
@SuppressFBWarnings
public class IncomeMonthly implements Serializable {

    @Getter
    @EmbeddedId
    IncomeMonthlyKey key;

    @Setter
    @Getter
    private String code;

    private Double totalConverted;

    private Double total;

    @Setter
    private String commodity;

    public int getMois() {
        return key.getMois();
    }

    @JsonProperty("year")
    public int getAn() {
        return key.getAn();
    }

    public double getTotal() {
        return total;
    }

    public double getTotalConverted() {
        return totalConverted;
    }

    public int getCommId() {
        return key.getCommId();
    }

    @JsonProperty("name")
    public String getCommodity() {
        return this.commodity;
    }

    @JsonProperty("month")
    public void setMois(int mois) {
        key.setMois(mois);
    }

    public void setAn(int an) {
        key.setAn(an);
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public void setTotalConverted(double total) {
        this.totalConverted = total;
    }

    public void setCommId(int commId) {
        this.key.setCommId(commId);
    }

}
