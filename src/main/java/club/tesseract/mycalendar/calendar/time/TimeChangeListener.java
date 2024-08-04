package club.tesseract.mycalendar.calendar.time;

/**
 * Represents a listener for time changes.
 */
public interface TimeChangeListener {

    /**
     * Called when the time changes.
     *
     * @param priorElapsed the elapsed time before the change.
     * @param elapsed the elapsed time after the change.
     * @param forced true if the time change was forced, false otherwise.
     */
    void onTimeChange(long priorElapsed, long elapsed, boolean forced);

}
