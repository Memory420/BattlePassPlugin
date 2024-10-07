package com.memory;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BPCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            player.sendMessage(ChatColor.GREEN + "Opening Battle Pass menu...");
            openMenu(player);
        } else {
            sender.sendMessage(ChatColor.RED + "This command can only be used by a player.");
        }
        return true;
    }

    private void openMenu(Player player) {

    }
}
