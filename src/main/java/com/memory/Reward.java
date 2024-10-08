package com.memory;

import org.bukkit.inventory.ItemStack;

public class Reward {
    int level;
    int xp;
    ItemStack rewardItem;
    int rewardAmount;

    public Reward(int level, int xp, ItemStack rewardItem, int rewardAmount) {
        this.level = level;
        this.xp = xp;
        this.rewardItem = rewardItem;
        this.rewardAmount = rewardAmount;
    }


    @Override
    public String toString() {
        return "Lvl " + level + " {" +
                "xp=" + xp +
                ", rewardItem = " + rewardItem +
                ", rewardAmount = " + rewardAmount +
                '}';
    }
}
