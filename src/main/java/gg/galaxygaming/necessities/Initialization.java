package gg.galaxygaming.necessities;

import gg.galaxygaming.necessities.Guilds.GuildManager;
import gg.galaxygaming.necessities.Hats.HatType;
import gg.galaxygaming.necessities.Material.Material;
import gg.galaxygaming.necessities.Material.MaterialHelper;
import gg.galaxygaming.necessities.RankManager.RankManager;
import java.io.File;
import java.util.Collections;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.libs.org.apache.commons.io.FileUtils;

class Initialization {

    void initiateFiles() {
        Utils.dirCreate("plugins/Necessities");
        Utils.dirCreate("plugins/Necessities/Logs");
        Utils.dirCreate("plugins/Necessities/Economy");
        Utils.dirCreate("plugins/Necessities/RankManager");
        Utils.dirCreate("plugins/Necessities/WorldManager");
        Utils.dirCreate("plugins/Necessities/Creative");
        Utils.dirCreate("plugins/Necessities/Guilds");
        Utils.dirCreate("plugins/Necessities/Backup");
        Utils.fileCreate("plugins/Necessities/motd.txt");
        Utils.fileCreate("plugins/Necessities/rules.txt");
        Utils.fileCreate("plugins/Necessities/faq.txt");
        Utils.fileCreate("plugins/Necessities/announcements.txt");
        File cWords = new File("plugins/Necessities", "customWords.txt");
        if (!cWords.exists()) {
            try {
                cWords.createNewFile();
                FileUtils.copyURLToFile(getClass().getResource("/customWords.txt"), cWords);
            } catch (Exception ignored) {
            }
        }
        createYaml();
        HatType.mapHats();
        Material
              .mapMaterials();//This is not in the Economy section because it is useful to have even without economy being enabled

        //RankManager
        RankManager rm = Necessities.getRM();
        rm.setRanks();
        rm.setSubranks();
        rm.readRanks();
        Necessities.getSBs().createScoreboard();

        YamlConfiguration config = Necessities.getInstance().getConfig();
        //WorldManager
        if (config.contains("Necessities.WorldManager") && config.getBoolean("Necessities.WorldManager")) {
            Necessities.getWM().initiate();
            Necessities.getWarps().initiate();
            Necessities.getPM().initiate();
        }

        Necessities.getBot().initiate();
        Necessities.getWrench().initiate();
        Necessities.getSpy().init();
        Necessities.getHide().init();
        Necessities.getWarns().initiate();

        //Guilds
        if (config.contains("Necessities.Guilds") && config.getBoolean("Necessities.Guilds")) {
            GuildManager gm = Necessities.getGM();
            gm.createFiles();
            gm.initiate();
            Necessities.getPower().initiate();
        }

        //Economy
        if (config.contains("Necessities.Economy") && config.getBoolean("Necessities.Economy")) {
            Necessities.getPrices().init();
            Necessities.getEconomy().init();
            Necessities.getRankPrices().initiate();
            Necessities.getCommandPrices().init();//Command prices are disabled anyways atm
        }

        Necessities.getRev().init();
        //Necessities.getNet().readCustom();
        Necessities.getSlack().init();
        Necessities.getAI().initiate();
        Necessities.getAnnouncer().init();

        Backup.tryBackup();

        File configFileStackSize = new File("plugins/Necessities", "stacksize.yml");
        if (configFileStackSize.exists()) {
            YamlConfiguration configStackSize = YamlConfiguration.loadConfiguration(configFileStackSize);
            for (String key : configStackSize.getKeys(false)) {
                MaterialHelper.setStackSize(Material.fromString(key), configStackSize.getInt(key));
            }
        }
    }

