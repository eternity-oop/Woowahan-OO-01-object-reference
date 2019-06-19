package org.eternity.food.domain.generic.time;

import java.time.LocalDateTime;

public class DateTimePeriod {
    private LocalDateTime from;
    private LocalDateTime to;

    public static DateTimePeriod between(LocalDateTime from, LocalDateTime to) {
        return new DateTimePeriod(from, to);
    }

    public DateTimePeriod(LocalDateTime from, LocalDateTime to) {
        this.from = from;
        this.to = to;
    }

    DateTimePeriod() {
    }

    public boolean contains(LocalDateTime datetime) {
        return (datetime.isAfter(from) || datetime.equals(from)) &&
               (datetime.isBefore(to) || datetime.equals(to));
    }
}
