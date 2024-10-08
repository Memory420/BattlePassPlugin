package com.memory;

import org.bukkit.inventory.ItemStack;

public class Reward {
    int xp;
    ItemStack rewardItem;
    int rewardAmount;

    public Reward(int xp, ItemStack rewardItem, int rewardAmount) {
        this.xp = xp;
        this.rewardItem = rewardItem;
        this.rewardAmount = rewardAmount;
    }

    @Override
    public String toString() {
        return "Reward {" +
                "xp=" + xp +
                ", rewardItem = " + rewardItem +
                ", rewardAmount = " + rewardAmount +
                '}';
    }
}
