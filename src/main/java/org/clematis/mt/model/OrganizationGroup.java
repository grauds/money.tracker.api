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
 * create table ORGANIZATIONGROUP
 * (
 * 	ID INTEGER not null
 * 		constraint PK_ORGANIZATIONGROUP
 * 			primary key,
 * 	NAME VARCHAR(64) not null,
 * 	PARENT INTEGER
 * 		constraint FK_ORGANIZATIONGROUP_PARENT
 * 			unique
 * 		constraint FK_ORGANIZATIONGROUP_PARENT
 * 			references ORGANIZATIONGROUP
 * 				on update cascade,
 * 	OBJVERSION INTEGER,
 * 	NAME_MIR VARCHAR(64),
 * 	constraint ORGANIZATIONGROUP_NAME
 * 		unique (NAME_MIR, PARENT)
 * );
 * {code}
 *
 * @author Anton Troshin
 */
@Entity
@Table(name = "ORGANIZATIONGROUP")
@SuppressFBWarnings
public class OrganizationGroup extends TreeNode<OrganizationGroup> {

    @Setter
    @Getter
    @OneToMany(mappedBy = "parent")
    private Collection<Organization> organizations;
}
