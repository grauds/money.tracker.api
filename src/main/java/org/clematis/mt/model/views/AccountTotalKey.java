package org.clematis.mt.model.views;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.clematis.mt.model.Account;
/**
 * @author Anton Troshin
 */
@Embeddable
public class AccountTotalKey implements Serializable {

    @ManyToOne
    @JoinColumn(name = "ACCOUNT")
    private Account account;

    private String name;

    private Double balance;

    private Double total;

    private String code;
}