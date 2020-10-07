package org.clematis.mt.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
/**
 * For the table
 *
 * {code}
 * create table ACCOUNT
 * (
 * 	ID INTEGER not null
 * 		constraint PK_ACCOUNT
 * 			primary key,
 * 	NAME VARCHAR(64) not null,
 * 	BALANCE DOUBLE PRECISION(15),
 * 	NOTES VARCHAR(64),
 * 	OBJVERSION INTEGER,
 * 	MONEYTYPE INTEGER not null
 * 		constraint FK_ACCOUNT_MONEYTYPE
 * 			unique
 * 		constraint FK_ACCOUNT_MONEYTYPE
 * 			references MONEYTYPE
 * 				on update cascade,
 * 	LASTCORRECTBALCOMM INTEGER
 * 		constraint FK_ACCOUNT_LCBCOMM
 * 			unique
 * 		constraint FK_ACCOUNT_LCBCOMM
 * 			references COMMODITY
 * 				on update cascade on delete set null,
 * 	LASTCORRECTBALORG INTEGER
 * 		constraint FK_ACCOUNT_LCBORG
 * 			unique
 * 		constraint FK_ACCOUNT_LCBORG
 * 			references ORGANIZATION
 * 				on update cascade on delete set null,
 * 	TOTALINVALIDDATE DATE(10),
 * 	PARENT INTEGER
 * 		constraint FK_ACCOUNT_PARENT
 * 			unique
 * 		constraint FK_ACCOUNT_PARENT
 * 			references ACCOUNTGROUP
 * 				on update cascade,
 * 	NAME_MIR VARCHAR(64)
 * 		constraint ACCOUNT_NAME
 * 			unique
 * );
 * {code}
 *
 * @author Anton Troshin
 */
@Entity
@Getter
@Setter
@Table(name = "ACCOUNT")
public class Account extends IdAware {

    private String name;

    private String notes;

    private Double balance;

    private Date totalInvalidDate;

    @ManyToOne
    @JoinColumn(name = "PARENT")
    private AccountGroup parent;

    public Date getTotalInvalidDate() {
        return (Date) totalInvalidDate.clone();
    }

    public void setTotalInvalidDate(Date date) {
        this.totalInvalidDate = (Date) date.clone();
    }
}
