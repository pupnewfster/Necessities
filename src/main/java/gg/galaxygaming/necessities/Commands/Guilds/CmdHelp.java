package gg.galaxygaming.necessities.Commands.Guilds;

import gg.galaxygaming.necessities.Necessities;
import gg.galaxygaming.necessities.Utils;
import gg.galaxygaming.necessities.Variables;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class CmdHelp implements GuildCmd {
    public boolean commandUse(CommandSender sender, String[] args) {
        Variables var = Necessities.getInstance().getVar();
        if (sender instanceof Player) {
            if (!sender.hasPermission("Necessities.guilds.help")) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You have not have permission to use /guild help.");
                return true;
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
        ArrayList<String> helpList = new ArrayList<>();
        setHelp(helpList, sender);
        if (helpList.size() % 10 != 0)
            rounder = 1;
        int totalPages = (helpList.size() / 10) + rounder;
        if (page > totalPages) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Input a number from 1 to " + Integer.toString(totalPages));
            return true;
        }
        sender.sendMessage(var.getMessages() + "Guild help [" + Integer.toString(page) + "/" + Integer.toString(totalPages) + "]");
        page = page - 1;
        String message = getLine(page, time, helpList);
        while (message != null) {
            sender.sendMessage(message);
            time++;
            message = getLine(page, time, helpList);
        }
        if (page + 1 < totalPages)
            sender.sendMessage(var.getMessages() + "Type " + var.getObj() + "/guild help " + Integer.toString(page + 2) + var.getMessages() + " to read the next page.");
        return true;
    }

    private String getLine(int page, int time, ArrayList<String> helpList) {
        page *= 10;
        if (helpList.size() < time + page + 1 || time == 10)
            return null;
        return helpList.get(page + time);
    }

    private void setHelp(ArrayList<String> helpList, CommandSender sender) {
        Variables var = Necessities.getInstance().getVar();
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (p.hasPermission("Necessities.guilds.list"))
                helpList.add(var.getMessages() + "/guild list" + ChatColor.WHITE + " - Shows a list of the guilds.");
            if (p.hasPermission("Necessities.guilds.info"))
                helpList.add(var.getMessages() + "/guild info [guild]" + ChatColor.WHITE + " - Shows info about [guild].");
            if (p.hasPermission("Necessities.guilds.power"))
                helpList.add(var.getMessages() + "/guild power [player]" + ChatColor.WHITE + " - Shows how much power [player] has.");
            if (p.hasPermission("Necessities.guilds.join"))
                helpList.add(var.getMessages() + "/guild join [guild]" + ChatColor.WHITE + " - Joins [guild] if invited.");
            if (p.hasPermission("Necessities.guilds.leave"))
                helpList.add(var.getMessages() + "/guild leave" + ChatColor.WHITE + " - Leaves your current guild.");
            if (p.hasPermission("Necessities.guilds.home"))
                helpList.add(var.getMessages() + "/guild home" + ChatColor.WHITE + " - Teleports you to your guild's home.");
            if (p.hasPermission("Necessities.guilds.map"))
                helpList.add(var.getMessages() + "/guild map" + ChatColor.WHITE + " - Displays a map of claimed chunks around you.");
            if (p.hasPermission("Necessities.guilds.create"))
                helpList.add(var.getMessages() + "/guild create [name]" + ChatColor.WHITE + " - Creates a guild named [name].");
            if (p.hasPermission("Necessities.guilds.description"))
                helpList.add(var.getMessages() + "/guild description [new description]" + ChatColor.WHITE + " - Sets [description] for your guild.");
            if (p.hasPermission("Necessities.guilds.sethome"))
                helpList.add(var.getMessages() + "/guild sethome" + ChatColor.WHITE + " - Sets the home for your guild to where you are standing.");
            if (p.hasPermission("Necessities.guilds.invite"))
                helpList.add(var.getMessages() + "/guild invite [player]" + ChatColor.WHITE + " - Invites [player] to join your guild.");
            if (p.hasPermission("Necessities.guilds.uninvite"))
                helpList.add(var.getMessages() + "/guild uninvite [player]" + ChatColor.WHITE + " - Cancels invite offer of [player].");
            if (p.hasPermission("Necessities.guilds.kick"))
                helpList.add(var.getMessages() + "/guild kick [player]" + ChatColor.WHITE + " - Kicks [player] from your guild.");
            if (p.hasPermission("Necessities.guilds.mod"))
                helpList.add(var.getMessages() + "/guild mod [player]" + ChatColor.WHITE + " - Makes [player] a mod in your guild.");
            if (p.hasPermission("Necessities.guilds.leader"))
                helpList.add(var.getMessages() + "/guild leader [player]" + ChatColor.WHITE + " - Transfers leadership of guild over to [player].");
            if (p.hasPermission("Necessities.guilds.claim"))
                helpList.add(var.getMessages() + "/guild claim [radius]" + ChatColor.WHITE + " - Claims the land around you for your guild.");
            if (p.hasPermission("Necessities.guilds.autoclaim"))
                helpList.add(var.getMessages() + "/guild autoclaim" + ChatColor.WHITE + " - Enables automatically claiming land as you move.");
            if (p.hasPermission("Necessities.guilds.unclaim"))
                helpList.add(var.getMessages() + "/guild unclaim" + ChatColor.WHITE + " - Unclaims the chunk of land you are on.");
            if (p.hasPermission("Necessities.guilds.unclaimall"))
                helpList.add(var.getMessages() + "/guild unclaimall" + ChatColor.WHITE + " - Unclaims all land your guild has claimed.");
            if (p.hasPermission("Necessities.guilds.ally"))
                helpList.add(var.getMessages() + "/guild ally [guild]" + ChatColor.WHITE + " - Sends [guild] an ally request.");
            if (p.hasPermission("Necessities.guilds.neutral"))
                helpList.add(var.getMessages() + "/guild neutral [guild]" + ChatColor.WHITE + " - Sets your guild as neutral with [guild].");
            if (p.hasPermission("Necessities.guilds.enemy"))
                helpList.add(var.getMessages() + "/guild enemy [guild]" + ChatColor.WHITE + " - Sets [guild] as an enemy.");
            if (p.hasPermission("Necessities.guilds.flag"))
                helpList.add(var.getMessages() + "/guild flag [pvp|permanent|infinite|explosions] [value]" + ChatColor.WHITE + " - Sets a guild flag.");
            if (p.hasPermission("Necessities.guilds.disband"))
                helpList.add(var.getMessages() + "/guild disband" + ChatColor.WHITE + " - Disbands your guild.");
            if (p.hasPermission("Necessities.guilds.chat"))
                helpList.add(var.getMessages() + "/guild chat" + ChatColor.WHITE + " - Toggles guild chat.");
            if (p.hasPermission("Necessities.guilds.rename"))
                helpList.add(var.getMessages() + "/guild rename [name]" + ChatColor.WHITE + " - Renames your guild to [name].");
            if (p.hasPermission("Necessities.guilds.help"))
                helpList.add(var.getMessages() + "/guild help [page]" + ChatColor.WHITE + " - Shows help for guild commands.");
        } else {
            helpList.add(var.getMessages() + "/guild list" + ChatColor.WHITE + " - Shows a list of the guilds.");
            helpList.add(var.getMessages() + "/guild info [guild]" + ChatColor.WHITE + " - Shows info about [guild].");
            helpList.add(var.getMessages() + "/guild power [player]" + ChatColor.WHITE + " - Shows how much power [player] has.");
            helpList.add(var.getMessages() + "/guild join [guild]" + ChatColor.WHITE + " - Joins [guild] if invited.");
            helpList.add(var.getMessages() + "/guild leave" + ChatColor.WHITE + " - Leaves your current guild.");
            helpList.add(var.getMessages() + "/guild home" + ChatColor.WHITE + " - Teleports you to your guild's home.");
            helpList.add(var.getMessages() + "/guild map" + ChatColor.WHITE + " - Displays a map of claimed chunks around you.");
            helpList.add(var.getMessages() + "/guild create [name]" + ChatColor.WHITE + " - Creates a guild named [name].");
            helpList.add(var.getMessages() + "/guild description [new description]" + ChatColor.WHITE + " - Sets [description] for your guild.");
            helpList.add(var.getMessages() + "/guild sethome" + ChatColor.WHITE + " - Sets the home for your guild to where you are standing.");
            helpList.add(var.getMessages() + "/guild invite [player]" + ChatColor.WHITE + " - Invites [player] to join your guild.");
            helpList.add(var.getMessages() + "/guild uninvite [player]" + ChatColor.WHITE + " - Cancels invite offer of [player].");
            helpList.add(var.getMessages() + "/guild kick [player]" + ChatColor.WHITE + " - Kicks [player] from your guild.");
            helpList.add(var.getMessages() + "/guild mod [player]" + ChatColor.WHITE + " - Makes [player] a mod in your guild.");
            helpList.add(var.getMessages() + "/guild leader [player]" + ChatColor.WHITE + " - Transfers leadership of guild over to [player].");
            helpList.add(var.getMessages() + "/guild claim [radius]" + ChatColor.WHITE + " - Claims the land around you for your guild.");
            helpList.add(var.getMessages() + "/guild autoclaim" + ChatColor.WHITE + " - Enables automatically claiming land as you move.");
            helpList.add(var.getMessages() + "/guild unclaim" + ChatColor.WHITE + " - Unclaims the chunk of land you are on.");
            helpList.add(var.getMessages() + "/guild unclaimall" + ChatColor.WHITE + " - Unclaims all land your guild has claimed.");
            helpList.add(var.getMessages() + "/guild ally [guild]" + ChatColor.WHITE + " - Sends [guild] an ally request.");
            helpList.add(var.getMessages() + "/guild neutral [guild]" + ChatColor.WHITE + " - Sets yourself as neutral with [guild].");
            helpList.add(var.getMessages() + "/guild enemy [guild]" + ChatColor.WHITE + " - Sets [guild] as an enemy.");
            helpList.add(var.getMessages() + "/guild flag [pvp|permanent|infinite] [value]" + ChatColor.WHITE + " - Sets a guild flag.");
            helpList.add(var.getMessages() + "/guild disband" + ChatColor.WHITE + " - Disbands your guild.");
            helpList.add(var.getMessages() + "/guild chat" + ChatColor.WHITE + " - Toggles guild chat.");
            helpList.add(var.getMessages() + "/guild rename [name]" + ChatColor.WHITE + " - Renames your guild to [name].");
            helpList.add(var.getMessages() + "/guild help [page]" + ChatColor.WHITE + " - Shows help for guild commands.");
        }
    }
}