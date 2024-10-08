package com.memory;

import java.util.List;

public class BattlePassManager {
    public List<Reward> getRewardList() {
        return rewardList;
    }

    public void setRewardList(List<Reward> rewardList) {
        this.rewardList = rewardList;
    }

    private List<Reward> rewardList;

    public BattlePassManager(List<Reward> rewardList) {
        this.rewardList = rewardList;
    }
    public String listRewards() {
        StringBuilder rewardsInfo = new StringBuilder("Battle Pass Rewards: \n");
        for (Reward reward : rewardList) {
            rewardsInfo.append(reward.toString()).append("\n");
        }
        return rewardsInfo.toString();
    }

}
