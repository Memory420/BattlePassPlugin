package com.memory;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.UUID;

public class BattlePassPlugin extends JavaPlugin implements Listener {
    public BattlePassManager getBattlePassManager() {
        return battlePassManager;
    }

    private BattlePassManager battlePassManager;
    private BattlePassScoreboard battlePassScoreboard;

    @Override
    public void onEnable() {
        broadcastMessage(ChatColor.DARK_AQUA + "💡 Current version: " + getDescription().getVersion());
        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("BattlePass plugin is running...");

        File dataFolder = new File(getDataFolder(), "player_data");
        battlePassManager = new BattlePassManager(BattlePassConfigLoader.loadRewards("BattlePassRewards.yml"), dataFolder, this);
        battlePassManager.loadAllProgress();
        battlePassScoreboard = new BattlePassScoreboard(battlePassManager);

        getCommand("bp").setExecutor(new BPCommand(this, battlePassManager));
        getCommand("bp").setTabCompleter(new BPTabCompleter());
        getLogger().info("BattlePass configuration loaded!");
    }

    @Override
    public void onDisable() {
        battlePassManager.saveAllProgress();
        getLogger().info("BattlePassPlugin disabled!");
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();

        battlePassManager.loadProgress(uuid);

        String version = getDescription().getVersion();
        event.getPlayer().sendMessage(ChatColor.DARK_AQUA + "💡 Current version: " + version);

        battlePassScoreboard.show(event.getPlayer());

        PlayerProgress progress = battlePassManager.getProgress(uuid);
        if (progress != null) {
            int level = progress.getLevel();
            int xp = progress.getXp();
            event.getPlayer().sendMessage(ChatColor.GREEN + "Ваш текущий уровень Battle Pass: " + ChatColor.GOLD + level);
            event.getPlayer().sendMessage(ChatColor.GREEN + "Ваш опыт: " + ChatColor.GOLD + xp);
        } else {
            event.getPlayer().sendMessage(ChatColor.RED + "Прогресс Battle Pass не найден.");
        }
    }


    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        battlePassManager.saveProgress(uuid);
    }
    public void broadcastMessage(String message) {
        for (Player player : getServer().getOnlinePlayers()) {
            player.sendMessage(message);
        }
    }
}
