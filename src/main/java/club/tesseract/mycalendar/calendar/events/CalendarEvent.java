package club.tesseract.mycalendar.calendar.events;

import club.tesseract.mycalendar.calendar.time.TimedCondition;
import java.util.function.Consumer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


/**
 * Represents an event that occurs at a specific time in the Minecraft calendar.
 */
@RequiredArgsConstructor
@Getter
public class CalendarEvent {

    protected final String id;
    protected final TimedCondition time;
    protected final Consumer<Long> action;

    public void execute(long time) {
        action.accept(time);
    }

    /**
     * Check if the event should execute at the given time.
     *
     * @param time The total ticks elapsed
     * @return true if the event should execute, false otherwise
     */
    public boolean shouldExecute(long time) {
        return this.time.test(time);
    }
}
