package org.clematis.mt.model;

import java.io.Serializable;

import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

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
public class IdAware implements Serializable {

    @Id
    private int id;
}
