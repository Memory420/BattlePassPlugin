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

    public BattlePassConfigLoader(BattlePassManager battlePassManager) {
        this.battlePassManager = battlePassManager;
    }

    public static List<Reward> loadRewards(String config) {
        List<Reward> rewardsList = new ArrayList<>();
        Yaml yaml = new Yaml();

        try (InputStream inputStream = BattlePassConfigLoader.class.getClassLoader().getResourceAsStream(config)) {
            if (inputStream == null) {
                System.out.println("Файл не найден!");
                return new ArrayList<>();
            }

            Map<String, Object> data = yaml.load(inputStream);
            Map<String, Object> rewardsData = (Map<String, Object>) data.get("rewards");
            Map<Object, Object> levels = (Map<Object, Object>) rewardsData.get("levels");

            for (Map.Entry<Object, Object> entry : levels.entrySet()) {
                int level = (int) entry.getKey();
                Map<String, Object> levelData = (Map<String, Object>) entry.getValue();

                int xp = (int) levelData.get("xp");
                List<Map<String, Object>> rewards = (List<Map<String, Object>>) levelData.get("reward");

                for (Map<String, Object> rewardData : rewards) {
                    String materialName = (String) rewardData.get("material");
                    int amount = (int) rewardData.get("amount");

                    Material material = Material.getMaterial(materialName);
                    if (material != null) {
                        ItemStack rewardItem = new ItemStack(material, amount);
                        Reward reward = new Reward(level, xp, rewardItem, amount);
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
