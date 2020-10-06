package org.clematis.mt.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class IdAware {

    @Id
    @Getter
    @Setter
    private Long id;
}
