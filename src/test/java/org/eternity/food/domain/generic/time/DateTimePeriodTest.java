package org.eternity.food.domain.generic.time;

import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DateTimePeriodTest {
    @Test
    public void 기간_범위() {
        LocalDateTime when = LocalDateTime.of(2019, 1, 1, 10, 30);

        assertTrue(DateTimePeriod.between(when, when.plusMinutes(1)).contains(when));
        assertTrue(DateTimePeriod.between(when.minusMinutes(1), when).contains(when));
        assertFalse(DateTimePeriod.between(when.plusMinutes(1), when.plusMinutes(2)).contains(when));
        assertFalse(DateTimePeriod.between(when.minusMinutes(2), when.minusMinutes(1)).contains(when));
    }
}
