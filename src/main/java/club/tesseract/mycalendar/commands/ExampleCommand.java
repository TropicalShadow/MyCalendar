package club.tesseract.mycalendar.commands;

import club.tesseract.mycalendar.MyCalendar;
import club.tesseract.mycalendar.calendar.MinecraftCalendar;
import club.tesseract.mycalendar.calendar.events.CalendarEvent;
import club.tesseract.mycalendar.calendar.events.EventManager;
import club.tesseract.mycalendar.calendar.events.ScheduledEvent;
import co.aikar.commands.BaseCommand;
import co.aikar.commands.BukkitCommandIssuer;
import co.aikar.commands.CommandIssuer;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Dependency;
import co.aikar.commands.annotation.Subcommand;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;

/**
 * An example command.
 */
@CommandAlias("example")
public class ExampleCommand extends BaseCommand {

    @Dependency
    private MyCalendar plugin;


    @Subcommand("default")
    @Default
    private void onDefault(CommandIssuer sender) {
        if (!(sender instanceof BukkitCommandIssuer)) {
            return;
        }
        Audience audience = ((BukkitCommandIssuer) sender).getIssuer();

        CalendarEvent event = EventManager.getInstance().nextEvent(MinecraftCalendar.getElapsed());
        Component debugMessage = Component.text("Hello, Minecraft!")
                .append(Component.newline())
                .append(Component.text("Next event is: " + event.getId()))
                .append(Component.newline())
                .append(Component.text("Elapsed time: " + MinecraftCalendar.getElapsed()))
                .append(Component.newline())
                .append(Component.text("Current time: "
                        + MinecraftCalendar.getMinecraftTime(MinecraftCalendar.getElapsed())))
                .append(Component.newline())
                .append(Component.text("Current day: " + MinecraftCalendar.getDay()))
                .append(Component.newline())
                .append(Component.text("Current season: " + MinecraftCalendar.getMonth()));

        audience.sendMessage(debugMessage);
    }

    @Subcommand("upcoming")
    private void onUpcoming(CommandIssuer sender) {
        if (!(sender instanceof BukkitCommandIssuer)) {
            return;
        }
        Audience audience = ((BukkitCommandIssuer) sender).getIssuer();
        Component debugMessage = Component.text("Hello, Minecraft!")
                .append(Component.newline())
                .append(Component.text("Upcoming events:"))
                .append(Component.newline());

        for (ScheduledEvent scheduledEvent : EventManager.getInstance().getScheduledEvents()) {
            Component eventName = Component.text(scheduledEvent.event().getId())
                    .hoverEvent(Component.text(scheduledEvent.id().toString()));

            debugMessage = debugMessage.append(eventName)
                    .append(Component.space())
                    .append(Component.text("at"))
                    .append(Component.space())
                    .append(MinecraftCalendar.getTimeComponent(scheduledEvent.time()))
                    .append(Component.newline());
        }

        audience.sendMessage(debugMessage);
    }

}
