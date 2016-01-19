/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.panryba.mc.filter;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author PanRyba.pl
 */
public class Plugin extends JavaPlugin implements Listener {
    
    String allowed;
    String disallowedMsg;

    int limit;
    String limitMsg;
    
    @Override
    public void onEnable() {
        super.onEnable();
        FileConfiguration config = getConfig();
        
        allowed = config.getString("name.allowed", "");
        disallowedMsg = config.getString("name.msg", "");

        limit = config.getInt("limit.max", 0);
        limitMsg = config.getString("limit.msg", "");

        getCommand("limit").setExecutor(new LimitCommand(this));
        getServer().getPluginManager().registerEvents(this, this);
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onAsyncLogin(AsyncPlayerPreLoginEvent event) {
        String name = event.getName();

        if(!name.matches(this.allowed)) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, this.disallowedMsg);
            Bukkit.getLogger().info("[FILTERED][NAME] " + name);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onLogin(PlayerLoginEvent event) {
        if(this.limit == 0) {
            return;
        }

        if(event.getPlayer().isWhitelisted()) {
            return;
        }

        int curr = Bukkit.getOnlinePlayers().length;

        if(curr >= this.limit) {
            event.disallow(PlayerLoginEvent.Result.KICK_OTHER, this.limitMsg);
            Bukkit.getLogger().info("[FILTERED][LIMIT] " + curr + "/" + this.limit);
        }
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}
