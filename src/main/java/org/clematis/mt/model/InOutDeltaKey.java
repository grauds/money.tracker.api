package org.clematis.mt.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Anton Troshin
 */
@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InOutDeltaKey implements Serializable {

    @Column(name = "COMM_ID") // <-- for the sake of checkstyle java members names check
    private int commId;

    private int mid;
}
