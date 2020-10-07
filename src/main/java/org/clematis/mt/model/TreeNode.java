package org.clematis.mt.model;

import java.util.Collection;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.Setter;

/**
 * Class to support tree like structures
 *
 * @author Anton Troshin
 * @param <T>
 */
@MappedSuperclass
public class TreeNode<T extends IdAware> extends IdAware {

    @Setter
    @Getter
    @ManyToOne
    @JoinColumn(name = "PARENT")
    private T parent;

    @Setter
    @Getter
    @OneToMany(mappedBy = "parent")
    private Collection<T> children;
}
