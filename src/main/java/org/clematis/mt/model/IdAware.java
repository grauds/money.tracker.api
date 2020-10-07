package org.clematis.mt.model;

import javax.persistence.Id;
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
public class IdAware {

    @Id
    private Long id;

    private Integer objversion;
}
