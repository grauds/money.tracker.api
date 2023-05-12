package org.clematis.mt.model;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Anton Troshin
 */
@Entity
@Table(name = "MONTHLY_INCOME")
public class IncomeMonthly implements Serializable {

    @EmbeddedId
    IncomeMonthlyKey key = new IncomeMonthlyKey();

    private String code;

    private Double totalConverted;

    private Double total;

    public IncomeMonthlyKey getKey() {
        return key;
    }

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

    public String getCode() {
        return code;
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

    public void setCode(String code) {
        this.code = code;
    }

}
