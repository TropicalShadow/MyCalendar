package club.tesseract.mycalendar.calendar.time;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;


/**
 * Represents a time specification for a calendar event.
 */
@AllArgsConstructor
public enum TimeSpecification {
    YEAR(8928000L), // 446,400 seconds in a year
    MONTH(744000L), // 37,200 seconds in a month
    WEEK(168000L),   // 8,400 total ticks in a week (7 days)
    DAY(24000L), // 1,200 seconds in a day
    HOUR(1000L), // every 50 seconds, 20 ticks per second

    MINECRAFT_DAY(24000L), // 20 minutes in a Minecraft day
    MINECRAFT_HOUR(1000L), // 50 seconds in a Minecraft hour

    NON(-1L);

    @Getter(onMethod_ = {@NotNull})
    private final long time;

}
