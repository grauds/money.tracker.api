package org.clematis.mt.model;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

/**
 * For the table
 *
 * {code}
 * create table EXPENSE
 * (
 * 	ID INTEGER not null,
 * 	TRANSFERDATE DATE(10) not null
 * 		constraint EXPENSE_IDX_TRANSFER_DATE
 * 			unique,
 * 	USERMT INTEGER not null
 * 		constraint INTEG_112
 * 			unique
 * 		constraint INTEG_112
 * 			references USERMT
 * 				on update cascade,
 * 	TOTALITEMS DOUBLE PRECISION(15),
 * 	OBJVERSION INTEGER,
 * 	ACCOUNT INTEGER not null
 * 		constraint FK_EXPENSE_ACCOUNT
 * 			unique
 * 		constraint FK_EXPENSE_ACCOUNT
 * 			references ACCOUNT
 * 				on update cascade,
 * 	MONEYTYPE INTEGER
 * 		constraint FK_EXPENSE_MONEYTYPE
 * 			unique
 * 		constraint FK_EXPENSE_MONEYTYPE
 * 			references MONEYTYPE
 * 				on update cascade,
 * 	DISC DOUBLE PRECISION(15),
 * 	DISCPERCENT DOUBLE PRECISION(15),
 * 	DISCTYPE INTEGER,
 * 	DISCINPRICE INTEGER,
 * 	COMMONTRADEPLACE INTEGER
 * 		constraint FK_EXPENSE_CTP
 * 			unique
 * 		constraint FK_EXPENSE_CTP
 * 			references ORGANIZATION
 * 				on update cascade,
 * 	TOTAL DOUBLE PRECISION(15),
 * 	REMARKS VARCHAR(512)
 * );
 *
 * create unique index RDB$PRIMARY19
 * 	on EXPENSE (ID);
 *
 * alter table EXPENSE
 * 	add constraint INTEG_111
 * 		primary key (ID);
 * {code}
 *
 * @author Anton Troshin
 */
@Entity
@Getter
@Setter
@Table(name = "EXPENSE")
public class Expense extends IdAware {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Date transferdate;

    private Double totalitems;

    @ManyToOne
    @JoinColumn(name = "ACCOUNT")
    private Account account;

    @ManyToOne
    @JoinColumn(name = "MONEYTYPE")
    private MoneyType moneyType;

    private Double disc;

    private Double discpercent;

    private Integer disctype;

    private Integer discinprice;

    @ManyToOne
    @JoinColumn(name = "COMMONTRADEPLACE")
    private Organization commontradeplace;

    private Double total;

    private String remarks;

    @Setter
    @Getter
    @OneToMany(mappedBy = "expense")
    private Collection<ExpenseItem> items;

    public Date getTransferDate() {
        return transferdate != null ? (Date) transferdate.clone() : null;
    }

    public void setTransferDate(Date date) {
        this.transferdate = date != null ? (Date) date.clone() : null;
    }
}
