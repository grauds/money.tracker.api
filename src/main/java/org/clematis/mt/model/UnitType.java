package org.clematis.mt.model;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Anton Troshin
 */
@Entity
@Table(name = "UNITTYPE")
@Getter
@Setter
public class UnitType extends IdAware {

    private String shortName;

    private Integer recalcAttr;

}
