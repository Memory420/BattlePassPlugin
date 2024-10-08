package com.memory;

import org.bukkit.ChatColor;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class BattlePassPlugin extends JavaPlugin implements Listener {
    public BattlePassManager getBattlePassManager() {
        return battlePassManager;
    }

    private BattlePassManager battlePassManager;

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("BattlePass plugin is running...");
        battlePassManager = new BattlePassManager(BattlePassConfigLoader.loadRewards("BattlePassRewards.yml"));
        for (Reward reward : battlePassManager.rewardList){
            getLogger().info(reward.toString());
        }
        getLogger().info("BP configuration loaded!");
        getCommand("bp").setExecutor(new BPCommand(this));
        getCommand("bp").setTabCompleter(new BPTabCompleter());

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
