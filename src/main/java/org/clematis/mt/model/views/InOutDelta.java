package org.clematis.mt.model.views;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.clematis.mt.model.Commodity;
import org.clematis.mt.model.MoneyType;

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
