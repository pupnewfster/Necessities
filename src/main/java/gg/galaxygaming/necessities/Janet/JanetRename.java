package gg.galaxygaming.necessities.Janet;

import gg.galaxygaming.necessities.Necessities;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class JanetRename {
    public String parseRename(String message, Player p) {
        YamlConfiguration config = Necessities.getInstance().getConfig();
        String censored = message;
        Janet bot = Necessities.getInstance().getBot();
        if (config.getBoolean("Necessities.language") && !p.hasPermission("Necessities.language"))
            censored = bot.internalLang(censored);
        if (config.getBoolean("Necessities.advertise") && !p.hasPermission("Necessities.advertise"))
            censored = bot.internalAdds(censored);
        return censored;
    }
}