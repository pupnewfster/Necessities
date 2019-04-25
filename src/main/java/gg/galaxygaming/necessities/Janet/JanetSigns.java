package gg.galaxygaming.necessities.Janet;

import gg.galaxygaming.necessities.Necessities;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class JanetSigns {

    /**
     * Potentially censors the content on a sign.
     *
     * @param s The sign to censor.
     * @param p The player who made the sign.
     */
    public void censorSign(Sign s, Player p) {
        String line0 = "", line1 = "", line2 = "", line3 = "";
        if (s.getLine(0) != null) {
            line0 = s.getLine(0).trim();
        }
        if (s.getLine(1) != null) {
            line1 = s.getLine(1).trim();
        }
        if (s.getLine(2) != null) {
            line2 = s.getLine(2).trim();
        }
        if (s.getLine(3) != null) {
            line3 = s.getLine(3).trim();
        }
        YamlConfiguration config = Necessities.getInstance().getConfig();
        Janet bot = Necessities.getBot();
        if (config.getBoolean("Necessities.language") && !p.hasPermission("Necessities.language")) {
            line0 = bot.internalLang(line0);
            line1 = bot.internalLang(line1);
            line2 = bot.internalLang(line2);
            line3 = bot.internalLang(line3);
        }
        if (config.getBoolean("Necessities.advertise") && !p.hasPermission("Necessities.advertise")) {
            line0 = bot.internalAdds(line0);
            line1 = bot.internalAdds(line1);
            line2 = bot.internalAdds(line2);
            line3 = bot.internalAdds(line3);
        }
        s.setLine(0, line0);
        s.setLine(1, line1);
        s.setLine(2, line2);
        s.setLine(3, line3);
        s.update();
    }
}