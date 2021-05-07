package org.clematis.mt.model.views;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
/**
 * @author Anton Troshin
 */
@Table(name = "ALL_OPERATIONS_HISTORY")
@Entity
@Getter
@Setter
public class OperationEntry {

    @EmbeddedId OperationEntryKey key;

}