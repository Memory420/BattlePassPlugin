package com.memory;

import java.util.List;

public class BattlePassManager {
    public List<Reward> rewardList;

    public BattlePassManager(List<Reward> rewardList) {
        this.rewardList = rewardList;
    }
    public void listRewards(){
        for (Reward reward : rewardList){
            reward.toString();
        }
    }
}
