package gg.galaxygaming.necessities.Commands;

import gg.galaxygaming.necessities.Necessities;
import gg.galaxygaming.necessities.RankManager.User;
import gg.galaxygaming.necessities.Variables;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class CmdSpawner implements Cmd {

    public boolean commandUse(CommandSender sender, String[] args) {
        Variables var = Necessities.getVar();
        if (sender instanceof Player) {
            Player p = (Player) sender;
            User u = Necessities.getUM().getUser(p.getUniqueId());
            Location l = u.getLookingAt();
            if (l == null) {
                p.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Block out of range.");
                return true;
            }
            if (l.getBlock().getState() instanceof CreatureSpawner) {
                CreatureSpawner spawner = (CreatureSpawner) l.getBlock().getState();
                if (args.length == 0) {
                    p.sendMessage(var.getMessages() + "Spawner type is " + spawner.getSpawnedType().toString()
                          .replaceAll("_", " ").toLowerCase() + '.');
                    return true;
                }
                EntityType spawnerType = getType(args[0]);
                if (spawnerType == null) {
                    p.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Invalid mob type.");
                    p.sendMessage(var.getMessages() + "Valid mob types: " + ChatColor.WHITE + validTypes());
                    return true;
                }
                spawner.setSpawnedType(spawnerType);
                spawner.update();
                p.sendMessage(
                      var.getMessages() + "Spawner type set to " + var.getObj() + spawner.getSpawnedType().toString()
                            .replaceAll("_", " ").toLowerCase() + var.getMessages() + '.');
                return true;
            }
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "That block is not a spawner.");
            return true;
        } else {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg()
                  + "You cannot change the type of spawner because you cannot look at the spawner.");
        }
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        if (args.length != 1 || !(sender instanceof Player)) {
            return Collections.emptyList();
        }
        List<String> complete = new ArrayList<>();
        String search = args[0];
        for (EntityType type : EntityType.values()) {
            if (type.isSpawnable()) {
                String name = type.toString().toLowerCase().replaceAll("_", "");
                if (name.startsWith(search)) {
                    complete.add(name);
                }
            }
        }
        return complete;
    }

    private EntityType getType(String message) {
        message = message.replaceAll("_", "");
        for (EntityType type : EntityType.values()) {
            if (type.isSpawnable()) {
                String name = type.toString();
                if (message.equalsIgnoreCase(name.replaceAll("_", ""))) {
                    return type;
                }
            }
        }
        return null;
    }

    private String validTypes() {
        StringBuilder typesBuilder = new StringBuilder();
        for (EntityType type : EntityType.values()) {
            if (type.isSpawnable()) {
                typesBuilder.append(type.toString().replaceAll("_", " ").toLowerCase()).append(", ");
            }
        }
        String types = typesBuilder.toString();
        return types.trim().substring(0, types.length() - 2);
    }
}