package gg.galaxygaming.necessities.Commands.Guilds;

import org.bukkit.command.CommandSender;

interface GuildCmd {
    /**
     * Performs a command for the given sender with the given arguments.
     * @param sender The sender of the command.
     * @param args   The arguments of the command.
     * @return True.
     */
    boolean commandUse(CommandSender sender, String[] args);
}