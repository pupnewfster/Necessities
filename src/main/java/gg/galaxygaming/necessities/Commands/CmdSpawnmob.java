package gg.galaxygaming.necessities.Commands;

import gg.galaxygaming.necessities.Necessities;
import gg.galaxygaming.necessities.RankManager.User;
import gg.galaxygaming.necessities.Utils;
import gg.galaxygaming.necessities.Variables;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class CmdSpawnmob implements Cmd {
    public boolean commandUse(CommandSender sender, String[] args) {
        Variables var = Necessities.getVar();
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (args.length < 2) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Format requires you enter a mob species to spawn and an amount to spawn.");
                return true;
            }
            if (!Utils.legalInt(args[1])) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Invalid amount.");
                return true;
            }
            int amount = Integer.parseInt(args[1]);
            if (amount > 50)
                amount = 50;//TODO: at some point make configurable possibly
            User u = Necessities.getUM().getUser(p.getUniqueId());
            Location l = u.getLookingAt();
            if (l == null) {
                p.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Block out of range.");
                return true;
            }
            if (l.getBlock().getType().isSolid())
                l.setY(l.getY() + 1);
            EntityType type = getType(args[0]);
            if (type == null) {
                p.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Invalid mob type.");
                p.sendMessage(var.getMessages() + "Valid mob types: " + ChatColor.WHITE + validTypes());
                return true;
            }
            for (int i = 0; i < amount; i++)
                l.getWorld().spawnEntity(l, type);
            p.sendMessage(var.getMessages() + "Spawned " + var.getObj() + amount + var.getMessages() + " of " + var.getObj() + type.toString().replaceAll("_", " ").toLowerCase() + var.getMessages() + ".");
        } else
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You cannot spawn mobs because you are... nice?");
        return true;
    }

    private EntityType getType(String message) {
        message = message.replaceAll("_", "");
        for (EntityType type : EntityType.values())
            if (type.isSpawnable()) {
                String name = type.toString();
                if (message.equalsIgnoreCase(name.replaceAll("_", "")))
                    return type;
            }
        return null;
    }

    private String validTypes() {
        StringBuilder typesBuilder = new StringBuilder();
        for (EntityType type : EntityType.values())
            if (type.isSpawnable())
                typesBuilder.append(type.toString().replaceAll("_", " ").toLowerCase()).append(", ");
        String types = typesBuilder.toString();
        return types.trim().substring(0, types.length() - 2);
    }
}