package gg.galaxygaming.necessities;

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.UUID;
import net.minecraft.server.v1_14_R1.IChatBaseComponent;
import net.minecraft.server.v1_14_R1.MinecraftServer;
import net.minecraft.server.v1_14_R1.PacketPlayOutTitle;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.v1_14_R1.CraftServer;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class Utils {

    //TODO: Paper
    //private static final Map<UUID, ProfileProperty> skins = new HashMap<>();

    /**
     * Checks if the given string is a valid double.
     *
     * @param input The string to check.
     * @return True if the input is a valid double, false otherwise.
     */
    @SuppressWarnings({"BooleanMethodIsAlwaysInverted"})
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
     *
     * @param input The string to check.
     * @return True if the input is a valid integer, false otherwise.
     */
    @SuppressWarnings({"BooleanMethodIsAlwaysInverted"})
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
     *
     * @param input The string to check.
     * @return True if the input is a valid long, false otherwise.
     */
    @SuppressWarnings({"BooleanMethodIsAlwaysInverted"})
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
     *
     * @param d The double to round.
     * @return The string representation rounded to two decimals of the given double.
     */
    public static String roundTwoDecimals(double d) {
        return new DecimalFormat("0.00").format(d);
    }

    /**
     * Adds commas to the specified string representation of a string.
     *
     * @param s The string to add commas to.
     * @return The string with commas added.
     */
    public static String addCommas(String s) {
        return new DecimalFormat("#,##0.00").format(Double.parseDouble(s));
    }

    /**
     * Adds commas to the string representation of an integer.
     *
     * @param i The integer to convert to a string and add commas.
     * @return The string of the integer with commas added.
     */
    public static String addCommas(int i) {
        return new DecimalFormat("#,###").format(i);
    }

    /**
     * Capitalizes the first letter of each word.
     *
     * @param matName The text to capitalize the first letter of each word.
     * @return The given string with the first letter of each word capitalized.
     */
    public static String capFirst(String matName) {
        if (matName == null) {
            return "";
        }
        StringBuilder name = new StringBuilder();
        matName = matName.replaceAll("_", " ").toLowerCase();
        String[] namePieces = matName.split(" ");
        for (String piece : namePieces) {
            name.append(uppercaseFirst(piece)).append(' ');
        }
        return name.toString().trim();
    }

    private static String uppercaseFirst(String word) {
        if (word == null) {
            return "";
        }
        String firstCapitalized = "";
        if (word.length() > 0) {
            firstCapitalized = word.substring(0, 1).toUpperCase();
        }
        if (word.length() > 1) {
            firstCapitalized += word.substring(1);
        }
        return firstCapitalized;
    }

    /**
     * Adds an ' or 's to the end of a string.
     *
     * @param name The string to add the apostrophe to.
     * @return The string with an ', or 's at the end.
     */
    public static String ownerShip(String name) {
        return name.endsWith("s") || name.endsWith("S") ? name + '\'' : name + "'s";
    }

    /**
     * Gets the uuid of an online player based on a partial name.
     *
     * @param name The name to search for.
     * @return The uuid of an online player based on a partial name.
     */
    public static UUID getID(String name) {
        UUID partial = null;
        boolean startsWith = false;
        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            if (p.getName().equalsIgnoreCase(name)) {
                return p.getUniqueId();
            }
            if (!startsWith && p.getName().toLowerCase().startsWith(name.toLowerCase())) {
                partial = p.getUniqueId();
                startsWith = true;
            }
            if (partial == null && (p.getName().toLowerCase().contains(name.toLowerCase()) || ChatColor
                  .stripColor(p.getDisplayName()).toLowerCase().contains(name.toLowerCase()))) {
                partial = p.getUniqueId();
            }
        }
        return partial;
    }

    /**
     * Gets the uuid of an offline player based on their name.
     *
     * @param name The name to search for.
     * @return The uuid of an offline player based on their name.
     */
    public static UUID getOfflineID(String name) {
        if (name == null) {
            return null;
        }
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(
                  new URL("https://api.mojang.com/users/profiles/minecraft/" + name).openConnection()
                        .getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return UUID.fromString(((String) Jsoner.deserialize(response.toString(), new JsonObject()).get("id"))
                  .replaceAll("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})", "$1-$2-$3-$4-$5"));
        } catch (Exception ignored) {
        }
        return null;
    }

    /**
     * Retrieves the head of the player given by the specified uuid and name.
     *
     * @param uuid The uuid of the player.
     * @param name The name of the player.
     * @return The head of the player with the corresponding uuid and name.
     * @throws PlayerNotFoundException When it failed to find the skin for the player.
     */
    public static ItemStack getPlayerHead(UUID uuid, String name) throws PlayerNotFoundException {
        //TODO: Paper
        /*ProfileProperty textures = getPlayerSkin(uuid);
        if (textures == null) {
            throw new PlayerNotFoundException();
        }
        PlayerProfile profile = Bukkit.createProfile(uuid, name);
        profile.setProperty(textures);*/
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta meta = (SkullMeta) skull.getItemMeta();
        //noinspection deprecation
        meta.setOwner(name);
        //TODO: Paper (Replace setOwner with setPlayerProfile
        //meta.setPlayerProfile(profile);
        skull.setItemMeta(meta);
        return skull;
    }

    /**
     * Gets a ProfileProperty representing the skin of the player with ths specified UUID.
     *
     * @param uuid The UUID of the player to get the skin of.
     * @return The ProfileProperty representing the skin of the player with the given UUID.
     */
    //TODO: Paper
    /*private static ProfileProperty getPlayerSkin(UUID uuid) {
        //TODO if the player is online grab it from their skin. If we end up supporting changing skins then also cache their old skin
        if (skins.containsKey(uuid)) {
            //TODO: Should clear things from the cache after x amount of time in case their skin changes
            return skins.get(uuid);
        }
        StringBuilder response = new StringBuilder();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(new URL(
              "https://sessionserver.mojang.com/session/minecraft/profile/" + uuid.toString().replaceAll("-", "")
                    + "?unsigned=false").openConnection().getInputStream()))) {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        JsonObject jo = (JsonObject) ((JsonArray) Jsoner.deserialize(response.toString(), new JsonObject())
              .get("properties")).get(0);
        ProfileProperty property = new ProfileProperty("textures", jo.getString(Jsoner.mintJsonKey("value", null)),
              jo.getString(Jsoner.mintJsonKey("signature", null)));
        skins.put(uuid, property);
        return property;
    }*/

    /**
     * Gets a players name based on the string representation of their uuid.
     *
     * @param message The string representation of a players uuid.
     * @return A players name based on the string representation of their uuid.
     */
    public static String nameFromString(String message) {
        if (message == null) {
            return null;
        }
        try {
            UUID uuid = UUID.fromString(message);
            OfflinePlayer p = Bukkit.getOfflinePlayer(uuid);
            if (p.hasPlayedBefore()) //If we have a player data for that uuid get their name  sort of as a cache
            {
                return p.getName();
            }
        } catch (Exception ignored) {
        }
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(
                  new URL("https://api.mojang.com/user/profiles/" + message.replaceAll("-", "") + "/names")
                        .openConnection().getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            JsonArray json = Jsoner.deserialize(response.toString(), new JsonArray());
            return ((JsonObject) json.get(json.size() - 1)).getString(Jsoner.mintJsonKey("name", null));
        } catch (Exception ignored) {
        }
        return null;
    }

    /**
     * Sends a message to a player's action bar.
     *
     * @param player The player to send the message to.
     * @param message The message to send.
     */
    public static void sendActionBarMessage(Player player, String message) {

        IChatBaseComponent infoJSON = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + message + "\"}");
        ((CraftPlayer) player).getHandle().playerConnection
              .sendPacket(new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.ACTIONBAR, infoJSON, 0, 60, 0));
        //TODO: Paper (Replaces above NMS version)
        //player.sendActionBar(message);
    }

    public static void wrenchCreeper(Creeper creeper) {
        //TODO: Paper
        //creeper.setIgnited(!creeper.isIgnited());
    }

    /**
     * Gets the server's TPS.
     *
     * @return The server's TPS.
     */
    public static String getTPS() {
        StringBuilder ticksBuilder = new StringBuilder(ChatColor.GOLD + "TPS from last 1m, 5m, 15m: ");
        //TODO: Paper (Replace getNMSRecentTps with Bukkit.getTPS)
        for (double tps : getNMSRecentTps()) {//Bukkit.getTPS()) {
            ticksBuilder.append(format(tps)).append(", ");
        }
        String ticks = ticksBuilder.toString();
        return ticks.substring(0, ticks.length() - 2).trim();
    }

    /**
     * Restart the server
     */
    public static void restartServer() {
        //TODO: Paper
        //Bukkit.spigot().restart();
    }

    private static String format(double tps) {
        return (tps > 18.0 ? ChatColor.GREEN : tps > 16.0 ? ChatColor.YELLOW : ChatColor.RED).toString() + (tps > 20.0
              ? "*" : "") + Math.min(Math.round(tps * 100.0) / 100.0, 20.0);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void dirCreate(String directory) {
        File d = new File(directory);
        if (!d.exists()) {
            d.mkdir();
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void fileCreate(String file) {
        File f = new File(file);
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (Exception ignored) {
            }
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void addYML(File file) {
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception ignored) {
            }
        }
    }

    //TODO: Paper (Start Delete)
    private static final Field recentTpsField = makeField();

    private static double[] getNMSRecentTps() {
        if (recentTpsField == null) {
            return new double[0];
        }
        return getField(((CraftServer) Bukkit.getServer()).getServer());
    }

    private static Field makeField() {
        try {
            return MinecraftServer.class.getDeclaredField("recentTps");
        } catch (Exception ex) {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> T getField(Object instance) {
        if (recentTpsField == null) {
            throw new RuntimeException("No such field");
        }
        recentTpsField.setAccessible(true);
        try {
            return (T) recentTpsField.get(instance);
        } catch (Exception ex) {
            return null;
        }
    }
    //TODO: Paper (End Delete)

    //TODO: Create a method to add items to a player inventory or just drop on ground if full
}