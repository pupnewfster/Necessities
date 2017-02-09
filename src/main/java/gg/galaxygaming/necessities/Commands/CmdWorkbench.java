package gg.galaxygaming.necessities.Commands;

import gg.galaxygaming.necessities.Necessities;
import gg.galaxygaming.necessities.Variables;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdWorkbench implements Cmd {
    public boolean commandUse(CommandSender sender, String[] args) {
        Variables var = Necessities.getInstance().getVar();
        if (sender instanceof Player) {
            ((Player) sender).openWorkbench(null, true);
            sender.sendMessage(var.getMessages() + "Workbench opened.");
        } else
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You can not open a workbench because you are not a player.");
        return true;
    }
}