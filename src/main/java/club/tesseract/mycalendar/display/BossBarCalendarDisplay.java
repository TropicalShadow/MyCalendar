package club.tesseract.mycalendar.display;

import club.tesseract.mycalendar.MyCalendar;
import club.tesseract.mycalendar.calendar.MinecraftCalendar;
import club.tesseract.mycalendar.calendar.time.TimeChangeListener;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Represents the display of the calendar in the boss bar.
 */
public class BossBarCalendarDisplay implements Listener, TimeChangeListener {

    private final BossBar bossBar;

    /**
     * Create a new boss bar calendar display.
     */
    public BossBarCalendarDisplay() {
        this.bossBar = BossBar.bossBar(
                Component.text("Date Time not found"),
                1.0f,
                BossBar.Color.BLUE,
                BossBar.Overlay.PROGRESS
        );
    }

    @EventHandler
    private void onPlayerJoin(PlayerJoinEvent event) {
        this.bossBar.addViewer(event.getPlayer());
    }

    @EventHandler
    private void onPlayerQuit(PlayerQuitEvent event) {
        this.bossBar.removeViewer(event.getPlayer());
    }

    /**
     * Called when the time changes.
     *
     * @param priorElapsed the elapsed time before the change.
     * @param elapsed the elapsed time after the change.
     * @param forced true if the time change was forced, false otherwise.
     */
    @Override
    public void onTimeChange(long priorElapsed, long elapsed, boolean forced) {
        int magicBitMask = 127;
        int minutes = MinecraftCalendar.getMinutes(elapsed);
        int valueBitmask = (minutes % 10 == 0 && minutes / 10 < 7) ? (1 << (minutes / 10)) : 0;

        if ((valueBitmask & magicBitMask) == 0 && !forced) {
            return;
        }

        boolean isDayTime = MinecraftCalendar.isDayTime();

        Component dateTimeString = MinecraftCalendar.getTimeComponent(elapsed);
        Component dayNightSymbol = MinecraftCalendar.getDayNightSymbol();
        final Component updatedTitle = Component.text(MinecraftCalendar.getMonthName(elapsed))
                .append(Component.space())
                .append(dateTimeString)
                .append(Component.space())
                .append(dayNightSymbol);
        MyCalendar.getPlugin().getComponentLogger().info(updatedTitle);

        int hour = MinecraftCalendar.getHour();

        bossBar.name(updatedTitle);
        bossBar.color(isDayTime ? BossBar.Color.BLUE : BossBar.Color.PURPLE);
        hour += 6;
        bossBar.progress((hour % 12.0f) / 12.0f);
    }
}
