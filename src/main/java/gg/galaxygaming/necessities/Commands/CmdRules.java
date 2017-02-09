package gg.galaxygaming.necessities.Commands;

import gg.galaxygaming.necessities.Necessities;
import gg.galaxygaming.necessities.Variables;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class CmdRules implements Cmd {
    public boolean commandUse(CommandSender sender, String[] args) {
        Variables var = Necessities.getInstance().getVar();
        File f = new File("plugins/Necessities/rules.txt");
        if (!f.exists()) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "The file does not exist somehow...");
            return true;
        }
        try {
            BufferedReader read = new BufferedReader(new FileReader(f));
            String line;
            boolean hasText = false;
            while ((line = read.readLine()) != null)
                if (!line.equals("")) {
                    hasText = true;
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', line));
                }
            if (!hasText)
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "No rules set.");
            read.close();
        } catch (Exception ignored) {
        }
        return true;
    }

    public boolean isPaintballEnabled() {
        return true;
    }
}	