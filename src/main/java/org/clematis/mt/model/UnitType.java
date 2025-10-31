package org.clematis.mt.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

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
