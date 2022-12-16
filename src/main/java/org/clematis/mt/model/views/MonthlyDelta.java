package org.clematis.mt.model.views;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * @author Anton Troshin
 */
@Entity
@Table(name = "MONTHLY_DELTA")
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyDelta implements Serializable {

    @EmbeddedId MonthlyDeltaKey key = new MonthlyDeltaKey();

    private double delta;

    public MonthlyDeltaKey getKey() {
        return key;
    }

    public int getMois() {
        return key.getMois();
    }

    @JsonProperty("year")
    public int getAn() {
        return key.getAn();
    }

    public double getDelta() {
        return delta;
    }

    public String getCode() {
        return key.getCode();
    }

    @JsonProperty("month")
    public void setMois(int mois) {
        key.setMois(mois);
    }

    public void setAn(int an) {
        key.setAn(an);
    }

    public void setDelta(double delta) {
        this.delta = delta;
    }

    public void setCode(String code) {
        key.setCode(code);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MonthlyDelta that = (MonthlyDelta) o;
        return Double.compare(that.delta, delta) == 0 && Objects.equals(key, that.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, delta);
    }
}
