package org.clematis.mt.model;

import java.util.Collection;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
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
@SuppressFBWarnings
public class CommodityGroup extends TreeNode<CommodityGroup> {

    @OneToMany(mappedBy = "parent")
    private Collection<Commodity> commodities;
}
