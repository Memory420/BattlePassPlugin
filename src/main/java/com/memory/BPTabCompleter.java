package com.memory;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class BPTabCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> suggestions = new ArrayList<>();

        if (command.getName().equalsIgnoreCase("bp")) {
            if (args.length == 1) {
                // Первый аргумент команды
                suggestions.add("menu");
                suggestions.add("help");
                suggestions.add("info");
                suggestions.add("rewards");
                suggestions.add("reload");
                suggestions.add("admin");
            } else if (args.length == 2 && args[0].equalsIgnoreCase("admin")) {
                suggestions.add("save");
                suggestions.add("load");
                suggestions.add("addxp");
            } else if (args.length == 3 && args[0].equalsIgnoreCase("admin") && args[1].equalsIgnoreCase("addxp")) {
                for (Player player : sender.getServer().getOnlinePlayers()) {
                    suggestions.add(player.getName());
                }
            }
        }

        return suggestions;
    }

}