    private void createYaml() {
        Utils.addYML(new File("plugins/Necessities", "logoutmessages.yml"));
        Utils.addYML(new File("plugins/Necessities", "titles.yml"));
        Utils.addYML(new File("plugins/Necessities", "wrenched.yml"));
        Utils.addYML(new File("plugins/Necessities/Economy", "prices.yml"));
        Utils.addYML(new File("plugins/Necessities", "spying.yml"));
        Utils.addYML(new File("plugins/Necessities", "hiding.yml"));
        Utils.addYML(new File("plugins/Necessities/WorldManager", "warps.yml"));
        Utils.addYML(new File("plugins/Necessities", "loginmessages.yml"));
        Utils.addYML(new File("plugins/Necessities/RankManager", "users.yml"));
        Utils.addYML(new File("plugins/Necessities/WorldManager", "worlds.yml"));
        Utils.addYML(new File("plugins/Necessities/WorldManager", "portals.yml"));
        Utils.addYML(new File("plugins/Necessities/Creative", "reviews.yml"));
        File configFileStackSize = new File("plugins/Necessities", "stacksize.yml");
        if (!configFileStackSize.exists()) {
            try {
                configFileStackSize.createNewFile();
                YamlConfiguration config = YamlConfiguration.loadConfiguration(configFileStackSize);
                config.set("EGG", 64);
                config.set("MINECART", 64);
                config.set("HOPPERMINECART", 64);
                config.set("CHESTMINECART", 64);
                config.set("FURNACEMINECART", 64);
                config.set("SNOWBALL", 64);
                config.set("OAKSIGN", 64);
                config.set("SPRUCESIGN", 64);
                config.set("BIRCHSIGN", 64);
                config.set("JUNGLESIGN", 64);
                config.set("ACACIASIGN", 64);
                config.set("DARKOAKSIGN", 64);
                config.set("OAKBOAT", 64);
                config.set("SPRUCEBOAT", 64);
                config.set("BIRCHBOAT", 64);
                config.set("JUNGLEBOAT", 64);
                config.set("ACACIABOAT", 64);
                config.set("DARKOAKBOAT", 64);
                config.save(configFileStackSize);
            } catch (Exception ignored) {
            }
        }
        File configFileCensors = new File("plugins/Necessities", "censors.yml");
        if (!configFileCensors.exists()) {
            try {
                configFileCensors.createNewFile();
                YamlConfiguration config = YamlConfiguration.loadConfiguration(configFileCensors);
                config.set("badwords", Collections.singletonList(""));
                config.set("goodwords", Collections.singletonList(""));
                config.set("ips", Collections.singletonList(""));
                config.save(configFileCensors);
            } catch (Exception ignored) {
            }
        }
        if (!Necessities.getInstance().getConfigFile().exists()) {
            try {
                Necessities.getInstance().getConfigFile().createNewFile();
                YamlConfiguration config = Necessities.getInstance().getConfig();
                config.set("Necessities.WorldManager", true);
                config.set("Necessities.Guilds", true);
                config.set("Necessities.Economy", true);
                config.set("Necessities.Creative", false);
                config.set("Necessities.Paintball", false);
                config.set("Necessities.warns", 3);
                config.set("Necessities.caps", true);
                config.set("Necessities.language", true);
                config.set("Necessities.cmdSpam", true);
                config.set("Necessities.chatSpam", true);
                config.set("Necessities.advertise", true);
                config.set("Necessities.AI", false);
                config.set("Necessities.log", false);
                config.set("Necessities.customDeny", false);
                config.set("Necessities.ChatFormat", "{WORLD} {GUILD} {TITLE} {RANK} {NAME}: {MESSAGE}");
                config.set("Necessities.firstTime", "Welcome {NAME}!");
                config.set("Necessities.firstItems", Collections.singletonList(""));
                config.set("Necessities.DonationPass", "password");
                config.set("Necessities.DonationServer", 8);
                config.set("Necessities.SlackToken", "token");
                config.set("Necessities.WebHook", "webHook");
                config.set("Necessities.MaxSingleTypeEntities", 100);
                config.set("Necessities.sleepPercent", 0.33);
                config.set("Console.AliveStatus", "Alive");
                config.set("Announcements.frequency", 5);
                config.set("Economy.DBHost", "127.0.0.1:3306");
                config.set("Economy.DBName", "minecraft");
                config.set("Economy.DBUser", "user");
                config.set("Economy.DBPassword", "password");
                config.set("Economy.currencyType", 0);
                config.set("Economy.prefix", "");
                config.set("Economy.suffix", "");
                config.set("Economy.initialMoney", 0.0);
                config.save(Necessities.getInstance().getConfigFile());
            } catch (Exception ignored) {
            }
        } else {
            YamlConfiguration config = Necessities.getInstance().getConfig();
            if (!config.contains("Necessities.warns")) {
                config.set("Necessities.warns", 3);
            }
            if (!config.contains("Necessities.caps")) {
                config.set("Necessities.caps", true);
            }
            if (!config.contains("Necessities.language")) {
                config.set("Necessities.language", true);
            }
            if (!config.contains("Necessities.cmdSpam")) {
                config.set("Necessities.cmdSpam", true);
            }
            if (!config.contains("Necessities.chatSpam")) {
                config.set("Necessities.chatSpam", true);
            }
            if (!config.contains("Necessities.advertise")) {
                config.set("Necessities.advertise", true);
            }
            if (!config.contains("Necessities.ChatFormat")) {
                config.set("Necessities.ChatFormat", "{WORLD} {GUILD} {TITLE} {RANK} {NAME}: {MESSAGE}");
            }
            if (!config.contains("Necessities.firstTime")) {
                config.set("Necessities.firstTime", "Welcome {NAME}!");
            }
            if (!config.contains("Necessities.firstItems")) {
                config.set("Necessities.firstItems", Collections.singletonList(""));
            }
            if (!config.contains("Console.AliveStatus")) {
                config.set("Console.AliveStatus", "Alive");
            }
            if (!config.contains("Necessities.WorldManager")) {
                config.set("Necessities.WorldManager", true);
            }
            if (!config.contains("Necessities.Creative")) {
                config.set("Necessities.Creative", false);
            }
            if (!config.contains("Necessities.Paintball")) {
                config.set("Necessities.Paintball", false);
            }
            if (!config.contains("Necessities.Guilds")) {
                config.set("Necessities.Guilds", true);
            }
            if (!config.contains("Necessities.Economy")) {
                config.set("Necessities.Economy", true);
            }
            if (!config.contains("Necessities.AI")) {
                config.set("Necessities.AI", false);
            }
            if (!config.contains("Necessities.log")) {
                config.set("Necessities.log", false);
            }
            if (!config.contains("Necessities.customDeny")) {
                config.set("Necessities.customDeny", false);
            }
            if (!config.contains("Necessities.DonationPass")) {
                config.set("Necessities.DonationPass", "password");
            }
            if (!config.contains("Necessities.DonationServer")) {
                config.set("Necessities.DonationServer", 8);
            }
            if (!config.contains("Necessities.SlackToken")) {
                config.set("Necessities.SlackToken", "token");
            }
            if (!config.contains("Necessities.WebHook")) {
                config.set("Necessities.WebHook", "webHook");
            }
            if (!config.contains("Necessities.MaxSingleTypeEntities")) {
                config.set("Necessities.MaxSingleTypeEntities", 100);
            }
            if (!config.contains("Necessities.sleepPercent")) {
                config.set("Necessities.sleepPercent", 0.33);
            }
            if (!config.contains("Announcements.frequency")) {
                config.set("Announcements.frequency", 5);
            }
            if (!config.contains("Economy.DBHost")) {
                config.set("Economy.DBHost", "127.0.0.1:3306");
            }
            if (!config.contains("Economy.DBName")) {
                config.set("Economy.DBName", "minecraft");
            }
            if (!config.contains("Economy.DBUser")) {
                config.set("Economy.DBUser", "user");
            }
            if (!config.contains("Economy.DBPassword")) {
                config.set("Economy.DBPassword", "password");
            }
            if (!config.contains("Economy.currencyType")) {
                config.set("Economy.currencyType", 0);
            }
            if (!config.contains("Economy.prefix")) {
                config.set("Economy.prefix", "");
            }
            if (!config.contains("Economy.suffix")) {
                config.set("Economy.suffix", "");
            }
            if (!config.contains("Economy.initialMoney")) {
                config.set("Economy.initialMoney", 0.0);
            }
            try {
                config.save(Necessities.getInstance().getConfigFile());
            } catch (Exception ignored) {
            }
        }
    }
}