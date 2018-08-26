package gg.galaxygaming.necessities;

import gg.galaxygaming.necessities.Economy.Material;
import net.minecraft.server.v1_12_R1.Item;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.json.simple.JsonArray;
import org.json.simple.JsonObject;
import org.json.simple.Jsoner;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.UUID;

public class Utils {
    /**
     * Checks if the given string is a valid double.
     * @param input The string to check.
     * @return True if the input is a valid double, false otherwise.
     */
    @SuppressWarnings({"BooleanMethodIsAlwaysInverted", "ResultOfMethodCallIgnored"})
    public static boolean legalDouble(String input) {
        try {
            Double.parseDouble(input);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * Checks if the given string is a valid integer.
     * @param input The string to check.
     * @return True if the input is a valid integer, false otherwise.
     */
    @SuppressWarnings({"BooleanMethodIsAlwaysInverted", "ResultOfMethodCallIgnored"})
    public static boolean legalInt(String input) {
        try {
            Integer.parseInt(input);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * Checks if the given string is a valid long.
     * @param input The string to check.
     * @return True if the input is a valid long, false otherwise.
     */
    @SuppressWarnings({"BooleanMethodIsAlwaysInverted", "ResultOfMethodCallIgnored"})
    public static boolean legalLong(String input) {
        try {
            Long.parseLong(input);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * Rounds the given double to a string with two decimals.
     * @param d The double to round.
     * @return The string representation rounded to two decimals of the given double.
     */
    public static String roundTwoDecimals(double d) {
        return new DecimalFormat("0.00").format(d);
    }

    /**
     * Adds commas to the specified string representation of a string.
     * @param s The string to add commas to.
     * @return The string with commas added.
     */
    public static String addCommas(String s) {
        return new DecimalFormat("#,##0.00").format(Double.parseDouble(s));
    }

    /**
     * Adds commas to the string representation of an integer.
     * @param i The integer to convert to a string and add commas.
     * @return The string of the integer with commas added.
     */
    public static String addCommas(int i) {
        return new DecimalFormat("#,###").format(i);
    }

    /**
     * Capitalizes the first letter of each word.
     * @param matName The text to capitalize the first letter of each word.
     * @return The given string with the first letter of each word capitalized.
     */
    public static String capFirst(String matName) {
        if (matName == null)
            return "";
        StringBuilder name = new StringBuilder();
        matName = matName.replaceAll("_", " ").toLowerCase();
        String[] namePieces = matName.split(" ");
        for (String piece : namePieces)
            name.append(uppercaseFirst(piece)).append(' ');
        return name.toString().trim();
    }

    private static String uppercaseFirst(String word) {
        if (word == null)
            return "";
        String firstCapitalized = "";
        if (word.length() > 0)
            firstCapitalized = word.substring(0, 1).toUpperCase();
        if (word.length() > 1)
            firstCapitalized += word.substring(1);
        return firstCapitalized;
    }

    /**
     * Adds an ' or 's to the end of a string.
     * @param name The string to add the apostrophe to.
     * @return The string with an ', or 's at the end.
     */
    public static String ownerShip(String name) {
        return name.endsWith("s") || name.endsWith("S") ? name + '\'' : name + "'s";
    }

    /**
     * Gets the uuid of an online player based on a partial name.
     * @param name The name to search for.
     * @return The uuid of an online player based on a partial name.
     */
    public static UUID getID(String name) {
        UUID partial = null;
        boolean startsWith = false;
        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            if (p.getName().equalsIgnoreCase(name))
                return p.getUniqueId();
            if (!startsWith && p.getName().toLowerCase().startsWith(name.toLowerCase())) {
                partial = p.getUniqueId();
                startsWith = true;
            }
            if (partial == null && (p.getName().toLowerCase().contains(name.toLowerCase()) || ChatColor.stripColor(p.getDisplayName()).toLowerCase().contains(name.toLowerCase())))
                partial = p.getUniqueId();
        }
        return partial;
    }

    /**
     * Gets the uuid of an offline player based on their name.
     * @param name The name to search for.
     * @return The uuid of an offline player based on their name.
     */
    public static UUID getOfflineID(String name) {
        if (name == null)
            return null;
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(new URL("https://api.mojang.com/users/profiles/minecraft/" + name).openConnection().getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null)
                response.append(inputLine);
            in.close();
            return UUID.fromString(((String) Jsoner.deserialize(response.toString(), new JsonObject()).get("id")).replaceAll("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})", "$1-$2-$3-$4-$5"));
        } catch (Exception ignored) {
        }
        return null;
    }

    /**
     * Gets a players name based on the string representation of their uuid.
     * @param message The string representation of a players uuid.
     * @return A players name based on the string representation of their uuid.
     */
    public static String nameFromString(String message) {
        if (message == null)
            return null;
        try {
            UUID uuid = UUID.fromString(message);
            OfflinePlayer p = Bukkit.getOfflinePlayer(uuid);
            if (p.hasPlayedBefore()) //If we have a player data for that uuid get their name  sort of as a cache
                return p.getName();
        } catch (Exception ignored) {
        }
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(new URL("https://api.mojang.com/user/profiles/" + message.replaceAll("-", "") + "/names").openConnection().getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null)
                response.append(inputLine);
            in.close();
            JsonArray json = Jsoner.deserialize(response.toString(), new JsonArray());
            return ((JsonObject) json.get(json.size() - 1)).getString(Jsoner.mintJsonKey("name", null));
        } catch (Exception ignored) {
        }
        return null;
    }

    /**
     * Gets the server's TPS.
     * @return The server's TPS.
     */
    public static String getTPS() {
        StringBuilder ticksBuilder = new StringBuilder(ChatColor.GOLD + "TPS from last 1m, 5m, 15m: ");
        for (double tps : Bukkit.getTPS())
            ticksBuilder.append(format(tps)).append(", ");
        String ticks = ticksBuilder.toString();
        return ticks.substring(0, ticks.length() - 2).trim();
    }

    private static String format(double tps) {
        return (tps > 18.0 ? ChatColor.GREEN : tps > 16.0 ? ChatColor.YELLOW : ChatColor.RED).toString() + (tps > 20.0 ? "*" : "") + Math.min(Math.round(tps * 100.0) / 100.0, 20.0);
    }

    /**
     * Changes the max stack size the given material.
     * @param material The material to change the stack size of.
     * @param size     The new max stack size for the material.
     */
    public static void setStackSize(Material material, int size) {
        Item item = Item.getById(material.getID());
        try {
            Field maxStackSize = Item.class.getDeclaredField("maxStackSize");
            maxStackSize.setAccessible(true);
            maxStackSize.setInt(item, size);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void dirCreate(String directory) {
        File d = new File(directory);
        if (!d.exists())
            d.mkdir();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void fileCreate(String file) {
        File f = new File(file);
        if (!f.exists())
            try {
                f.createNewFile();
            } catch (Exception ignored) {
            }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void addYML(File file) {
        if (!file.exists())
            try {
                file.createNewFile();
            } catch (Exception ignored) {
            }
    }
}