package gg.galaxygaming.necessities.Commands;

import gg.galaxygaming.necessities.Necessities;
import gg.galaxygaming.necessities.Variables;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdSuicide implements Cmd {

    public boolean commandUse(CommandSender sender, String[] args) {
        Variables var = Necessities.getVar();
        if (sender instanceof Player) {
            Player player = (Player) sender;
            player.setHealth(0);
            player.sendMessage(var.getMessages() + "Goodbye cruel world.");
            Bukkit.broadcastMessage(
                  var.getObj() + player.getDisplayName() + var.getMessages() + " took their own life.");
        } else {
            sender.sendMessage(
                  var.getEr() + "Error: " + var.getErMsg() + "You cannot suicide you would kill the server.");
        }
        return true;
    }

    public boolean isPaintballEnabled() {
        return true;
    }
}