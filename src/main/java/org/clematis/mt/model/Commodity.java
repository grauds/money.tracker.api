package org.clematis.mt.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "COMMODITY")
public class Commodity extends IdAware {

    @Getter
    @Setter
    private String name;

    @Setter
    @Getter
    @ManyToOne
    @JoinColumn(name = "PARENT")
    private CommodityGroup parent;
}
