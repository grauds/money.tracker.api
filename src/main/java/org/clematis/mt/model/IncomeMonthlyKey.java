package org.clematis.mt.model;

import java.io.Serial;
import java.io.Serializable;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
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

    @Serial
    private static final long serialVersionUID = -3952895633364699023L;

    private int mois;

    private int an;

    @Column(name = "COMM_ID")
    private int commId;
}
