package org.clematis.mt.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.MappedSuperclass;

/**
 * Base class for entities with identifier and version
 *
 * @author Anton Troshin
 */
@Getter
@Setter
@MappedSuperclass
public class VersionedEntity extends IdAware {

    private Integer objversion;
}
