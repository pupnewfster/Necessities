//package gg.galaxygaming.necessities;

//import net.nyvaria.googleanalytics.hit.EventHit;
//import net.nyvaria.openanalytics.bukkit.client.Client;
//public class OpenAnalyticsHook {
    /*
     * Tracks an economy action.
     * @param uuid   The uuid of the player that had their balance change.
     * @param change The change in the players balance.
     */
    /*public static void trackEconomyAction(UUID uuid, double change) {
        EventHit hit = new EventHit(new Client(Bukkit.getOfflinePlayer(uuid)), "Economy", "Economy");
        hit.event_value = (int) change;//TODO Possibly multiply by 100 to not loose accuracy
        Necessities.trackAction(hit);
    }

    static void trackNewLogin(Player p) {
        EventHit hit = new EventHit(new Client(p), "NewLogin", "NewLogin");
        hit.event_label = p.getName();
        Necessities.trackAction(hit);
    }*/

    /*
     * Tracks a L2M occurrence.
     * @param p     The player who performed the L2M.
     * @param level The number of levels the player converted.
     */
    /*public static void trackLevelConvert(Player p, int level) {
        EventHit hit = new EventHit(new Client(p), "ConvertLevel", "ConvertLevel");
        hit.event_value = level;
        Necessities.trackAction(hit);
    }*/
//}