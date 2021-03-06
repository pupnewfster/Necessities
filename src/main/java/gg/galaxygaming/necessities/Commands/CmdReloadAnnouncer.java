package gg.galaxygaming.necessities.Commands;

import gg.galaxygaming.necessities.Necessities;
import gg.galaxygaming.necessities.Variables;
import org.bukkit.command.CommandSender;

public class CmdReloadAnnouncer implements Cmd {

    @Override
    public boolean commandUse(CommandSender sender, String[] args) {
        Variables var = Necessities.getVar();
        sender.sendMessage(var.getMessages() + "Reloading Announcer.");
        Necessities.getAnnouncer().reloadAnnouncer();
        sender.sendMessage(var.getMessages() + "Announcer reloaded.");
        return true;
    }

    public boolean isPaintballEnabled() {
        return true;
    }
}