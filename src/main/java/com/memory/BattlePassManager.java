package com.memory;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class BattlePassManager {

    private final Map<UUID, PlayerProgress> playerProgressMap = new HashMap<>();
    private final File dataFolder;
    private final BattlePassPlugin battlePassPlugin;
    public List<Reward> getRewardList() {
        return rewardList;
    }

    public void setRewardList(List<Reward> rewardList) {
        this.rewardList = rewardList;
    }

    private List<Reward> rewardList;

    public BattlePassManager(List<Reward> rewardList, File dataFolder, BattlePassPlugin bpp) {
        this.rewardList = rewardList;
        this.dataFolder = dataFolder;
        this.battlePassPlugin = bpp;
        if (!dataFolder.exists()){
            dataFolder.mkdirs();
        }
    }
    public PlayerProgress getPlayerProgressByName(String playerName) {
        Player player = Bukkit.getPlayer(playerName);
        if (player == null) {
            return null;
        }

        UUID uuid = player.getUniqueId(); // Получаем UUID игрока
        return getProgress(uuid); // Возвращаем прогресс игрока
    }
    public String listRewards() {
        StringBuilder rewardsInfo = new StringBuilder("Battle Pass Rewards: \n");
        for (Reward reward : rewardList) {
            rewardsInfo.append(reward.toString()).append("\n");
        }
        return rewardsInfo.toString();
    }

    public void loadProgress(UUID uuid){
        File file = new File(dataFolder, uuid.toString() + ".yml");
        if (file.exists()){
            FileConfiguration config = YamlConfiguration.loadConfiguration(file);
            int level = config.getInt("level", 1);
            int xp = config.getInt("xp", 0);
            playerProgressMap.put(uuid, new PlayerProgress(level, xp));
        } else {
            playerProgressMap.put(uuid, new PlayerProgress(1, 0));
        }
    }
    public void loadAllProgress() {
        File[] files = dataFolder.listFiles((dir, name) -> name.endsWith(".yml"));
        if (files != null) {
            for (File file : files) {
                String uuidString = file.getName().replace(".yml", "");
                try {
                    UUID uuid = UUID.fromString(uuidString);
                    loadProgress(uuid);
                } catch (IllegalArgumentException e) {
                    battlePassPlugin.getLogger().warning("Некорректный UUID в файле: " + file.getName());
                }
            }
        }
    }
    public void saveProgress(UUID uuid){
        PlayerProgress progress = playerProgressMap.get(uuid);
        if (progress != null){
            File file = new File(dataFolder, uuid.toString() + ".yml");
            FileConfiguration config = YamlConfiguration.loadConfiguration(file);
            config.set("level", progress.getLevel());
            config.set("xp", progress.getXp());
            try {
                config.save(file);
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }
    public PlayerProgress getProgress(UUID uuid){
        return playerProgressMap.get(uuid);
    }
    public void addXp(Player player, int xp){
        UUID uuid = player.getUniqueId();
        PlayerProgress progress = getProgress(uuid);
        if (progress != null) {
            progress.setXp(progress.getXp() + xp);
            int levelUpXp = 100;
            while (progress.getXp() >= levelUpXp){
                progress.setXp(progress.getXp() - levelUpXp);
                progress.setLevel(progress.getLevel() + 1);
            }
            saveProgress(uuid);
        }
    }
    public void saveAllProgress(){
        for (UUID uuid : playerProgressMap.keySet()){
            saveProgress(uuid);
        }
    }
}
