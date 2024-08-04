package club.tesseract.mycalendar.calendar;

import club.tesseract.mycalendar.MyCalendar;
import club.tesseract.mycalendar.calendar.events.EventManager;
import club.tesseract.mycalendar.calendar.time.TimeNotifier;
import club.tesseract.mycalendar.calendar.time.TimeSpecification;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;


/**
 * Represents the Minecraft calendar.
 */
public final class MinecraftCalendar {
    private static final String DAY_SYMBOL = "☀";
    private static final String NIGHT_SYMBOL = "☽";

    private static final List<String> SEASONS = Arrays.asList("Early Spring", "Spring",
            "Late Spring", "Early Summer", "Summer", "Late Summer", "Early Autumn",
            "Autumn", "Late Autumn", "Early Winter", "Winter", "Late Winter");


    /**
     * The total ticks elapsed.
     */
    @Getter
    @Setter
    private static long elapsed = 0L; // Ticks

    /**
     * initialise the elapsed time for the Minecraft calendar.
     */
    public static void setupElapsed() {
        elapsed = 0L; // TODO - poll from the server

        EventManager.getInstance().scheduleEvents(elapsed);

        Bukkit.getScheduler().runTask(MyCalendar.getPlugin(),
                () -> TimeNotifier.notifyTimeChange(elapsed, elapsed, true));
        Bukkit.getScheduler().scheduleSyncRepeatingTask(
                MyCalendar.getPlugin(),
                MinecraftCalendar::tick, 10L, 10L
        );
    }

    /**
     * Update the elapsed time by the given amount.
     *
     * @param amount the amount to update the elapsed time by.
     */
    public static synchronized void updateElapsed(long amount) {
        long priorElapsed = elapsed;
        elapsed += amount;
        Bukkit.getScheduler().runTaskAsynchronously(MyCalendar.getPlugin(),
                () -> TimeNotifier.notifyTimeChange(priorElapsed, elapsed, amount != 10));
        // TODO - consider if we need to reschedule events?
        // TODO - or remove events if too big of a time jump?
        checkForEvents(elapsed);
    }

    /**
     * Get the current time in ticks.
     *
     * @param time total time elapsed.
     * @return the current time in ticks.
     */
    public static Long getDifferenceInHours(long time) {
        return (getElapsed() - time) / TimeSpecification.HOUR.getTime();
    }

    /**
     * Get the current time in ticks.
     *
     * @return the current time in ticks.
     */
    public static int getMinutes(long time) {
        int currentTime = (int) ((time % TimeSpecification.DAY.getTime()) - 6000);
        if (currentTime < 0) {
            currentTime += (int) TimeSpecification.DAY.getTime();
        }

        currentTime = currentTime % (int) TimeSpecification.HOUR.getTime();
        return currentTime * 60 / ((int) TimeSpecification.HOUR.getTime());
    }

    public static int getYear() {
        return (int) (getElapsed() / TimeSpecification.YEAR.getTime()) + 1;
    }

    public static int getMonth() {
        return ((int) (getElapsed() / TimeSpecification.MONTH.getTime()) % 12) + 1;
    }

    public static int getMonth(long elapsed) {
        return ((int) (elapsed / TimeSpecification.MONTH.getTime()) % 12) + 1;
    }

    public static int getDay() {
        return ((int) (getElapsed() / TimeSpecification.DAY.getTime()) % 31) + 1;
    }

    /**
     * Get the current day of the month.
     *
     * @param elapsed the current ticks that have elapsed.
     * @return the current ordinal day of the month.
     */
    public static int getDay(long elapsed) {
        return ((int) (elapsed / TimeSpecification.DAY.getTime()) % 31) + 1;
    }

