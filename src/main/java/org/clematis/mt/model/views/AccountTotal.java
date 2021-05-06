package org.clematis.mt.model.views;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
/**
 * @author Anton Troshin
 */
@Table(name = "ACCOUNTS_TOTAL_LAST")
@Entity
public class AccountTotal {

    @EmbeddedId AccountTotalKey key;
}