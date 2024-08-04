package club.tesseract.mycalendar;

import club.tesseract.mycalendar.calendar.MinecraftCalendar;
import club.tesseract.mycalendar.calendar.time.TimeNotifier;
import club.tesseract.mycalendar.commands.ExampleCommand;
import club.tesseract.mycalendar.display.BossBarCalendarDisplay;
import club.tesseract.mycalendar.display.WorldTimeSync;
import club.tesseract.mycalendar.listeners.PlayerUpdatesTimeListener;
import co.aikar.commands.PaperCommandManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * The main class of the plugin.
 */
public final class MyCalendar extends JavaPlugin {

    private PaperCommandManager commandManager;

    @Override
    public void onEnable() {
        // Plugin startup logic
        BossBarCalendarDisplay bossBarCalendarDisplay = new BossBarCalendarDisplay();
        WorldTimeSync worldTimeSync = new WorldTimeSync();
        PlayerUpdatesTimeListener playerUpdatesTimeListener = new PlayerUpdatesTimeListener();
        Bukkit.getServer().getPluginManager().registerEvents(bossBarCalendarDisplay, this);
        Bukkit.getServer().getPluginManager().registerEvents(worldTimeSync, this);
        Bukkit.getServer().getPluginManager().registerEvents(playerUpdatesTimeListener, this);
        TimeNotifier.addTimeChangeListener(bossBarCalendarDisplay);
        TimeNotifier.addTimeChangeListener(worldTimeSync);

        commandManager = new PaperCommandManager(this);
        commandManager.registerCommand(new ExampleCommand());

        commandManager.setDefaultExceptionHandler((command, registeredCommand, sender, args, t) -> {
            sender.sendMessage("An error occurred while executing the command.");
            getLogger().warning("Error occured while executing command " + command.getName());
            getLogger().severe(t.getMessage());
            return true; // mark as handled to prevent further handlers from being called.
        });

        MinecraftCalendar.setupElapsed();

        this.getLogger().info("Plugin enabled!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

        this.getLogger().info("Plugin disabled!");
    }

    public static MyCalendar getPlugin() {
        return MyCalendar.getPlugin(MyCalendar.class);
    }
}
