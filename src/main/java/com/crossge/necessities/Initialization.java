package com.crossge.necessities;

import com.crossge.necessities.Economy.Material;
import com.crossge.necessities.Guilds.GuildManager;
import com.crossge.necessities.Hats.HatType;
import com.crossge.necessities.RankManager.RankManager;
import org.apache.commons.io.FileUtils;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.Collections;

class Initialization {
    @SuppressWarnings("ResultOfMethodCallIgnored")
    void initiateFiles() {
        dirCreate("plugins/Necessities");
        dirCreate("plugins/Necessities/Logs");
        dirCreate("plugins/Necessities/Economy");
        dirCreate("plugins/Necessities/RankManager");
        dirCreate("plugins/Necessities/WorldManager");
        dirCreate("plugins/Necessities/Creative");
        dirCreate("plugins/Necessities/Guilds");
        fileCreate("plugins/Necessities/motd.txt");
        fileCreate("plugins/Necessities/rules.txt");
        fileCreate("plugins/Necessities/faq.txt");
        fileCreate("plugins/Necessities/announcements.txt");
        File cwords = new File("plugins/Necessities", "customWords.txt");
        if (!cwords.exists())
            try {
                cwords.createNewFile();
                FileUtils.copyURLToFile(getClass().getResource("/customWords.txt"), cwords);
            } catch (Exception ignored) {
            }
        createYaml();
        HatType.mapHats();
        Material.mapMaterials();

        //RankManager
        RankManager rm = Necessities.getInstance().getRM();
        rm.setRanks();
        rm.setSubranks();
        rm.readRanks();
        Necessities.getInstance().getSBs().createScoreboard();

        YamlConfiguration config = Necessities.getInstance().getConfig();
        //WorldManager
        if (config.contains("Necessities.WorldManager") && config.getBoolean("Necessities.WorldManager")) {
            Necessities.getInstance().getWM().initiate();
            Necessities.getInstance().getWarps().initiate();
            Necessities.getInstance().getPM().initiate();
        }

        Necessities.getInstance().getUUID().initiate();
        Necessities.getInstance().getBot().initiate();
        Necessities.getInstance().getWrench().initiate();
        Necessities.getInstance().getSpy().init();
        Necessities.getInstance().getHide().init();
        Necessities.getInstance().getWarns().initiate();

        //Guilds
        if (config.contains("Necessities.Guilds") && config.getBoolean("Necessities.Guilds")) {
            GuildManager gm = Necessities.getInstance().getGM();
            gm.createFiles();
            gm.initiate();
            Necessities.getInstance().getPower().initiate();
        }

        //Economy
        if (config.contains("Necessities.Economy") && config.getBoolean("Necessities.Economy")) {
            Necessities.getInstance().getPrices().parseList();
            Necessities.getInstance().getBalChecks().updateB();
            Necessities.getInstance().getRankPrices().initiate();
            Necessities.getInstance().getCommandPrices().upList();//Command prices are disabled anyways atm
        }

        Necessities.getInstance().getRev().parseList();
        Necessities.getInstance().getNet().readCustom();
        Necessities.getInstance().getSlack().init();
        Necessities.getInstance().getAI().initiate();
        Necessities.getInstance().getAnnouncer().init();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void dirCreate(String directory) {
        File d = new File(directory);
        if (!d.exists())
            d.mkdir();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void fileCreate(String file) {
        File f = new File(file);
        if (!f.exists())
            try {
                f.createNewFile();
            } catch (Exception ignored) {
            }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void addYML(File file) {
        if (!file.exists())
            try {
                file.createNewFile();
            } catch (Exception ignored) {
            }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void createYaml() {
        addYML(new File("plugins/Necessities/Economy", "friendlynames.yml"));
        addYML(new File("plugins/Necessities/Economy", "pluralnames.yml"));
        addYML(new File("plugins/Necessities", "logoutmessages.yml"));
        addYML(new File("plugins/Necessities", "titles.yml"));
        addYML(new File("plugins/Necessities", "wrenched.yml"));
        addYML(new File("plugins/Necessities/Economy", "prices.yml"));
        addYML(new File("plugins/Necessities", "spying.yml"));
        addYML(new File("plugins/Necessities", "hiding.yml"));
        addYML(new File("plugins/Necessities/WorldManager", "warps.yml"));
        addYML(new File("plugins/Necessities", "loginmessages.yml"));
        addYML(new File("plugins/Necessities/RankManager", "users.yml"));
        addYML(new File("plugins/Necessities/Economy", "ids.yml"));
        addYML(new File("plugins/Necessities/WorldManager", "worlds.yml"));
        addYML(new File("plugins/Necessities/WorldManager", "portals.yml"));
        addYML(new File("plugins/Necessities/Creative", "reviews.yml"));
        File configFileCensors = new File("plugins/Necessities", "censors.yml");
        if (!configFileCensors.exists())
            try {
                configFileCensors.createNewFile();
                YamlConfiguration config = YamlConfiguration.loadConfiguration(configFileCensors);
                config.set("badwords", Collections.singletonList(""));
                config.set("goodwords", Collections.singletonList(""));
                config.set("ips", Collections.singletonList(""));
                config.save(configFileCensors);
            } catch (Exception ignored) {
            }
        if (!Necessities.getInstance().getConfigFile().exists())
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
                config.set("Console.AliveStatus", "Alive");
                config.set("Announcements.frequency", 5);
                config.save(Necessities.getInstance().getConfigFile());
            } catch (Exception ignored) {
            }
        else {
            YamlConfiguration config = Necessities.getInstance().getConfig();
            if (!config.contains("Necessities.warns"))
                config.set("Necessities.warns", 3);
            if (!config.contains("Necessities.caps"))
                config.set("Necessities.caps", true);
            if (!config.contains("Necessities.language"))
                config.set("Necessities.language", true);
            if (!config.contains("Necessities.cmdSpam"))
                config.set("Necessities.cmdSpam", true);
            if (!config.contains("Necessities.chatSpam"))
                config.set("Necessities.chatSpam", true);
            if (!config.contains("Necessities.advertise"))
                config.set("Necessities.advertise", true);
            if (!config.contains("Necessities.ChatFormat"))
                config.set("Necessities.ChatFormat", "{WORLD} {GUILD} {TITLE} {RANK} {NAME}: {MESSAGE}");
            if (!config.contains("Necessities.firstTime"))
                config.set("Necessities.firstTime", "Welcome {NAME}!");
            if (!config.contains("Necessities.firstItems"))
                config.set("Necessities.firstItems", Collections.singletonList(""));
            if (!config.contains("Console.AliveStatus"))
                config.set("Console.AliveStatus", "Alive");
            if (!config.contains("Necessities.WorldManager"))
                config.set("Necessities.WorldManager", true);
            if (!config.contains("Necessities.Creative"))
                config.set("Necessities.Creative", false);
            if (!config.contains("Necessities.Paintball"))
                config.set("Necessities.Paintball", false);
            if (!config.contains("Necessities.Guilds"))
                config.set("Necessities.Guilds", true);
            if (!config.contains("Necessities.Economy"))
                config.set("Necessities.Economy", true);
            if (!config.contains("Necessities.AI"))
                config.set("Necessities.AI", false);
            if (!config.contains("Necessities.log"))
                config.set("Necessities.log", false);
            if (!config.contains("Necessities.customDeny"))
                config.set("Necessities.customDeny", false);
            if (!config.contains("Necessities.DonationPass"))
                config.set("Necessities.DonationPass", "password");
            if (!config.contains("Necessities.DonationServer"))
                config.set("Necessities.DonationServer", 8);
            if (!config.contains("Necessities.SlackToken"))
                config.set("Necessities.SlackToken", "token");
            if (!config.contains("Necessities.WebHook"))
                config.set("Necessities.WebHook", "webHook");
            if (!config.contains("Necessities.MaxSingleTypeEntities"))
                config.set("Necessities.MaxSingleTypeEntities", 100);
            if (!config.contains("Announcements.frequency"))
                config.set("Announcements.frequency", 5);
            try {
                config.save(Necessities.getInstance().getConfigFile());
            } catch (Exception ignored) {
            }
        }
    }
}