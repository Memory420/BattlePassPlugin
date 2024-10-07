package com.memory;

import org.bukkit.ChatColor;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class BattlePassPlugin extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        getCommand("bp").setExecutor(new BPCommand());
        getLogger().info("BattlePassPlugin enabled!");
    }


    @Override
    public void onDisable() {
        getLogger().info("BattlePassPlugin disabled!");
    }
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        String version = getDescription().getVersion();
        e.getPlayer().sendMessage(ChatColor.DARK_AQUA + "💡 Current version: " + version);
    }

}
