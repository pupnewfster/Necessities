package com.crossge.necessities.Commands.Guilds;

import com.crossge.necessities.Guilds.Guild;
import com.crossge.necessities.RankManager.User;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class CmdEnemy extends GuildCmd {
    public boolean commandUse(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (!p.hasPermission("Necessities.guilds.enemy")) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You have not have permission to use /guild neutral.");
                return true;
            }
            if (args.length == 0) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Must enter a name for the guild you wish to become enemies with.");
                return true;
            }
            Guild g = gm.getGuild(args[0]);
            if (g == null) {
                UUID uuid = get.getID(args[0]);
                if (uuid == null)
                    uuid = get.getOfflineID(args[0]);
                if (uuid == null) {
                    sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "That player has not joined the server. If the player is offline, please use the full and most recent name.");
                    return true;
                }
                if (um.getUser(uuid) != null)
                    g = um.getUser(uuid).getGuild();
            }
            if (g == null) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "That guild does not exists.");
                return true;
            }
            User u = um.getUser(p.getUniqueId());
            if (u.getGuild() == null) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You are not in a guild.");
                return true;
            }
            if (g.equals(u.getGuild()) || u.getGuild().isEnemy(g)) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You are already enemies with that guild.");
                return true;
            }
            g.addEnemy(u.getGuild());
            u.getGuild().addEnemy(g);
            g.sendMods(var.getMessages() + "You are now enemies with " + var.getObj() + u.getGuild().getName());
            u.getGuild().sendMods(var.getMessages() + "You are now enemies with " + var.getObj() + g.getName());
        } else
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must be in a guild to be able to become enemies.");
        return true;
    }
}