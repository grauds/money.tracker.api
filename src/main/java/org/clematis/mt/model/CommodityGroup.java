package org.clematis.mt.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "COMMGROUP")
public class CommodityGroup extends IdAware {

    @Getter
    @Setter
    private String name;

    @Setter
    @Getter
    @ManyToOne
    @JoinColumn(name = "PARENT")
    private CommodityGroup parent;

    @Setter
    @Getter
    @OneToMany(mappedBy = "parent")
    private Collection<CommodityGroup> children;

    @Setter
    @Getter
    @OneToMany(mappedBy = "parent")
    private Collection<Commodity> commodities;
}
