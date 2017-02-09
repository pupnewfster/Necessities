package gg.galaxygaming.necessities.Commands;

import gg.galaxygaming.necessities.Console;
import gg.galaxygaming.necessities.Necessities;
import gg.galaxygaming.necessities.Variables;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdToggleChat implements Cmd {
    public boolean commandUse(CommandSender sender, String[] args) {
        Variables var = Necessities.getVar();
        if (sender instanceof Player)
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You cannot use this command, it is console specific.");
        else {
            Console console = Necessities.getConsole();
            sender.sendMessage(var.getMessages() + (console.chatToggled() ? "Toggled back to command mode." : "Toggled to chat mode."));
            console.chatToggle();
        }
        return true;
    }

    public boolean isPaintballEnabled() {
        return true;
    }
}