package club.tesseract.mycalendar.calendar.time;

import java.util.ArrayList;
import java.util.List;

/**
 * Notifies listeners of time changes.
 */
public class TimeNotifier {

    private static final List<TimeChangeListener> listeners = new ArrayList<>();

    /**
     * Add a time change listener.
     *
     * @param listener the listener to add.
     */
    public static void addTimeChangeListener(TimeChangeListener listener) {
        synchronized (listeners) {
            listeners.add(listener);
        }
    }

    /**
     * Remove a time change listener.
     *
     * @param listener the listener to remove.
     */
    public static void removeTimeChangeListener(TimeChangeListener listener) {
        synchronized (listeners) {
            listeners.remove(listener);
        }
    }

    /**
     * Notify all listeners of a time change.
     *
     * @param priorTicks the ticks elapsed before the change.
     * @param totalTicks the total ticks elapsed.
     * @param forced true if the time change was forced, false otherwise.
     */
    public static void notifyTimeChange(long priorTicks, long totalTicks, boolean forced) {
        for (TimeChangeListener listener : listeners) {
            listener.onTimeChange(priorTicks, totalTicks, forced);
        }
    }

}
