package com.memory;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BattlePassConfigLoader {
    private BattlePassManager battlePassManager;

    public static void main(String[] args) {
        BattlePassManager battlePassManager1 = new BattlePassManager(loadRewards("BattlePassRewards.yml"));
        for (Reward reward : battlePassManager1.rewardList){
            reward.toString();
        }
    }
    public BattlePassConfigLoader(BattlePassManager battlePassManager) {
        this.battlePassManager = battlePassManager;
    }
    public static List<Reward> loadRewards(String config){
        List<Reward> rewardsList = new ArrayList<Reward>();
        Yaml yaml = new Yaml();
        try (InputStream inputStream = BattlePassConfigLoader.class.getClassLoader().getResourceAsStream(config)){
            if (inputStream == null){
                System.out.println("Файл не найден!");
                return new ArrayList<>();
            }
            Map<String, Object> data = yaml.load(inputStream);
            Map<String, Object> rewardsData = (Map<String, Object>) data.get("rewards");
            Map<String, Object> levels = (Map<String, Object>) rewardsData.get("levels");

            for (String levelKey : levels.keySet()){
                Map<String, Object> levelData = (Map<String, Object>) levels.get(levelKey);
                int xp = (int) levelData.get("xp");
                List<Map<String, Object>> rewards = (List<Map<String, Object>>) levelData.get("reward");

                for (Map<String, Object> rewardData : rewards) {
                    String materialName = (String) rewardData.get("material");
                    int amount = (int) rewardData.get("amount");

                    Material material = Material.getMaterial(materialName);
                    if (material != null){
                        ItemStack rewardItem = new ItemStack(material, amount);
                        Reward reward = new Reward(xp, rewardItem, amount);
                        rewardsList.add(reward);
                    } else {
                        System.out.println("Неизвестный материал: " + materialName);
                    }
                }
            }
            System.out.println("Конфигурация загружена!");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return rewardsList;
    }
}
