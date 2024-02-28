package org.clematis.mt.model;

import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.Getter;
import lombok.Setter;

/**
 * For the table
 *
 * {code}
 * create table ACCOUNTGROUP
 * (
 * 	ID INTEGER not null
 * 		constraint PK_ACCOUNTGROUP
 * 			primary key,
 * 	NAME VARCHAR(64),
 * 	PARENT INTEGER
 * 		constraint FK_ACCOUNTGROUP_PARENT
 * 			unique
 * 		constraint FK_ACCOUNTGROUP_PARENT
 * 			references ACCOUNTGROUP
 * 				on update cascade,
 * 	OBJVERSION INTEGER,
 * 	NAME_MIR VARCHAR(64),
 * 	constraint ACCOUNTGROUP_NAME
 * 		unique (NAME_MIR, PARENT)
 * );
 * {code}
 *
 * @author Anton Troshin
 */
@Entity
@Getter
@Setter
@Table(name = "ACCOUNTGROUP")
@SuppressFBWarnings
public class AccountGroup extends TreeNode<AccountGroup> {

    @OneToMany(mappedBy = "parent")
    private Collection<Account> accounts;
}
