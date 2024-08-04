package club.tesseract.mycalendar.calendar.time;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a condition that is based on time.
 */
@RequiredArgsConstructor
public class SimpleTimedCondition implements TimedCondition {

    private final long time;


    @Override
    public boolean test(long time) {
        return this.time == time;
    }

    @Override
    public @NotNull List<Long> getTimes() {
        return List.of(time);
    }
}
