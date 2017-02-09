package gg.galaxygaming.necessities.Commands;

import org.bukkit.command.CommandSender;

import java.util.List;

public interface Cmd {
    boolean commandUse(CommandSender sender, String[] args);

    default boolean isPaintballEnabled() {
        return false;
    }

    default List<String> tabComplete(CommandSender sender, String[] args) {
        return null;
    }
}