package org.clematis.mt.model;

import jakarta.persistence.MappedSuperclass;

import lombok.Getter;
import lombok.Setter;

/**
 * Base class for entities with a name
 *
 * @author Anton Troshin
 */
@Getter
@Setter
@MappedSuperclass
public class NamedEntity extends IdAware {

    private String name;
}
