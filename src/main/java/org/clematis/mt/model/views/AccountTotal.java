package org.clematis.mt.model.views;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
/**
 * @author Anton Troshin
 */
@Table(name = "ACCOUNTS_TOTAL_LAST")
@Entity
@Getter
@Setter
public class AccountTotal {

    @EmbeddedId AccountTotalKey key;
}