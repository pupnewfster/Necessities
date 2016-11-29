package com.crossge.necessities.Commands.Economy;

import com.crossge.necessities.Necessities;
import org.bukkit.command.CommandSender;

public class CmdPlayers implements EconomyCmd {
    public boolean commandUse(CommandSender sender, String[] args) {
        sender.sendMessage(Necessities.getInstance().getVar().getMessages() + Necessities.getInstance().getBalChecks().players() + " players have joined the server.");
        return true;
    }
}