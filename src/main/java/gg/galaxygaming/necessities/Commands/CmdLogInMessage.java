package gg.galaxygaming.necessities.Commands;

import gg.galaxygaming.necessities.Necessities;
import gg.galaxygaming.necessities.Variables;
import java.io.File;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class CmdLogInMessage implements Cmd {

    private final File configFileLogIn = new File("plugins/Necessities", "loginmessages.yml");

    public boolean commandUse(CommandSender sender, String[] args) {
        Variables var = Necessities.getVar();
        if (sender instanceof Player) {
            Player p = (Player) sender;
            String loginMessage = "{RANK} {NAME}&r joined the game.";
            if (args.length != 0) {
                StringBuilder loginMessageBuilder = new StringBuilder();
                for (String arg : args) {
                    loginMessageBuilder.append(arg).append(' ');
                }
                loginMessage = loginMessageBuilder.toString();
                if (!loginMessage.contains("{NAME}")) {
                    loginMessage = "{RANK} {NAME}&r " + loginMessage;
                }
                loginMessage = loginMessage.trim();
            }
            YamlConfiguration configLogIn = YamlConfiguration.loadConfiguration(configFileLogIn);
            configLogIn.set(p.getUniqueId().toString(), loginMessage);
            try {
                configLogIn.save(configFileLogIn);
            } catch (Exception ignored) {
            }
            p.sendMessage("Login message set to: " + ChatColor.YELLOW + ChatColor.translateAlternateColorCodes('&',
                  loginMessage.replaceAll("\\{NAME}", p.getDisplayName()).replaceAll("\\{RANK}",
                        Necessities.getUM().getUser(p.getUniqueId()).getRank().getTitle()))
                  .replaceAll(ChatColor.RESET.toString(), ChatColor.YELLOW.toString()));
        } else {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "The console does not have a login message.");
        }
        return true;
    }

    public boolean isPaintballEnabled() {
        return true;
    }
}