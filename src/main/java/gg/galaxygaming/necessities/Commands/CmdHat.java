package gg.galaxygaming.necessities.Commands;

import gg.galaxygaming.necessities.Hats.Hat;
import gg.galaxygaming.necessities.Hats.HatType;
import gg.galaxygaming.necessities.Necessities;
import gg.galaxygaming.necessities.RankManager.User;
import gg.galaxygaming.necessities.Variables;
import java.util.Collections;
import java.util.List;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdHat implements Cmd {

    public boolean commandUse(CommandSender sender, String[] args) {
        Variables var = Necessities.getVar();
        if (sender instanceof Player) {
            Player p = (Player) sender;
            User u = Necessities.getUM().getUser(p.getUniqueId());
            if (args.length == 0) {
                if (u.getHat() == null) {
                    p.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must enter a valid hat type.");
                    p.sendMessage(var.getMessages() + validTypes());
                } else {
                    u.setHat(null);
                    p.sendMessage(var.getMessages() + "You are no longer wearing a hat.");
                }
                return true;
            }
            HatType type = HatType.fromString(args[0]);
            if (type == null) {
                p.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must enter a valid hat type.");
                p.sendMessage(var.getMessages() + validTypes());
                return true;
            } else if (type.equals(HatType.Design) && !Necessities.getInstance().isDev(p.getUniqueId())) {
                p.sendMessage(var.getEr() + "Error: " + var.getErMsg()
                      + "That is a hat for designing. You do not have access to it please choose another hat.");
                p.sendMessage(var.getMessages() + validTypes());
                return true;
            }
            Hat h = Hat.fromType(type, p.getLocation());
            if (h == null) {
                p.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must enter a valid hat type.");
                p.sendMessage(var.getMessages() + validTypes());
                return true;
            }
            u.setHat(h);
            p.sendMessage(var.getMessages() + "You are now wearing a " + var.getObj() + type.getName().toLowerCase()
                  .replaceAll("_", " ") + var.getMessages() + '.');
        } else {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must be a player to wear a hat.");
        }
        return true;
    }

    private String validTypes() {
        StringBuilder typesBuilder = new StringBuilder("Valid hat types: ");
        for (String h : HatType.getTypes()) {
            typesBuilder.append(h.toLowerCase()).append(", ");
        }
        String types = typesBuilder.toString();
        return types.substring(0, types.length() - 2) + '.';
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        //TODO: TabComplete
        return Collections.emptyList();
    }
}