package com.crossge.necessities.Janet;

import java.io.File;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.BookMeta;

public class JanetBooks {
	private File configFile = new File("plugins/Necessities", "config.yml");
	Janet bot = new Janet();
	
	public String newTitle(String title, Player p) {
		YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);
		String censored = title.trim();
		if(config.getBoolean("Necessities.language") && !p.hasPermission("Necessities.language"))
			censored = bot.internalLang(censored);
		if(config.getBoolean("Necessities.advertise") && !p.hasPermission("Necessities.advertise"))
			censored = bot.internalAdds(censored);
		return censored;
	}
	
	public BookMeta newMeta(BookMeta book, Player p) {
		for(int page = 0; page < book.getPageCount(); page++)
			book.setPage(page + 1, censor(book.getPage(page + 1), p));
		return book;
	}
	
	private String censor(String page, Player p) {
		YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);
		String censored = page;
		if(config.getBoolean("Necessities.language") && !p.hasPermission("Necessities.language")) {
			String tempCensor = "";
			String[] lines = censored.split("\n");
			for(int i = 0; i < lines.length; i++)
				tempCensor += bot.internalLang(lines[i]) + "\n";
			censored = tempCensor.substring(0, tempCensor.lastIndexOf("\n"));
		}
		if(config.getBoolean("Necessities.advertise") && !p.hasPermission("Necessities.advertise")) {
			String tempCensor = "";
			String[] lines = censored.split("\n");
			for(int i = 0; i < lines.length; i++)
				tempCensor += bot.internalAdds(lines[i]) + "\n";
			censored = tempCensor.substring(0, tempCensor.lastIndexOf("\n"));
		}
		return censored;
	}
}