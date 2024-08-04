package club.tesseract.mycalendar.calendar.events;

import com.google.common.collect.ImmutableList;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

/**
 * Manages upcoming events, and executes them when they occur.
 */
public class EventManager {

    @Getter
    @NotNull
    private static final EventManager instance = new EventManager();

    private final List<CalendarEvent> events;

    private LinkedList<ScheduledEvent> scheduledEvents;
    private Map<Long, List<ScheduledEvent>> cachedEvents;

    private EventManager() {
        events = new CopyOnWriteArrayList<>();
        scheduledEvents = new LinkedList<>();
    }

    public void schedule(CalendarEvent event) {
        events.add(event);
        refreshScheduledEvents();
    }

    public LinkedList<ScheduledEvent> getScheduledEvents() {
        return new LinkedList<>(scheduledEvents);
    }

    public void refreshScheduledEvents() {
        scheduledEvents = getEventsAsList();
        cachedEvents = generateCachedEvents();
    }

    /**
     * Get an unmodifiable view of the events.`
     *
     * @return an unmodifiable view of the events
     */
    public List<CalendarEvent> getEvents() {
        // Copy the elements to a list
        return ImmutableList.copyOf(events);
    }

    private Map<Long, List<ScheduledEvent>> generateCachedEvents() {
        Map<Long, List<ScheduledEvent>> cachedEvents = new ConcurrentHashMap<>();
        for (ScheduledEvent event : scheduledEvents) {
            long time = event.time();
            cachedEvents.computeIfAbsent(time, k -> new ArrayList<>()).add(event);
        }
        return cachedEvents;
    }

    /**
     * Get an unmodifiable view of the events as a list in order.
     *
     * @return an unmodifiable view of the events as a list
     */
    private LinkedList<ScheduledEvent> getEventsAsList() {

        return events.stream()
                .flatMap(event -> event.getTime().getTimes().stream()
                        .map(scheduledTime ->
                                new ScheduledEvent(UUID.randomUUID(), event, scheduledTime)))
                .sorted(Comparator.comparingLong(ScheduledEvent::time))
                .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get the next event that will occur given the current total ticks.
     *
     * @param totalTicks the total ticks elapsed
     * @return the next event that will occur
     */
    public CalendarEvent nextEvent(long totalTicks) {
        return getEvents().stream()
                .min((e1, e2) -> {

                    boolean e1Test = e1.getTime().test(totalTicks);
                    boolean e2Test = e2.getTime().test(totalTicks);

                    if (e1Test && e2Test) {
                        return 0;
                    }

                    if (e1Test) {
                        return -1;
                    }

                    return 1;
                })
                .orElse(null); // Return null if there are no events
    }


    /**
     * Check and trigger events that are scheduled to occur at the current time.
     *
     * @param currentTime the total ticks elapsed
     */
    public void checkAndTriggerEvents(long currentTime) {
        cachedEvents.getOrDefault(currentTime, Collections.emptyList())
                .parallelStream()
                .filter(event -> event.event().shouldExecute(currentTime))
                .forEach(event -> event.event().execute(currentTime));
    }

    public void scheduleEvents(long elapsed) {
        CalendarEventRegistry.getEvents().forEach(this::schedule);
    }

}
