package org.clematis.mt.model;

import javax.persistence.MappedSuperclass;

import lombok.Getter;
import lombok.Setter;

/**
 * Base class for entities with a name
 *
 */
@Getter
@Setter
@MappedSuperclass
public class NamedEntity extends IdAware {

    private String name;
}
