package org.clematis.mt.model;

import javax.persistence.Column;
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
public class UnitType extends VersionedEntity {

    @Column(name = "SHORTNAME")
    private String shortName;

    @Column(name = "RECALCATTR")
    private Integer recalcAttr;

}
