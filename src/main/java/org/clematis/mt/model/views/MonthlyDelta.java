package org.clematis.mt.model.views;


import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Anton Troshin
 */
@Table(name = "MONTHLY_DELTA")
@Entity
public class MonthlyDelta implements Serializable {

    @EmbeddedId MonthlyDeltaKey key;

    public String getCode() {
        return this.key.getCode();
    }

    public int getYear() {
        return this.key.getAn();
    }

    public int getMonth() {
        return this.key.getMois();
    }

    public double getDelta() {
        return this.key.getDelta();
    }
}
