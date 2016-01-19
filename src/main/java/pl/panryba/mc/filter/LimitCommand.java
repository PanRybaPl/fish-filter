package pl.panryba.mc.filter;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * @author PanRyba.pl
 */
public class LimitCommand implements CommandExecutor {

    private final Plugin plugin;

    public LimitCommand(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(strings.length > 1) {
            return false;
        }

        switch(strings.length) {
            case 0:
                int limit = plugin.getLimit();
                commandSender.sendMessage("Current players limit: " + limit + " (0 means no limit)");
                break;

            case 1:
                int value = Integer.parseInt(strings[0]);
                plugin.setLimit(value);
                commandSender.sendMessage("Players limit set to: " + value);
                break;
        }

        return true;
    }
}
