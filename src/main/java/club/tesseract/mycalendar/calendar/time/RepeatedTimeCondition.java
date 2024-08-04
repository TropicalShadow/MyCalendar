package club.tesseract.mycalendar.calendar.time;

import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a condition that is satisfied at a specific time.
 */
public class RepeatedTimeCondition implements TimedCondition {

    private final long modulator;
    private final long repeater;

    private final List<Long> times;

    /**
     * Creates a new condition that is satisfied at the given time.
     *
     * @param repeater the repeater for the condition
     * @param modulator the modulator for the condition
     */
    public RepeatedTimeCondition(long repeater, long modulator) {
        this.modulator = modulator;
        this.repeater = repeater;

        this.times = generateTimes();
    }

    /**
     * Creates a new condition that is satisfied at the given time.
     *
     * @param initialOffset the initial offset of the condition
     * @param modulator the modulator for the condition
     */
    public RepeatedTimeCondition(long initialOffset,  TimeSpecification repeater, TimeSpecification modulator) {
        this(repeater.getTime(), modulator.getTime());
    }

    public long getIteration() {
        return modulator / repeater;
    }

    @Override
    public boolean test(long time) {
        return times.contains(time);
    }

    List<Long> generateTimes() {
        ArrayList<Long> times = new ArrayList<>();

        long iteration = getIteration();
        for (int i = 1; i <= iteration; i++) {
            times.add((repeater * i) % modulator);
        }

        return times;
    }

    @Override
    public @NotNull List<Long> getTimes() {
        return ImmutableList.copyOf(times);
    }
}
