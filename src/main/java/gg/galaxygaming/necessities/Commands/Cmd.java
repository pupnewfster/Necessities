package gg.galaxygaming.necessities.Commands;

import java.util.Collections;
import java.util.List;
import org.bukkit.command.CommandSender;

public interface Cmd {

    /**
     * Performs a command for the given sender with the given arguments.
     *
     * @param sender The sender of the command.
     * @param args The arguments of the command.
     * @return True.
     */
    boolean commandUse(CommandSender sender, String[] args);

    /**
     * Checks if the command is enabled on PaintBall. Eventually will be renamed to isEnabled();
     *
     * @return True if the command is enabled, false otherwise.
     */
    default boolean isPaintballEnabled() {
        return false;
    }

    /**
     * Gets the possible tab completions for this command.
     *
     * @param sender The sender of the tab complete request.
     * @param args The current arguments for the command.
     * @return The list of possible tab completions.
     */
    default List<String> tabComplete(CommandSender sender, String[] args) {
        return Collections.emptyList();
    }
}