    /**
     * Get the current hour of the day.
     *
     * @return the current hour of the day 0-23.
     */
    public static int getHour() {
        int currentTime = (int) ((getElapsed() % TimeSpecification.DAY.getTime()) - 6000);
        if (currentTime < 0) {
            currentTime += (int) TimeSpecification.DAY.getTime(); // Adjust if before 6:00AM
        }
        int hours = 6 + (currentTime / 1000); // 1 hour = 1000 units in elapsed time
        if (hours >= 24) {
            hours -= 24;
        }
        hours = Math.clamp(hours, 0, 24);
        return hours;
    }

    /**
     * Get whether it is currently daytime.
     * Daytime is defined as 6:00AM to 6:00PM.
     *
     * @return true if it is daytime, false otherwise.
     */
    public static boolean isDayTime() {
        int currentTime = (int) ((getElapsed() % TimeSpecification.DAY.getTime()) - 6000);
        if (currentTime < 0) {
            currentTime += (int) TimeSpecification.DAY.getTime(); // Adjust if before 6:00AM
        }
        return currentTime >= 0 && currentTime < 12000;
    }

    public static Long getMinecraftTime(long elapsed) {
        return elapsed + 18000;
    }

    /**
     * Get the current season of the year, based on the current month.
     *
     * @param month the current month in ordinal form.
     *
     * @return the season of the year.
     */
    public static String getMonthName(int month) {
        if (month < 1 || month > 12) {
            return "Unknown Month";
        }
        return SEASONS.get(month - 1);
    }

    /**
     * Get the current season of the year, based on the current month.
     *
     * @return the season of the year.
     *
     */
    public static String getMonthName() {
        return getMonthName(getMonth());
    }

    /**
     * Get the current season of the year, based on the current month.
     *
     * @param elapsed the current ticks that have elapsed.
     *
     * @return the season of the year.
     */
    public static String getMonthName(long elapsed) {
        return getMonthName((int) ((elapsed / TimeSpecification.MONTH.getTime()) % 12) + 1);
    }

    /**
     * Get the current day or night symbol.
     *
     * @return the day or night symbol.
     */
    public static Component getDayNightSymbol() {
        int currentTime = (int) ((getElapsed() % TimeSpecification.DAY.getTime()) - 6000);
        if (currentTime < 0) {
            currentTime += (int) TimeSpecification.DAY.getTime(); // Adjust if before 6:00AM
        }

        boolean isDaytime = currentTime >= 0 && currentTime < 12000;

        if (isDaytime) {
            return Component.text(DAY_SYMBOL).color(NamedTextColor.YELLOW);
        } else {
            return Component.text(NIGHT_SYMBOL).color(NamedTextColor.AQUA);
        }
    }

    /**
     * Get the current time of day in a displayable format.
     *
     * @return the current time of day.
     */
    public static Component getTimeComponent(long time) {
        int currentTime = (int) (time % TimeSpecification.DAY.getTime()) - 6000;
        if (currentTime < 0) {
            currentTime += (int) TimeSpecification.DAY.getTime();
        }

        int hours = 6 + (currentTime / (int) TimeSpecification.HOUR.getTime());
        if (hours >= 24) {
            hours -= 24;
        }

        // Corrected minutes calculation
        int minutes;
        minutes = (int) (currentTime % TimeSpecification.HOUR.getTime());
        minutes = minutes * 60 / (int) TimeSpecification.HOUR.getTime();
        String timePeriod = hours >= 12 ? "pm" : "am";
        hours = hours > 12 ? hours - 12 : (hours == 0 ? 12 : hours);

        String formattedMinutes = String.format("%02d", minutes);

        //"%d:%s%s"
        return Component.text(hours)
                .append(Component.text(":"))
                .append(Component.text(formattedMinutes))
                .append(Component.text(timePeriod));
    }

    private static void tick() {
        updateElapsed(10L);
    }

    /**
     * Check for events that occur at the given time.
     *
     * @see EventManager#checkAndTriggerEvents(long)
     * @param time the total ticks elapsed.
     */
    public static void checkForEvents(long time) {
        EventManager eventManager = EventManager.getInstance();
        eventManager.checkAndTriggerEvents(time);
    }
}
