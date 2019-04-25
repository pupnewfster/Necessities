package gg.galaxygaming.necessities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

public class Announcer {

    private BukkitRunnable announcerTask;
    private final ArrayList<String> messages = new ArrayList<>();
    private final Random r = new Random();

    void init() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "Loading Announcer messages...");
        try (BufferedReader read = new BufferedReader(
              new FileReader(new File("plugins/Necessities/announcements.txt")))) {
            String line;
            while ((line = read.readLine()) != null) {
                this.messages.add(ChatColor.translateAlternateColorCodes('&', line));
            }
        } catch (Exception ignored) {
        }
        if (this.messages.isEmpty()) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "No announcer messages loaded.");
            return;
        }
        this.announcerTask = new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.broadcastMessage(messages.get(r.nextInt(messages.size())));
            }
        };
        YamlConfiguration config = Necessities.getInstance().getConfig();
        this.announcerTask.runTaskTimerAsynchronously(Necessities.getInstance(), 0,
              20 * 60 * config.getInt("Announcements.frequency"));
        Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "Announcer messages loaded.");
    }

    void exit() {
        try {
            this.announcerTask.cancel();
        } catch (Exception ignored) { //No announcers
        }
    }

    public void reloadAnnouncer() {
        exit();
        this.messages.clear();
        init();
    }
}