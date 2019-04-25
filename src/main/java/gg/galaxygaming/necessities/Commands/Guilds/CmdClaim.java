package gg.galaxygaming.necessities.Commands.Guilds;

import gg.galaxygaming.necessities.Guilds.Guild;
import gg.galaxygaming.necessities.Necessities;
import gg.galaxygaming.necessities.RankManager.User;
import gg.galaxygaming.necessities.Utils;
import gg.galaxygaming.necessities.Variables;
import org.bukkit.Chunk;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdClaim implements GuildCmd {

    public boolean commandUse(CommandSender sender, String[] args) {
        Variables var = Necessities.getVar();
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (!p.hasPermission("Necessities.guilds.claim")) {
                sender.sendMessage(
                      var.getEr() + "Error: " + var.getErMsg() + "You have not have permission to use /guild claim.");
                return true;
            }
            User u = Necessities.getUM().getUser(p.getUniqueId());
            if (u.getGuild() == null || u.getGuild().getRank(p.getUniqueId()) == null || u.getGuild()
                  .getRank(p.getUniqueId()).equalsIgnoreCase("member")) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg()
                      + "You must be a mod or higher to claim land for your guild.");
                return true;
            }
            if (u.getGuild().getPower() != -1 && u.getGuild().getLand() >= u.getGuild().getPower() && !(
                  u.getGuild().getPower() < 1)) {
                sender.sendMessage(
                      var.getEr() + "Error: " + var.getErMsg() + "Your guild does not have that much power.");
                return true;
            }
            if (args.length == 0 || args[0].equals("0") || !Utils.legalInt(args[0])) {
                Guild owner = Necessities.getGM().chunkOwner(p.getLocation().getChunk());
                if (owner != null) {
                    if (owner.equals(u.getGuild())) {
                        sender.sendMessage(
                              var.getEr() + "Error: " + var.getErMsg() + "Your guild already owns this chunk.");
                    } else {
                        if (owner.getPower() == -1) {
                            sender.sendMessage(
                                  var.getEr() + "Error: " + var.getErMsg() + "This chunk cannot be claimed.");
                            return true;
                        }
                        if (owner.isAlly(u.getGuild()) || owner.isNeutral(u.getGuild())) {
                            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg()
                                  + "You can only claim land from enemies not allies or neutral.");
                            return true;
                        }
                        if (owner.getLand() > owner.getPower() || p.hasPermission("Necessities.guilds.admin")) {
                            u.getGuild().claim(p.getLocation().getChunk());
                            owner.unclaim(p.getLocation().getChunk());
                            u.getGuild().sendMods(
                                  var.getMessages() + "Successfully claimed chunk " + var.getObj() + p.getLocation()
                                        .getChunk().getX() + var.getMessages() +
                                        ", " + var.getObj() + p.getLocation().getChunk().getZ() + var.getMessages()
                                        + " in world " + var.getObj() + p.getWorld().getName() +
                                        var.getMessages() + " from the guild " + var.getObj() + owner.getName());
                        } else {
                            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg()
                                  + "This chunk is already claimed by another guild.");
                        }
                    }
                    return true;
                }
                u.getGuild().claim(p.getLocation().getChunk());
                u.getGuild().sendMods(
                      var.getMessages() + "Successfully claimed chunk " + var.getObj() + p.getLocation().getChunk()
                            .getX() + var.getMessages() + ", " +
                            var.getObj() + p.getLocation().getChunk().getZ() + var.getMessages() + " in world " + var
                            .getObj() + p.getWorld().getName());
                return true;
            }
            int r = Integer.parseInt(args[0]);
            for (int i = 1; i < r; i++) {
                for (int x = -i; x <= i; x++) {
                    for (int z = -i; z <= i; z++) {
                        Chunk c = p.getWorld()
                              .getChunkAt(p.getLocation().getChunk().getX() + x, p.getLocation().getChunk().getZ() + z);
                        if (u.getGuild().getPower() != -1 && u.getGuild().getLand() >= u.getGuild().getPower()) {
                            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg()
                                  + "Your guild does not have that much power, claiming stopped.");
                            return true;
                        }
                        Guild o = Necessities.getGM().chunkOwner(c);
                        if (o != null) {
                            if (!o.equals(u.getGuild()) && o.getPower() != -1 && (
                                  o.getLand() > o.getPower() && !o.isAlly(u.getGuild()) && !o.isNeutral(u.getGuild()) ||
                                        p.hasPermission("Necessities.guilds.admin"))) {
                                u.getGuild().claim(c);
                                o.unclaim(c);
                                u.getGuild().sendMods(
                                      var.getMessages() + "Successfully claimed chunk " + var.getObj() + c.getX() + var
                                            .getMessages() + ", " +
                                            var.getObj() + c.getZ() + var.getMessages() + " in world " + var.getObj()
                                            + c.getWorld().getName() + var.getMessages() +
                                            " from the guild " + var.getObj() + o.getName());
                            }
                        } else {
                            u.getGuild().claim(c);
                            u.getGuild().sendMods(
                                  var.getMessages() + "Successfully claimed chunk " + var.getObj() + c.getX() + var
                                        .getMessages() + ", " +
                                        var.getObj() + c.getZ() + var.getMessages() + " in world " + var.getObj() + c
                                        .getWorld().getName());
                        }
                    }
                }
            }
        } else {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must be logged in to claim land.");
        }
        return true;
    }
}