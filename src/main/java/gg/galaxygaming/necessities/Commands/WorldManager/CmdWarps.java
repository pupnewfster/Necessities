package gg.galaxygaming.necessities.Commands.WorldManager;

import gg.galaxygaming.necessities.Necessities;
import gg.galaxygaming.necessities.Variables;
import gg.galaxygaming.necessities.WorldManager.WarpManager;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class CmdWarps implements WorldCmd {
    public boolean commandUse(CommandSender sender, String[] args) {
        Variables var = Necessities.getVar();
        WarpManager warps = Necessities.getWarps();
        sender.sendMessage((warps.getWarps().equals("") ? var.getEr() + "Error: " + var.getErMsg() + "There are no warps set." : var.getMessages() + "Available warps: " + ChatColor.WHITE + warps.getWarps()));
        return true;
    }
}