package com.crossge.necessities.Commands.Guilds;

import com.crossge.necessities.Commands.Cmd;
import org.bukkit.command.CommandSender;

public class CmdGuild implements Cmd {
    public boolean commandUse(CommandSender sender, String[] args) {
        String subCmd = "help";
        String[] subArgs = new String[0];
        if (args.length >= 1) {
            subCmd = args[0];
            subArgs = new String[args.length - 1];
            System.arraycopy(args, 1, subArgs, 0, args.length - 1);
        }
        GuildCmd cmd;
        if (subCmd.equalsIgnoreCase("list"))
            cmd = new CmdList();
        else if (subCmd.equalsIgnoreCase("info") || subCmd.equalsIgnoreCase("who"))
            cmd = new CmdInfo();
        else if (subCmd.equalsIgnoreCase("power") || subCmd.equalsIgnoreCase("pow"))
            cmd = new CmdPower();
        else if (subCmd.equalsIgnoreCase("join") || subCmd.equalsIgnoreCase("j"))
            cmd = new CmdJoin();
        else if (subCmd.equalsIgnoreCase("leave") || subCmd.equalsIgnoreCase("l"))
            cmd = new CmdLeave();
        else if (subCmd.equalsIgnoreCase("home") || subCmd.equalsIgnoreCase("h"))
            cmd = new CmdHome();
        else if (subCmd.equalsIgnoreCase("map") || subCmd.equalsIgnoreCase("m"))
            cmd = new CmdMap();
        else if (subCmd.equalsIgnoreCase("create"))
            cmd = new CmdCreate();
        else if (subCmd.equalsIgnoreCase("description") || subCmd.equalsIgnoreCase("desc"))
            cmd = new CmdDescription();
        else if (subCmd.equalsIgnoreCase("sethome"))
            cmd = new CmdSetHome();
        else if (subCmd.equalsIgnoreCase("invite") || subCmd.equalsIgnoreCase("inv"))
            cmd = new CmdInvite();
        else if (subCmd.equalsIgnoreCase("uninvite") || subCmd.equalsIgnoreCase("uninv"))
            cmd = new CmdUninvite();
        else if (subCmd.equalsIgnoreCase("kick") || subCmd.equalsIgnoreCase("k"))
            cmd = new CmdKick();
        else if (subCmd.equalsIgnoreCase("mod") || subCmd.equalsIgnoreCase("officer") || subCmd.equalsIgnoreCase("moderator") || subCmd.equalsIgnoreCase("demod"))
            cmd = new CmdMod();
        else if (subCmd.equalsIgnoreCase("leader") || subCmd.equalsIgnoreCase("owner"))
            cmd = new CmdLeader();
        else if (subCmd.equalsIgnoreCase("claim"))
            cmd = new CmdClaim();
        else if (subCmd.equalsIgnoreCase("autoclaim"))
            cmd = new CmdAutoclaim();
        else if (subCmd.equalsIgnoreCase("unclaim"))
            cmd = new CmdUnclaim();
        else if (subCmd.equalsIgnoreCase("unclaimall"))
            cmd = new CmdUnclaimAll();
        else if (subCmd.equalsIgnoreCase("ally"))
            cmd = new CmdAlly();
        else if (subCmd.equalsIgnoreCase("neutral") || subCmd.equalsIgnoreCase("unally") || subCmd.equalsIgnoreCase("unenemy"))
            cmd = new CmdNeutral();
        else if (subCmd.equalsIgnoreCase("enemy"))
            cmd = new CmdEnemy();
        else if (subCmd.equalsIgnoreCase("flag"))
            cmd = new CmdFlag();
        else if (subCmd.equalsIgnoreCase("disband"))
            cmd = new CmdDisband();
        else if (subCmd.equalsIgnoreCase("chat") || subCmd.equalsIgnoreCase("c"))
            cmd = new CmdChat();
        else if (subCmd.equalsIgnoreCase("rename") || subCmd.equalsIgnoreCase("r"))
            cmd = new CmdRename();
        else
            cmd = new CmdHelp();
        return cmd.commandUse(sender, subArgs);
    }
}