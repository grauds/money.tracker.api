package org.clematis.mt.model.views;

import java.io.Serializable;

import javax.persistence.Embeddable;

import lombok.Getter;
import lombok.Setter;
/**
 * @author Anton Troshin
 */
@Embeddable
@Getter
@Setter
public class MonthlyDeltaKey implements Serializable {

    private int mois;

    private int an;

    private double delta;

    private String code;
}
