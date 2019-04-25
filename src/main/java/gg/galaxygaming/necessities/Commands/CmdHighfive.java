package gg.galaxygaming.necessities.Commands;

import gg.galaxygaming.necessities.Necessities;
import gg.galaxygaming.necessities.Utils;
import gg.galaxygaming.necessities.Variables;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdHighfive implements Cmd {

    public boolean commandUse(CommandSender sender, String[] args) {
        Variables var = Necessities.getVar();
        if (args.length == 0) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must enter a player to highfive.");
            return true;
        }
        if (sender instanceof Player && args[0].equalsIgnoreCase("Console")) {
            Player player = (Player) sender;
            Bukkit.getConsoleSender()
                  .sendMessage(var.getObj() + player.getName() + var.getMessages() + " just highfived you.");
            Bukkit.broadcastMessage(
                  var.getMessages() + player.getName() + " just highfived " + Necessities.getConsole().getName()
                        .replaceAll(":", ""));
            return true;
        }
        UUID uuid = Utils.getID(args[0]);
        if (uuid == null) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must enter a valid player.");
            return true;
        }
        Player target = Bukkit.getPlayer(uuid);
        String name =
              sender instanceof Player ? sender.getName() : Necessities.getConsole().getName().replaceAll(":", "");
        target.sendMessage(var.getObj() + name + var.getMessages() + " just highfived you.");
        Bukkit.broadcastMessage(var.getMessages() + name + " just highfived " + target.getName());
        return true;
    }

    public boolean isPaintballEnabled() {
        return true;
    }
}