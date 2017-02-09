package gg.galaxygaming.necessities.Commands.Guilds;

import gg.galaxygaming.necessities.Guilds.Guild;
import gg.galaxygaming.necessities.Guilds.GuildManager;
import gg.galaxygaming.necessities.Necessities;
import gg.galaxygaming.necessities.RankManager.User;
import gg.galaxygaming.necessities.RankManager.UserManager;
import gg.galaxygaming.necessities.Utils;
import gg.galaxygaming.necessities.Variables;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;

public class CmdList implements GuildCmd {
    public boolean commandUse(CommandSender sender, String[] args) {
        ArrayList<String> guildList = new ArrayList<>();
        Variables var = Necessities.getVar();
        UserManager um = Necessities.getUM();
        GuildManager gm = Necessities.getGM();
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (!p.hasPermission("Necessities.guilds.list")) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You have not have permission to use /guild list.");
                return true;
            }
            User u = um.getUser(p.getUniqueId());
            int guildless = 0;
            for (UUID uuid : um.getUsers().keySet()) {
                User x = um.getUser(uuid);
                if (x.getGuild() == null && x.getPlayer() != null && p.canSee(x.getPlayer()))
                    guildless++;
            }
            guildList.add(var.getGuildMsgs() + "" + guildless + " guildless online");
            for (String name : gm.getGuilds().keySet()) {
                Guild g = gm.getGuild(name);
                guildList.add(g.relation(u.getGuild()) + g.getName() + " " + var.getGuildMsgs() + g.getOnline(p.hasPermission("Necessities.seehidden")) + "/" + g.getTotal() + " online, " + g.getLand() + "/" +
                        Utils.roundTwoDecimals(g.getPower()) + "/" + g.getMaxPower() + ".00");
            }
        } else {
            int guildless = 0;
            for (UUID uuid : um.getUsers().keySet())
                if (um.getUser(uuid).getGuild() == null)
                    guildless++;
            guildList.add(var.getGuildMsgs() + "" + guildless + " guildless online");
            for (String name : gm.getGuilds().keySet()) {
                Guild g = gm.getGuild(name);
                guildList.add(var.getNeutral() + g.getName() + " " + var.getGuildMsgs() + g.getOnline(true) + "/" + g.getTotal() + " online, " + g.getLand() + "/" +
                        Utils.roundTwoDecimals(g.getPower()) + "/" + g.getMaxPower() + ".00");
            }
        }
        int page = 0;
        if (args.length != 0 && !Utils.legalInt(args[0])) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must enter a valid help page.");
            return true;
        }
        if (args.length != 0)
            page = Integer.parseInt(args[0]);
        if (args.length == 0 || page == 0)
            page = 1;
        int time = 0;
        int rounder = 0;
        if (guildList.size() % 10 != 0)
            rounder = 1;
        int totalPages = (guildList.size() / 10) + rounder;
        if (page > totalPages) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Input a number from 1 to " + Integer.toString(totalPages));
            return true;
        }
        sender.sendMessage(var.getMessages() + "Guild list [" + Integer.toString(page) + "/" + Integer.toString(totalPages) + "]");
        page = page - 1;
        String message = getLine(page, time, guildList);
        while (message != null) {
            sender.sendMessage(message);
            time++;
            message = getLine(page, time, guildList);
        }
        if (page + 1 < totalPages)
            sender.sendMessage(var.getMessages() + "Type " + var.getObj() + "/guild list " + Integer.toString(page + 2) + var.getMessages() + " to read the next page.");
        return true;
    }

    private String getLine(int page, int time, ArrayList<String> guildList) {
        page *= 10;
        if (guildList.size() < time + page + 1 || time == 10)
            return null;
        return guildList.get(page + time);
    }
}