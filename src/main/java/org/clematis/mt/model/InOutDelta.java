package org.clematis.mt.model;

import java.io.Serializable;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 *
 * @author Anton Troshin
 */
@Entity
@Table(name = "IN_OUT_DELTA")
@NoArgsConstructor
@AllArgsConstructor
@SuppressFBWarnings
public class InOutDelta implements Serializable {

    @EmbeddedId InOutDeltaKey key;

    @ManyToOne
    @JoinColumn(name = "COMM_ID", insertable = false, updatable = false)
    Commodity commodity;

    Double etotal;

    Double itotal;

    Double delta;

    @ManyToOne
    @JoinColumn(name = "MID", insertable = false, updatable = false)
    MoneyType moneyType;

}
