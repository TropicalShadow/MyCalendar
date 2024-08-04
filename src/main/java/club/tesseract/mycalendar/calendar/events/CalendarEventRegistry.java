package club.tesseract.mycalendar.calendar.events;

import club.tesseract.mycalendar.calendar.MinecraftCalendar;
import club.tesseract.mycalendar.calendar.time.RepeatedTimeCondition;
import club.tesseract.mycalendar.calendar.time.SimpleTimedCondition;
import club.tesseract.mycalendar.calendar.time.TimeSpecification;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;


/**
 * Represents the registry of all calendar events.
 */
public class CalendarEventRegistry {

    public static final CalendarEvent NEW_YEAR = new CalendarEvent(
            "new_year",
            new SimpleTimedCondition(10L), // New Year's Day
            time -> {
                Audience.audience(Bukkit.getOnlinePlayers())
                        .sendMessage(Component.text("Happy New Year!"));
                Bukkit.getConsoleSender().sendMessage(Component.text("Happy New Year!"));
            }
    );
    public static final CalendarEvent WEEKEND_EVENT = new CalendarEvent(
            "new_month",
            new RepeatedTimeCondition(0L, TimeSpecification.MONTH, TimeSpecification.YEAR),
            time -> {
                Component message = Component.text("It's ")
                        .append(Component.text(MinecraftCalendar.getMonth()));


                Audience.audience(Bukkit.getOnlinePlayers())
                                .sendMessage(message);
                Bukkit.getConsoleSender().sendMessage(message);
            }
    );

    private static final Map<String, CalendarEvent> calendarEventMap = new ConcurrentHashMap<>();


    static {
        register(NEW_YEAR);
        register(WEEKEND_EVENT);
    }

    private static void register(CalendarEvent calendarEvent) throws IllegalArgumentException {
        if (calendarEventMap.containsKey(calendarEvent.getId())) {
            throw new IllegalArgumentException(
                    "Event with id " + calendarEvent.getId() + " already exists."
            );
        }
        calendarEventMap.put(calendarEvent.getId(), calendarEvent);
        EventManager.getInstance().schedule(calendarEvent);
    }

    public static CalendarEvent getEvent(String id) {
        return calendarEventMap.get(id);
    }

    public static Collection<CalendarEvent> getEvents() {
        return calendarEventMap.values();
    }
}
