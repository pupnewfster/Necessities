package gg.galaxygaming.necessities.Commands;

import gg.galaxygaming.necessities.Necessities;
import gg.galaxygaming.necessities.Variables;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdUnbanIP implements Cmd {

    public boolean commandUse(CommandSender sender, String[] args) {
        Variables var = Necessities.getVar();
        if (args.length == 0) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must enter an ip to unban.");
            return true;
        }
        boolean validIp = false;
        try {
            Pattern ipAdd = Pattern.compile(
                  "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
                        + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");
            validIp = ipAdd.matcher(args[0]).matches();
        } catch (Exception ignored) {
        }
        if (!validIp) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Invalid ip.");
            return true;
        }
        String name =
              sender instanceof Player ? sender.getName() : Necessities.getConsole().getName().replaceAll(":", "");
        BanList bans = Bukkit.getBanList(BanList.Type.IP);
        String theirIP = args[0];
        if (!bans.isBanned(theirIP)) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "That ip is not banned.");
            return true;
        }
        bans.pardon(theirIP);
        Bukkit.broadcastMessage(var.getMessages() + name + " unbanned " + theirIP + '.');
        return true;
    }

    public boolean isPaintballEnabled() {
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        //TODO: TabComplete
        return Collections.emptyList();
    }
}