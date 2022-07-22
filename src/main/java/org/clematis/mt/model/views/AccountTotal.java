package org.clematis.mt.model.views;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.clematis.mt.model.NamedEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Anton Troshin
 */
@Table(name = "ACCOUNTS_TOTAL_LAST")
@Entity
@Getter
@Setter
public class AccountTotal extends NamedEntity {

    private Double balance;

    private Double total;

    private String code;
}
