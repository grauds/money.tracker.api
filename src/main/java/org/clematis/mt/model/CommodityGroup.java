package org.clematis.mt.model;

import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
/**
 * For the table
 *
 * {code}
 * create table COMMGROUP
 * (
 * 	ID INTEGER not null
 * 		constraint PK_COMMGROUP
 * 			primary key,
 * 	NAME VARCHAR(64) not null,
 * 	PARENT INTEGER
 * 		constraint FK_COMMGROUP_PARENT
 * 			unique
 * 		constraint FK_COMMGROUP_PARENT
 * 			references COMMGROUP
 * 				on update cascade on delete cascade,
 * 	OBJVERSION INTEGER,
 * 	NAME_MIR VARCHAR(64),
 * 	PARENT_ID NUMERIC(18),
 * 	constraint COMMGROUP_NAME
 * 		unique (NAME_MIR, PARENT)
 * );
 * {code}
 *
 * @author Anton Troshin
 */
@Entity
@Getter
@Setter
@Table(name = "COMMGROUP")
public class CommodityGroup extends TreeNode<CommodityGroup> {

    @OneToMany(mappedBy = "parent")
    private Collection<Commodity> commodities;
}
