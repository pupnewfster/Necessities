package com.crossge.necessities.Commands;

import com.crossge.necessities.Necessities;
import com.crossge.necessities.Variables;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.List;

public class CmdDevs implements Cmd {
    public boolean commandUse(CommandSender sender, String[] args) {
        Variables var = Necessities.getInstance().getVar();
        String d = var.getMessages() + "The Devs for Necessities are: ";
        List<String> devs = Necessities.getInstance().getDevs();
        for (int i = 0; i < devs.size(); i++)
            d += (i + 1 >= devs.size() ? "and " + ChatColor.WHITE + ChatColor.ITALIC + devs.get(i) + var.getMessages() + "." : "" + ChatColor.WHITE + ChatColor.ITALIC + devs.get(i) + var.getMessages() + ", ");
        sender.sendMessage(d);
        return true;
    }

    public boolean isPaintballEnabled() {
        return true;
    }
}