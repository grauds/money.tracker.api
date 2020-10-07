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
 * create table ORGANIZATION
 * (
 * 	ID INTEGER not null
 * 		constraint PK_ORGANIZATION
 * 			primary key,
 * 	NAME VARCHAR(128) not null,
 * 	OBJVERSION INTEGER,
 * 	EXCHANGEFEE DOUBLE PRECISION(15),
 * 	ADDRESS VARCHAR(1024),
 * 	URL VARCHAR(256),
 * 	EMAIL VARCHAR(64),
 * 	PHONE VARCHAR(128),
 * 	FAX VARCHAR(128),
 * 	FULLNAME VARCHAR(128),
 * 	REMARKS VARCHAR(4096),
 * 	PARENT INTEGER
 * 		constraint FK_ORGANIZATION_PARENT
 * 			unique
 * 		constraint FK_ORGANIZATION_PARENT
 * 			references ORGANIZATIONGROUP
 * 				on update cascade,
 * 	NAME_MIR VARCHAR(128)
 * 		constraint ORGANIZATION_NAME
 * 			unique
 * );
 * {code}
 *
 * @author Anton Troshin
 */
@Entity
@Table(name = "ORGANIZATION")
@Getter
@Setter
public class Organization extends IdAware {

    private String name;

    private String address;

    private String url;

    private String email;

    private String phone;

    private String fax;

    private String fullname;

    private String remarks;

    @ManyToOne
    @JoinColumn(name = "PARENT")
    private OrganizationGroup parent;
}
