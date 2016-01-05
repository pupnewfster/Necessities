package com.crossge.necessities.Janet;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;

public class JanetRename {
    private File configFile = new File("plugins/Necessities", "config.yml");
    Janet bot = new Janet();

    public String parseRename(String message, Player p) {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        String censored = message;
        if (config.getBoolean("Necessities.language") && !p.hasPermission("Necessities.language"))
            censored = bot.internalLang(censored);
        if (config.getBoolean("Necessities.advertise") && !p.hasPermission("Necessities.advertise"))
            censored = bot.internalAdds(censored);
        return censored;
    }
}