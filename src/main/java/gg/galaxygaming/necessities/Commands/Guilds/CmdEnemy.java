package gg.galaxygaming.necessities.Commands.Guilds;

import gg.galaxygaming.necessities.Guilds.Guild;
import gg.galaxygaming.necessities.Necessities;
import gg.galaxygaming.necessities.RankManager.User;
import gg.galaxygaming.necessities.Variables;
import java.util.Collections;
import java.util.List;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdEnemy implements GuildCmd {

    public boolean commandUse(CommandSender sender, String[] args) {
        Variables var = Necessities.getVar();
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (!p.hasPermission("Necessities.guilds.enemy")) {
                sender.sendMessage(
                      var.getEr() + "Error: " + var.getErMsg() + "You have not have permission to use /guild neutral.");
                return true;
            }
            if (args.length == 0) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg()
                      + "Must enter a name for the guild you wish to become enemies with.");
                return true;
            }
            Guild g = Necessities.getGM().getGuildVerbose(args[0]);
            if (g == null) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg()
                      + "That guild does not exists, or no guild owner found with that name.");
                return true;
            }
            User u = Necessities.getUM().getUser(p.getUniqueId());
            if (u.getGuild() == null) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You are not in a guild.");
                return true;
            }
            if (g.equals(u.getGuild()) || u.getGuild().isEnemy(g)) {
                sender.sendMessage(
                      var.getEr() + "Error: " + var.getErMsg() + "You are already enemies with that guild.");
                return true;
            }
            g.addEnemy(u.getGuild());
            u.getGuild().addEnemy(g);
            g.sendMods(var.getMessages() + "You are now enemies with " + var.getObj() + u.getGuild().getName());
            u.getGuild().sendMods(var.getMessages() + "You are now enemies with " + var.getObj() + g.getName());
        } else {
            sender.sendMessage(
                  var.getEr() + "Error: " + var.getErMsg() + "You must be in a guild to be able to become enemies.");
        }
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        //TODO: TabComplete
        return Collections.emptyList();
    }
}