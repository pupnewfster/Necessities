package com.crossge.necessities;

import com.crossge.necessities.Commands.CmdCommandSpy;
import com.crossge.necessities.Commands.CmdHide;
import com.crossge.necessities.Economy.*;
import com.crossge.necessities.Guilds.GuildManager;
import com.crossge.necessities.Guilds.PowerManager;
import com.crossge.necessities.Janet.Janet;
import com.crossge.necessities.Janet.JanetAI;
import com.crossge.necessities.Janet.JanetWarn;
import com.crossge.necessities.RankManager.RankManager;
import com.crossge.necessities.WorldManager.PortalManager;
import com.crossge.necessities.WorldManager.WarpManager;
import com.crossge.necessities.WorldManager.WorldManager;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.Arrays;

public class Initialization {
    private File configFileFriendlyNames = new File("plugins/Necessities/Economy", "friendlynames.yml");
    private File configFilePluralyNames = new File("plugins/Necessities/Economy", "pluralnames.yml");
    private File configFileWarps = new File("plugins/Necessities/WorldManager", "warps.yml");
    private File configFilePM = new File("plugins/Necessities/WorldManager", "portals.yml");
    private File configFileUsers = new File("plugins/Necessities/RankManager", "users.yml");
    private File configFileWM = new File("plugins/Necessities/WorldManager", "worlds.yml");
    private File configFilePrices = new File("plugins/Necessities/Economy", "prices.yml");
    private File configFileLogOut = new File("plugins/Necessities", "logoutmessages.yml");
    private File configFileLogIn = new File("plugins/Necessities", "loginmessages.yml");
    private File configFileWrench = new File("plugins/Necessities", "wrenched.yml");
    private File configFileIds = new File("plugins/Necessities/Economy", "ids.yml");
    private File configFileCensors = new File("plugins/Necessities", "censors.yml");
    private File configFileSpying = new File("plugins/Necessities", "spying.yml");
    private File configFileHiding = new File("plugins/Necessities", "hiding.yml");
    private File configFileTitles = new File("plugins/Necessities", "titles.yml");
    private File configFile = new File("plugins/Necessities", "config.yml");
    MaterialNames matNames = new MaterialNames();
    PowerManager power = new PowerManager();
    CmdCommandSpy cs = new CmdCommandSpy();
    PortalManager pm = new PortalManager();
    WarpManager warps = new WarpManager();
    WorldManager wm = new WorldManager();
    GuildManager gm = new GuildManager();
    ScoreBoards sb = new ScoreBoards();
    RankManager rm = new RankManager();
    JanetWarn warns = new JanetWarn();
    BalChecks balc = new BalChecks();
    CmdPrices cmdp = new CmdPrices();
    RankPrices rp = new RankPrices();
    Wrenched wrench = new Wrenched();
    Materials mat = new Materials();
    Console console = new Console();
    CmdHide hide = new CmdHide();
    GetUUID get = new GetUUID();
    JanetAI ai = new JanetAI();
    Prices pr = new Prices();
    Janet bot = new Janet();
    Ids ids = new Ids();

    public void initiateFiles() {
        dirCreate("plugins/Necessities");
        dirCreate("plugins/Necessities/Logs");
        dirCreate("plugins/Necessities/Economy");
        dirCreate("plugins/Necessities/RankManager");
        dirCreate("plugins/Necessities/Guilds");
        fileCreate("plugins/Necessities/motd.txt");
        fileCreate("plugins/Necessities/rules.txt");
        fileCreate("plugins/Necessities/faq.txt");
        createYaml();
        //RankManager
        rm.setRanks();
        rm.setSubranks();
        rm.readRanks();
        sb.createScoreboard();
        
        YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        //WorldManager
        if (config.contains("Necessities.WorldManager") && config.getBoolean("Necessities.WorldManager")) {
            wm.initiate();
            warps.initiate();
            pm.initiate();
        }

        console.initiate();
        get.initiate();
        ids.setIds();
        matNames.setIds();
        mat.readIds();
        bot.initiate();
        wrench.initiate();
        cs.init();
        hide.init();
        warns.initiate();
        ai.initiate();

        //Guilds
        if (config.contains("Necessities.Guilds") && config.getBoolean("Necessities.Guilds")) {
            gm.createFiles();
            gm.initiate();
            power.initiate();
        }

        //Economy
        if (config.contains("Necessities.Economy") && config.getBoolean("Necessities.Economy")) {
            pr.parseList();
            balc.updateB();
            rp.initiate();
            cmdp.upList();//Command prices are disabled anyways atm
        }
    }

    private void dirCreate(String directory) {
        File d = new File(directory);
        if (!d.exists())
            d.mkdir();
    }

    private void fileCreate(String file) {
        File f = new File(file);
        if (!f.exists())
            try {
                f.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    private void addYML(File file) {
        if (!file.exists())
            try {
                file.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    private void createYaml() {
        addYML(configFileFriendlyNames);
        addYML(configFilePluralyNames);
        addYML(configFileLogOut);
        addYML(configFileTitles);
        addYML(configFileWrench);
        addYML(configFilePrices);
        addYML(configFileSpying);
        addYML(configFileHiding);
        addYML(configFileWarps);
        addYML(configFileLogIn);
        addYML(configFileUsers);
        addYML(configFileIds);
        addYML(configFileWM);
        addYML(configFilePM);
        if (!configFileCensors.exists())
            try {
                configFileCensors.createNewFile();
                YamlConfiguration config = YamlConfiguration.loadConfiguration(configFileCensors);
                config.set("badwords", Arrays.asList(""));
                config.set("goodwords", Arrays.asList(""));
                config.set("ips", Arrays.asList(""));
                config.save(configFileCensors);
            } catch (Exception e) {
                e.printStackTrace();
            }
        if (!configFile.exists())
            try {
                configFile.createNewFile();
                YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);
                config.set("Necessities.WorldManager", true);
                config.set("Necessities.Guilds", true);
                config.set("Necessities.Economy", true);
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
                config.set("Necessities.firstItems", Arrays.asList(""));
                config.set("Console.AliveStatus", "Alive");
                config.save(configFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
        else {
            YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);
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
                config.set("Necessities.firstItems", Arrays.asList(""));
            if (!config.contains("Console.AliveStatus"))
                config.set("Console.AliveStatus", "Alive");
            if (!config.contains("Necessities.WorldManager"))
                config.set("Necessities.WorldManager", true);
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
            try {
                config.save(configFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}