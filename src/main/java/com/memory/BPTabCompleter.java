package com.memory;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class BPTabCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> suggestions = new ArrayList<>();

        if (command.getName().equalsIgnoreCase("bp")) {
            if (args.length == 1) {
                suggestions.add("menu");
                suggestions.add("help");
                suggestions.add("info");
                suggestions.add("rewards");
            }
        }
        return suggestions;
    }
}
