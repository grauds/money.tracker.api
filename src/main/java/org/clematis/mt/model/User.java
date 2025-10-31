package org.clematis.mt.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * Users as actors initiating financial operations
 * @author Anton Troshin
 */
@Entity
@Table(name = "USERMT")
public class User extends VersionedEntity {

}
