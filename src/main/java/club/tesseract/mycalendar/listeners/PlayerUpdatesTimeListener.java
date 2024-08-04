package club.tesseract.mycalendar.listeners;

import club.tesseract.mycalendar.MyCalendar;
import club.tesseract.mycalendar.calendar.MinecraftCalendar;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.TimeSkipEvent;

/**
 * Listen for when a player or console use command to force update the worlds time.
 */
public class PlayerUpdatesTimeListener implements Listener {

    @EventHandler
    private void onPlayerUpdatesTime(TimeSkipEvent event) {
        if (event.getSkipReason() == TimeSkipEvent.SkipReason.CUSTOM) {
            return;
        }
        long skipAmount = event.getSkipAmount();
        Bukkit.getScheduler().runTask(MyCalendar.getPlugin(),
                () -> MinecraftCalendar.updateElapsed(skipAmount));
    }

}
