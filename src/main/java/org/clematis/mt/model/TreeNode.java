package org.clematis.mt.model;

import java.util.Collection;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.OneToMany;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.Getter;
import lombok.Setter;

/**
 * Class to support tree like structures
 *
 * @author Anton Troshin
 * @param <T>
 */
@MappedSuperclass
@SuppressFBWarnings
public class TreeNode<T extends IdAware> extends NamedEntity {

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
