package org.clematis.mt.model;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Users as actors initiating financial operations
 * @author Anton Troshin
 */
@Entity
@Table(name = "USERMT")
public class User extends VersionedEntity {

}
