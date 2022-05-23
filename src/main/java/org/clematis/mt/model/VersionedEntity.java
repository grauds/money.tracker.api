package org.clematis.mt.model;

import javax.persistence.MappedSuperclass;

import lombok.Getter;
import lombok.Setter;

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
