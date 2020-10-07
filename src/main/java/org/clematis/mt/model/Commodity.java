package org.clematis.mt.model;

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
 * create table COMMODITY
 * (
 * 	ID INTEGER not null
 * 		constraint PK_COMMODITY
 * 			primary key,
 * 	NAME VARCHAR(128) not null,
 * 	UNITTYPE INTEGER
 * 		constraint FK_COMMODITY_UNITTYPE
 * 			unique
 * 		constraint FK_COMMODITY_UNITTYPE
 * 			references UNITTYPE
 * 				on update cascade,
 * 	PARENT INTEGER
 * 		constraint FK_COMMODITY_PARENT
 * 			unique
 * 		constraint FK_COMMODITY_PARENT
 * 			references COMMGROUP
 * 				on update cascade,
 * 	OBJVERSION INTEGER,
 * 	DEFAULTTRADEPLACE INTEGER
 * 		constraint FK_COMMODITY_DTP
 * 			unique
 * 		constraint FK_COMMODITY_DTP
 * 			references ORGANIZATION
 * 				on update cascade,
 * 	DEFAULTPRICE DOUBLE PRECISION(15),
 * 	DEFAULTMONEYTYPE INTEGER
 * 		constraint FK_COMMODITY_DMT
 * 			unique
 * 		constraint FK_COMMODITY_DMT
 * 			references MONEYTYPE
 * 				on update cascade,
 * 	DEFAULTQTY DOUBLE PRECISION(15),
 * 	NAME_MIR VARCHAR(128)
 * 		constraint COMMODITY_NAME
 * 			unique,
 * 	REMARKS VARCHAR(512),
 * 	RECALCATTR INTEGER not null,
 * 	PARENT_ID NUMERIC(18)
 * );
 * {code}
 *
 * @author Anton Troshin
 */
@Entity
@Getter
@Setter
@Table(name = "COMMODITY")
public class Commodity extends IdAware {

    private String name;

    @ManyToOne
    @JoinColumn(name = "PARENT")
    private CommodityGroup parent;
}
