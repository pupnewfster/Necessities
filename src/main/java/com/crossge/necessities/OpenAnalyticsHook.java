package com.crossge.necessities;

import net.nyvaria.googleanalytics.hit.EventHit;
import net.nyvaria.openanalytics.bukkit.client.Client;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class OpenAnalyticsHook {
    public static void trackEconomyAction(UUID uuid, double change) {
        EventHit hit = new EventHit(new Client(Bukkit.getOfflinePlayer(uuid)), "Economy", "Economy");
        hit.event_value = (int) change;//TODO Possibly multiply by 100 to not loose accuracy
        Necessities.trackAction(hit);
    }

    static void trackNewLogin(Player p) {
        EventHit hit = new EventHit(new Client(p), "NewLogin", "NewLogin");
        hit.event_label = p.getName();
        Necessities.trackAction(hit);
    }

    public static void trackLevelConvert(Player p, int level) {
        EventHit hit = new EventHit(new Client(p), "ConvertLevel", "ConvertLevel");
        hit.event_value = level;
        Necessities.trackAction(hit);
    }
}