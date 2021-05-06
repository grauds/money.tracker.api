package org.clematis.mt.model.views;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
/**
 * @author Anton Troshin
 */
@Table(name = "ALL_OPERATIONS_HISTORY")
@Entity
public class OperationEntry {

    @EmbeddedId OperationEntryKey key;

}