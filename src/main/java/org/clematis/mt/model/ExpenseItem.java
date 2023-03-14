package org.clematis.mt.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

/**
 * For the table
 *
 * {code}
 * create table EXPENSEITEM
 * (
 * 	ID INTEGER not null,
 * 	QTY DOUBLE PRECISION(15) not null,
 * 	PRICE DOUBLE PRECISION(15),
 * 	REMARKS VARCHAR(4096),
 * 	EXPENSE INTEGER not null
 * 		constraint INTEG_129
 * 			unique
 * 		constraint INTEG_129
 * 			references EXPENSE
 * 				on update cascade on delete cascade,
 * 	COMM INTEGER
 * 		constraint FK_EXPENSEITEM_COMM
 * 			unique
 * 		constraint FK_EXPENSEITEM_COMM
 * 			references COMMODITY
 * 				on update cascade,
 * 	TOTAL DOUBLE PRECISION(15),
 * 	TRADEPLACE INTEGER not null
 * 		constraint FK_EXPENSEITEM_TP
 * 			unique
 * 		constraint FK_EXPENSEITEM_TP
 * 			references ORGANIZATION
 * 				on update cascade,
 * 	DISC DOUBLE PRECISION(15),
 * 	TRANSFERDATE DATE(10)
 * 		constraint EXPENSEITEM_DATE_IDX
 * 			unique,
 * 	IDX INTEGER,
 * 	constraint EXPENSE_ITEM_LAST_PRICE
 * 		unique (COMM, TRADEPLACE, TRANSFERDATE, ID)
 * );
 *
 * create unique index RDB$PRIMARY25
 * 	on EXPENSEITEM (ID);
 *
 * alter table EXPENSEITEM
 * 	add constraint INTEG_128
 * 		primary key (ID);
 * {code}
 *
 * @author Anton Troshin
 */
@Entity
@Getter
@Setter
@Table(name = "EXPENSEITEM")
public class ExpenseItem extends IdAware {

    private Double qty;

    private Double price;

    private String remarks;

    @ManyToOne
    @JoinColumn(name = "EXPENSE")
    private Expense expense;

    @ManyToOne()
    @JoinColumn(name = "COMM")
    private Commodity commodity;

    private Double total;

    @ManyToOne
    @JoinColumn(name = "TRADEPLACE")
    private Organization tradeplace;

    private Double disc;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Date transferdate;

    private Integer idx;

    public Date getTransferDate() {
        return transferdate != null ? (Date) transferdate.clone() : null;
    }

    public void setTransferDate(Date date) {
        this.transferdate = date != null ? (Date) date.clone() : null;
    }

}
