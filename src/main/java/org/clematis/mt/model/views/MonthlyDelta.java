package org.clematis.mt.model.views;


import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Anton Troshin
 */
@Table(name = "MONTHLY_DELTA")
@Entity
@Getter
@Setter
public class MonthlyDelta implements Serializable {

    @EmbeddedId MonthlyDeltaKey key;
}
