package org.clematis.mt.model;

import java.io.Serializable;

import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Anton Troshin
 */
@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyDeltaKey implements Serializable {

    private int mois;

    private int an;

    private String code;
}
