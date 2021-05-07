package org.clematis.mt.model.views;

import java.io.Serializable;

import javax.persistence.Embeddable;

import lombok.Getter;
import lombok.Setter;
/**
 * @author Anton Troshin
 */
@Embeddable
@Getter
@Setter
public class AccountTotalKey implements Serializable {

    private Integer account;

    private String name;

    private Double balance;

    private Double total;

    private String code;
}