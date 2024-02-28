package org.clematis.mt.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.Getter;
import lombok.Setter;

/**
 * For the table
 *
 * {code}
 * create table INCOMEITEM
 * (
 * 	ID INTEGER not null,
 * 	REMARKS VARCHAR(4096),
 * 	INCOME INTEGER not null
 * 		constraint INTEG_123
 * 			unique
 * 		constraint INTEG_123
 * 			references INCOME
 * 				on update cascade on delete cascade,
 * 	COMM INTEGER
 * 		constraint FK_INCOMEITEM_COMM
 * 			unique
 * 		constraint FK_INCOMEITEM_COMM
 * 			references COMMODITY
 * 				on update cascade,
 * 	TOTAL DOUBLE PRECISION(15),
 * 	TRADEPLACE INTEGER not null
 * 		constraint FK_INCOMEITEM_TP
 * 			unique
 * 		constraint FK_INCOMEITEM_TP
 * 			references ORGANIZATION
 * 				on update cascade,
 * 	TRANSFERDATE DATE(10)
 * 		constraint INCOMEITEM_DATE_IDX
 * 			unique,
 * 	QTY DOUBLE PRECISION(15) not null,
 * 	PRICE DOUBLE PRECISION(15),
 * 	IDX INTEGER,
 * 	constraint INCOME_ITEM_LAST_PRICE
 * 		unique (COMM, TRADEPLACE, TRANSFERDATE, ID)
 * );
 *
 * create unique index RDB$PRIMARY23
 * 	on INCOMEITEM (ID);
 *
 * alter table INCOMEITEM
 * 	add constraint INTEG_122
 * 		primary key (ID);
 * {code}
 *
 * @author Anton Troshin
 */
@Entity
@Getter
@Setter
@Table(name = "INCOMEITEM")
@SuppressFBWarnings
public class IncomeItem extends IdAware {

    private Double qty;

    private Double price;

    private String remarks;

    @ManyToOne
    @JoinColumn(name = "INCOME")
    private Income income;

    @ManyToOne
    @JoinColumn(name = "COMM")
    private Commodity commodity;

    private Double total;

    @ManyToOne
    @JoinColumn(name = "TRADEPLACE")
    private Organization tradeplace;

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
