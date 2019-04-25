package gg.galaxygaming.necessities.Janet;

import gg.galaxygaming.necessities.Necessities;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class JanetRename {

    /**
     * Potentially censors the text from a /rename or renaming in the anvil.
     *
     * @param message The message to censor.
     * @param p The player who is trying to rename an item.
     * @return The potentially censored string.
     */
    public String parseRename(String message, Player p) {
        YamlConfiguration config = Necessities.getInstance().getConfig();
        String censored = message;
        Janet bot = Necessities.getBot();
        if (config.getBoolean("Necessities.language") && !p.hasPermission("Necessities.language")) {
            censored = bot.internalLang(censored);
        }
        if (config.getBoolean("Necessities.advertise") && !p.hasPermission("Necessities.advertise")) {
            censored = bot.internalAdds(censored);
        }
        return censored;
    }
}