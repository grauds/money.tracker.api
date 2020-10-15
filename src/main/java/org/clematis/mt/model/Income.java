package org.clematis.mt.model;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * For the table
 *
 * {code}
 * create table INCOME
 * (
 * 	ID INTEGER not null,
 * 	TRANSFERDATE DATE(10) not null
 * 		constraint INCOME_IDX_TRANSFER_DATE
 * 			unique,
 * 	USERMT INTEGER not null
 * 		constraint INTEG_118
 * 			unique
 * 		constraint INTEG_118
 * 			references USERMT
 * 				on update cascade,
 * 	TOTALITEMS DOUBLE PRECISION(15),
 * 	OBJVERSION INTEGER,
 * 	ACCOUNT INTEGER not null
 * 		constraint FK_INCOME_ACCOUNT
 * 			unique
 * 		constraint FK_INCOME_ACCOUNT
 * 			references ACCOUNT
 * 				on update cascade,
 * 	MONEYTYPE INTEGER
 * 		constraint FK_INCOME_MONEYTYPE
 * 			unique
 * 		constraint FK_INCOME_MONEYTYPE
 * 			references MONEYTYPE
 * 				on update cascade,
 * 	COMMONTRADEPLACE INTEGER
 * 		constraint FK_INCOME_CTP
 * 			unique
 * 		constraint FK_INCOME_CTP
 * 			references ORGANIZATION
 * 				on update cascade,
 * 	TOTAL DOUBLE PRECISION(15),
 * 	REMARKS VARCHAR(512)
 * );
 *
 * create unique index RDB$PRIMARY21
 * 	on INCOME (ID);
 *
 * alter table INCOME
 * 	add constraint INTEG_117
 * 		primary key (ID);
 *
 * @author Anton Troshin
 */
@Entity
@Getter
@Setter
@Table(name = "INCOME")
public class Income extends IdAware {

    private Date transferdate;

    private Double totalitems;

    @ManyToOne
    @JoinColumn(name = "ACCOUNT")
    private Account account;

    @ManyToOne
    @JoinColumn(name = "MONEYTYPE")
    private MoneyType moneyType;

    @ManyToOne
    @JoinColumn(name = "COMMONTRADEPLACE")
    private Organization commontradeplace;

    private Double total;

    private String remarks;

    @Setter
    @Getter
    @OneToMany(mappedBy = "income")
    private Collection<IncomeItem> items;

    public Date getTransferDate() {
        return (Date) transferdate.clone();
    }

    public void setTransferDate(Date date) {
        this.transferdate = (Date) date.clone();
    }
}
