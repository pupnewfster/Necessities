package gg.galaxygaming.necessities.Janet;

import gg.galaxygaming.necessities.Necessities;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.BookMeta;

public class JanetBooks {
    public String newTitle(String title, Player p) {
        YamlConfiguration config = Necessities.getInstance().getConfig();
        Janet bot = Necessities.getInstance().getBot();
        String censored = title.trim();
        if (config.getBoolean("Necessities.language") && !p.hasPermission("Necessities.language"))
            censored = bot.internalLang(censored);
        if (config.getBoolean("Necessities.advertise") && !p.hasPermission("Necessities.advertise"))
            censored = bot.internalAdds(censored);
        return censored;
    }

    public BookMeta newMeta(BookMeta book, Player p) {
        for (int page = 0; page < book.getPageCount(); page++)
            book.setPage(page + 1, censor(book.getPage(page + 1), p));
        return book;
    }

    private String censor(String page, Player p) {
        YamlConfiguration config = Necessities.getInstance().getConfig();
        String censored = page;
        Janet bot = Necessities.getInstance().getBot();
        if (config.getBoolean("Necessities.language") && !p.hasPermission("Necessities.language")) {
            String tempCensor = "";
            String[] lines = censored.split("\n");
            for (String line : lines)
                tempCensor += bot.internalLang(line) + "\n";
            censored = tempCensor.substring(0, tempCensor.lastIndexOf("\n"));
        }
        if (config.getBoolean("Necessities.advertise") && !p.hasPermission("Necessities.advertise")) {
            String tempCensor = "";
            String[] lines = censored.split("\n");
            for (String line : lines)
                tempCensor += bot.internalAdds(line) + "\n";
            censored = tempCensor.substring(0, tempCensor.lastIndexOf("\n"));
        }
        return censored;
    }
}