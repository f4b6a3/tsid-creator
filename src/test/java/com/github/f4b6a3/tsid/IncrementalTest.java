package com.github.f4b6a3.tsid;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Test;

/**
 * TSID must be generated incrementally as per specification but there is a
 * case where this condition is not met. When the factory generates more than the
 * {@code CLOCK_DRIFT_TOLERANCE * 2^(counterBits)} values per millisecond the condition:
 * <br>
 * {@code if ((time > this.lastTime - CLOCK_DRIFT_TOLERANCE) && (time <= this.lastTime)) }
 * <br>
 * is not met and the time is not incremented but only the counter. This will cause
 * the generated TSID value to be less than the previous one.
 * <p>
 * The <b>CLOCK_DRIFT_TOLERANCE</b> adjustment is not really needed: if a time glitch should accour
 * the bigger between the actual system time and the incremented internal value will automatically
 * prevail by simply using the line:
 * <br>
 * {@code if (time <= this.lastTime) }
 *
 */
public class IncrementalTest {

    private static class ClockMock extends Clock {

        private Instant instant = Instant.ofEpochSecond(0);

        @Override
        public ZoneId getZone() {
            throw new UnsupportedOperationException("getZone");
        }

        @Override
        public Clock withZone(ZoneId zoneid) {
            throw new UnsupportedOperationException("withZone");
        }

        @Override
        public Instant instant() {
            return instant;
        }

        public void incrementMillis(int millis) {
            this.instant = this.instant.plusMillis(millis);
        }

        public void decrementMillis(int millis) {
            this.instant = this.instant.minusMillis(millis);
        }
    }

    @Test
    public void shouldGenerateIncrementalValuesInCaseOfBackwardAGlitch() {
        ClockMock clock = new ClockMock();

        TsidFactory factory = TsidFactory.builder()
                .withNodeBits(20)
                .withNode(0)
                .withClock(clock)
                .build();

        Tsid prev = factory.create();

        // simulates a drift back of CLOCK_DRIFT_TOLERANCE
        clock.decrementMillis(TsidFactory.CLOCK_DRIFT_TOLERANCE - 1);

        Tsid next = factory.create();

        assertIncremental(prev, next);
    }

    @Test
    public void shouldGenerateIncrementalValuesInCaseOfForwardAGlitch() {
        ClockMock clock = new ClockMock();

        TsidFactory factory = TsidFactory.builder()
                .withNodeBits(20)
                .withNode(0)
                .withClock(clock)
                .build();

        Tsid prev = factory.create();

        // simulates a drift back of CLOCK_DRIFT_TOLERANCE
        clock.incrementMillis(TsidFactory.CLOCK_DRIFT_TOLERANCE - 1);

        Tsid next = factory.create();

        assertIncremental(prev, next);
    }

    @Test
    public void shouldManageAGlitch() {

        ClockMock clock = new ClockMock();

        TsidFactory factory = TsidFactory.builder()
                .withNodeBits(20)
                .withNode(0)
                .withClock(clock)
                .build();

        final int advanceTimeUpTODriftTolerance = TsidFactory.CLOCK_DRIFT_TOLERANCE * 4 - 1;

        long last = Long.MIN_VALUE;
        for (int i=0; i < advanceTimeUpTODriftTolerance; i++) {
            long tsid = factory.create().toLong();
            assertTrue(last < tsid);
            last = tsid;
        }

        // this is the last TSID generated incrementally
        Tsid prev = factory.create();
        assertTrue(last < prev.toLong());

        // this TSID will be out of the time-CLOCK_DRIFT_TOLERANCE window and will not
        // be incremented but only its counter will be incremented (error!)
        Tsid next = factory.create();
        assertIncremental(prev, next);
    }

     @Test
    public void shouldAlwaysBeIncremental() {
        TsidFactory factory = TsidFactory.builder()
                .withNodeBits(20)
                .withNode(0)
                .build();

        long last = 0;
        for (int i=0; i<1_000_000; i++) {
            long tsid = factory.create().toLong();
            if (last != 0 && tsid < last) {
                fail(("generated TSID value is less that the previous one:\n" +
                     "   iteration: %d\n" +
                     "   previous: %s  long= %d\n" +
                     "   actual  : %s  long= %d\n")
                             .formatted(i,
                                     Tsid.from(last).toString(), last,
                                     Tsid.from(tsid).toString(), tsid));
            }
            last = tsid;
        }
    }

    private void assertIncremental(Tsid prev, Tsid next) {
        assertTrue(
                ("generated TSID value is less that the previous one:\n" +
                "   previous: %s  long= %d\n" +
                "   actual  : %s  long= %d\n")
                    .formatted(prev, prev.toLong(),next, next.toLong()),
                prev.toLong() < next.toLong());

    }
}
