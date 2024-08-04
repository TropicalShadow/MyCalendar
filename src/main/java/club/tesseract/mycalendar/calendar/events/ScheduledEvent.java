package club.tesseract.mycalendar.calendar.events;

import java.util.UUID;

/**
 * Represents an event that is scheduled to occur at a specific time.
 */
public record ScheduledEvent(UUID id, CalendarEvent event, long time) {

}
