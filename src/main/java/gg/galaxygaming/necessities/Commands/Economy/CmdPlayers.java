package gg.galaxygaming.necessities.Commands.Economy;

import gg.galaxygaming.necessities.Necessities;
import org.bukkit.command.CommandSender;

public class CmdPlayers implements EconomyCmd {
    public boolean commandUse(CommandSender sender, String[] args) {
        sender.sendMessage(Necessities.getVar().getMessages() + Necessities.getEconomy().players() + " players have joined the server.");
        return true;
    }

    public boolean isPaintballEnabled() {
        return true;
    }
}