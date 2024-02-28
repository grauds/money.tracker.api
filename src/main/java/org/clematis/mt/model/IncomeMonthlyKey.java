package org.clematis.mt.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
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
@SuppressFBWarnings
public class IncomeMonthlyKey implements Serializable {

    private static final long serialVersionUID = -3952895633364699023L;

    private int mois;

    private int an;

    @Column(name = "COMM_ID")
    private int commId;
}
