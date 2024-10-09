package com.memory;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

public class BattlePassScoreboard {
    private final BattlePassManager battlePassManager;
    private final Scoreboard scoreboard;

    public BattlePassScoreboard(BattlePassManager battlePassManager) {
        this.battlePassManager = battlePassManager;
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        scoreboard = manager.getNewScoreboard();

        Objective objective = scoreboard.registerNewObjective("battlepass", "dummy", ChatColor.GOLD + "Battle Pass");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        Score level = objective.getScore(ChatColor.GREEN + "Уровень: ");
        level.setScore(1);

        Score xp = objective.getScore(ChatColor.AQUA + "Опыт: ");
        xp.setScore(0);

        Score nextLevel = objective.getScore(ChatColor.YELLOW + "Следующий уровень: ");
        nextLevel.setScore(300); // Пример для следующего уровня
    }

    public void show(Player player) {
        player.setScoreboard(scoreboard);
    }

    public void update(Player player, int newLevel, int newXp, int xpForNextLevel) {
        Objective objective = scoreboard.getObjective("battlepass");
        if (objective != null) {
            scoreboard.resetScores(ChatColor.GREEN + "Уровень: ");
            scoreboard.resetScores(ChatColor.AQUA + "Опыт: ");
            scoreboard.resetScores(ChatColor.YELLOW + "Следующий уровень: ");

            objective.getScore(ChatColor.GREEN + "Уровень: ").setScore(newLevel);
            objective.getScore(ChatColor.AQUA + "Опыт: ").setScore(newXp);
            objective.getScore(ChatColor.YELLOW + "Следующий уровень: ").setScore(xpForNextLevel);

            player.setScoreboard(scoreboard);
        }
    }
}
