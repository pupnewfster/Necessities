package gg.galaxygaming.necessities.Commands;

import gg.galaxygaming.necessities.Necessities;
import gg.galaxygaming.necessities.Variables;
import java.io.File;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class CmdLogOutMessage implements Cmd {

    private final File configFileLogOut = new File("plugins/Necessities", "logoutmessages.yml");

    public boolean commandUse(CommandSender sender, String[] args) {
        Variables var = Necessities.getVar();
        if (sender instanceof Player) {
            Player p = (Player) sender;
            String logoutMessage = "{RANK} {NAME}&r Disconnected.";
            if (args.length != 0) {
                StringBuilder logoutMessageBuilder = new StringBuilder();
                for (String arg : args) {
                    logoutMessageBuilder.append(arg).append(' ');
                }
                logoutMessage = logoutMessageBuilder.toString();
                if (!logoutMessage.contains("{NAME}")) {
                    logoutMessage = "{RANK} {NAME}&r " + logoutMessage;
                }
                logoutMessage = logoutMessage.trim();
            }
            YamlConfiguration configLogOut = YamlConfiguration.loadConfiguration(configFileLogOut);
            configLogOut.set(p.getUniqueId().toString(), logoutMessage);
            try {
                configLogOut.save(configFileLogOut);
            } catch (Exception ignored) {
            }
            p.sendMessage("Logout message set to: " + ChatColor.YELLOW + ChatColor.translateAlternateColorCodes('&',
                  logoutMessage.replaceAll("\\{NAME}", p.getDisplayName()).replaceAll("\\{RANK}",
                        Necessities.getUM().getUser(p.getUniqueId()).getRank().getTitle()))
                  .replaceAll(ChatColor.RESET.toString(), ChatColor.YELLOW.toString()));
        } else {
            sender.sendMessage(
                  var.getEr() + "Error: " + var.getErMsg() + "The console does not have a logout message.");
        }
        return true;
    }

    public boolean isPaintballEnabled() {
        return true;
    }
}