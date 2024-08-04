package club.tesseract.mycalendar.test.event;

import club.tesseract.mycalendar.calendar.MinecraftCalendar;
import club.tesseract.mycalendar.calendar.events.CalendarEvent;
import club.tesseract.mycalendar.calendar.events.EventManager;
import club.tesseract.mycalendar.calendar.time.RepeatedTimeCondition;
import club.tesseract.mycalendar.calendar.time.SimpleTimedCondition;
import club.tesseract.mycalendar.calendar.time.TimeSpecification;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

public class EventManagerTest {

    private EventManager eventManager;
    private CalendarEvent event;
    private CalendarEvent repeatEvent;
    private Runnable eventCallback;
    private Runnable repeatEventCallback;
    private AtomicInteger repeatEventCounter;

    @BeforeEach
    public void setUp() {
        eventManager = EventManager.getInstance();
        repeatEventCounter = new AtomicInteger(0);
        eventCallback = mock(Runnable.class);
        repeatEventCallback = mock(Runnable.class);
        event = new CalendarEvent("test",
                new SimpleTimedCondition(10),
                (time) -> {
                    eventCallback.run();
                    System.out.println("Event triggered!");
                });
        repeatEvent = new CalendarEvent("repeat",
                new RepeatedTimeCondition(10, TimeSpecification.YEAR.getTime()),
                (time) -> {
                    repeatEventCallback.run();
                    repeatEventCounter.incrementAndGet();
                });
    }

    @AfterEach
    public void tearDown() {
        // Clean up or reset the event manager if needed
        //eventManager.clearAllEvents();  // Assuming there's a method to clear events
    }

    @Test
    public void testEventSchedulingAndTriggering() {
        // Schedule the event
        eventManager.schedule(event);

        // Trigger the event
        eventManager.checkAndTriggerEvents(10);

        // Verify that the callback was executed
        verify(eventCallback).run();
    }


    @Test
    public void testRepeatedEventSchedulingAndTriggering() {
        // Schedule the repeated event
        eventManager.schedule(repeatEvent);

        for (long i = 0; i < TimeSpecification.YEAR.getTime(); i += 10) {
            // Trigger the event
            if ((i % TimeSpecification.MONTH.getTime()) == 0) {
                System.out.println("Month " + i / TimeSpecification.MONTH.getTime());
            }
            eventManager.checkAndTriggerEvents(i);
        }

        // Verify that the callback was executed
        verify(repeatEventCallback, times(372)).run();
        System.out.println("Event triggered " + repeatEventCounter.get() + " times.");
        Assertions.assertEquals(372, repeatEventCounter.get());
    }
}
