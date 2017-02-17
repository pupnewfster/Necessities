package gg.galaxygaming.necessities.Janet;

import gg.galaxygaming.necessities.Necessities;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.BookMeta;

public class JanetBooks {
    /**
     * Potentially censors the title being set for a book.
     * @param title The title to censor.
     * @param p     The player who is setting the title of the book.
     * @return The potentially censored title.
     */
    public String newTitle(String title, Player p) {
        YamlConfiguration config = Necessities.getInstance().getConfig();
        Janet bot = Necessities.getBot();
        String censored = title.trim();
        if (config.getBoolean("Necessities.language") && !p.hasPermission("Necessities.language"))
            censored = bot.internalLang(censored);
        if (config.getBoolean("Necessities.advertise") && !p.hasPermission("Necessities.advertise"))
            censored = bot.internalAdds(censored);
        return censored;
    }

    /**
     * Potentially censors the content of a book.
     * @param book The book to censor.
     * @param p    The player who wrote the book.
     * @return The potentially censored content of the book.
     */
    public BookMeta newMeta(BookMeta book, Player p) {
        for (int page = 0; page < book.getPageCount(); page++)
            book.setPage(page + 1, censor(book.getPage(page + 1), p));
        return book;
    }

    private String censor(String page, Player p) {
        YamlConfiguration config = Necessities.getInstance().getConfig();
        String censored = page;
        Janet bot = Necessities.getBot();
        if (config.getBoolean("Necessities.language") && !p.hasPermission("Necessities.language")) {
            String tempCensor;
            String[] lines = censored.split("\n");
            StringBuilder tempCensorBuilder = new StringBuilder();
            for (String line : lines)
                tempCensorBuilder.append(bot.internalLang(line)).append("\n");
            tempCensor = tempCensorBuilder.toString();
            censored = tempCensor.substring(0, tempCensor.lastIndexOf("\n"));
        }
        if (config.getBoolean("Necessities.advertise") && !p.hasPermission("Necessities.advertise")) {
            String tempCensor;
            String[] lines = censored.split("\n");
            StringBuilder tempCensorBuilder = new StringBuilder();
            for (String line : lines)
                tempCensorBuilder.append(bot.internalAdds(line)).append("\n");
            tempCensor = tempCensorBuilder.toString();
            censored = tempCensor.substring(0, tempCensor.lastIndexOf("\n"));
        }
        return censored;
    }
}