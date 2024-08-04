package club.tesseract.mycalendar.display;

import club.tesseract.mycalendar.MyCalendar;
import club.tesseract.mycalendar.calendar.MinecraftCalendar;
import club.tesseract.mycalendar.calendar.time.TimeChangeListener;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;

/**
 * Synchronizes the world time across all worlds.
 */
public class WorldTimeSync implements TimeChangeListener, Listener {


    @EventHandler
    private void onWorldsLoaded(WorldLoadEvent event) {
        syncWorldTime(0, event.getWorld());
    }

    /**
     * Synchronizes the world time across all worlds.
     */
    public static void syncWorldTime(long elapsed) {
        Bukkit.getWorlds().forEach(world -> syncWorldTime(elapsed, world));
    }

    public static void syncWorldTime(long elapsed, World world) {
        world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
        world.setFullTime(MinecraftCalendar.getMinecraftTime(elapsed));
    }

    @Override
    public void onTimeChange(long priorElapsed, long elapsed, boolean forced) {
        Bukkit.getScheduler().runTask(MyCalendar.getPlugin(), () -> syncWorldTime(elapsed));
    }
}
