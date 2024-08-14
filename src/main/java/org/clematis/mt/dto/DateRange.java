package org.clematis.mt.dto;

import java.util.Date;
import java.util.Objects;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Anton Troshin
 */
@Getter
@Setter
@SuppressFBWarnings
public class DateRange {

    private Date start;

    private Date end;

    public DateRange(Date start, Date end) {
        this.start = new Date(start.getTime());
        this.end = new Date(end.getTime());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DateRange dateRange = (DateRange) o;
        return Objects.equals(start, dateRange.start) && Objects.equals(end, dateRange.end);
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end);
    }
}
