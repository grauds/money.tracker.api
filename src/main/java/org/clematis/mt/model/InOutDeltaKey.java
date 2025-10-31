package org.clematis.mt.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
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
@SuppressFBWarnings
public class InOutDeltaKey implements Serializable {

    @Column(name = "COMM_ID") // <-- for the sake of checkstyle java members names check
    private int commId;

    private int mid;
}
