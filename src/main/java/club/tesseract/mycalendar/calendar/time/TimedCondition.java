package club.tesseract.mycalendar.calendar.time;

import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a condition that is based on time.
 */
public interface TimedCondition {


    /**
     * Tests the condition based on the given time.
     * <p>
     *     The time is the total ticks elapsed.
     *     The condition is met if the time is in the list of times.
     *     The time is modulo'd by the total ticks in a year.
     * </p>
     *
     * @param time the total ticks elapsed
     * @return true if the condition is met, false otherwise
     */
    default boolean test(long time) {
        long timeMod = time % TimeSpecification.YEAR.getTime();
        return getTimes().contains(timeMod);
    }


    /**
     * Get the times that this condition is based on.
     *
     * @return the times that this condition is based on
     */
    @NotNull
    List<Long> getTimes();


    /**
     * Finds the next scheduled time after the given time.
     *
     * @param time the current tick time
     * @return the next scheduled tick time, or null if no future times are found
     */
    @Nullable
    default Long getNextScheduledTime(long time) {
        long currentModTime = time % TimeSpecification.YEAR.getTime();
        return getTimes().stream()
                .filter(t -> t > currentModTime)
                .min(Long::compareTo)
                .orElse(null);
    }
}
