package gg.galaxygaming.necessities.Commands.WorldManager;

import gg.galaxygaming.necessities.Necessities;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

public class CmdWorlds implements WorldCmd {

    public boolean commandUse(CommandSender sender, String[] args) {
        List<String> worlds = Bukkit.getWorlds().stream().map(World::getName)
              .collect(Collectors.toCollection(ArrayList::new));
        StringBuilder levelsBuilder = new StringBuilder();
        for (int i = 0; i < worlds.size() - 1; i++) {
            levelsBuilder.append(worlds.get(i)).append(", ");
        }
        if (worlds.size() > 1) {
            levelsBuilder.append("and ");
        }
        levelsBuilder.append(worlds.get(worlds.size() - 1)).append('.');
        sender.sendMessage(
              Necessities.getVar().getMessages() + "The worlds are: " + ChatColor.WHITE + levelsBuilder.toString());
        return true;
    }
}