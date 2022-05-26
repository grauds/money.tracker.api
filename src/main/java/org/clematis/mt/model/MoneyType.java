package org.clematis.mt.model;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;


/**
 * For the table
 *
 * {code}
 * create table MONEYTYPE
 * (
 * 	ID INTEGER not null
 * 		constraint PK_MONEYTYPE
 * 			primary key,
 * 	NAME VARCHAR(32) not null,
 * 	CODE VARCHAR(16),
 * 	SIGN VARCHAR(16) not null,
 * 	OBJVERSION INTEGER,
 * 	NAME_MIR VARCHAR(32)
 * 		constraint MONEYTYPE_NAME
 * 			unique
 * );
 * {code}
 *
 * @author Anton Troshin
 */
@Entity
@Getter
@Setter
@Table(name = "MONEYTYPE")
public class MoneyType extends NamedEntity {

    private String code;

    private String sign;
}
