package gg.galaxygaming.necessities.Janet;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketFactory;
import gg.galaxygaming.necessities.Commands.CmdHide;
import gg.galaxygaming.necessities.Economy.Economy;
import gg.galaxygaming.necessities.Necessities;
import gg.galaxygaming.necessities.RankManager.Rank;
import gg.galaxygaming.necessities.RankManager.RankManager;
import gg.galaxygaming.necessities.RankManager.User;
import gg.galaxygaming.necessities.RankManager.UserManager;
import gg.galaxygaming.necessities.Utils;
import gg.galaxygaming.necessities.Variables;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.net.ssl.HttpsURLConnection;
import org.bukkit.BanEntry;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class JanetSlack {//TODO: Even though this is not currently being used it probably should be updated with the latest Slack API I made for Janet.
    private final Map<String, SlackUser> userMap = new HashMap<>();
    private final Map<Integer, List<String>> helpLists = new HashMap<>();
    private boolean isConnected;
    private String token;
    private URL hookURL;
    private WebSocket ws;

    /**
     * Initializes JanetSlack and connections to a slack channel.
     */
    public void init() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "Connecting to Slack...");
        YamlConfiguration config = Necessities.getInstance().getConfig();
        token = config.contains("Necessities.SlackToken") ? config.getString("Necessities.SlackToken") : "token";
        String hook = config.contains("Necessities.WebHook") ? config.getString("Necessities.WebHook") : "webHook";
        if (token.equals("token") || hook.equals("webHook")) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "Failed to connect to Slack.");
            return;
        }
        try {
            hookURL = new URL(hook);
        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "Failed to connect to Slack.");
            return;
        }
        setHelp();
        connect();
        Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "Connected to Slack.");
    }

    /**
     * Disconnects from slack.
     */
    public void disconnect() {
        if (!isConnected) {
            return;
        }
        userMap.clear();
        helpLists.clear();
        sendMessage("Disconnected.");
        sendPost("https://slack.com/api/users.setPresence?token=" + token + "&presence=away&pretty=1");
        isConnected = false;
        if (ws != null) {
            ws.disconnect();
        }
    }

    /**
     * Sends ingame chat to all the slack users monitoring ingame chat.
     *
     * @param message The message to send.
     */
    public void handleInGameChat(String message) {
        userMap.values().stream().filter(SlackUser::viewingChat).forEach(u -> u.sendPrivateMessage(message));
    }

    /**
     * Sends a message to slack.
     *
     * @param message The message to send.
     * @param isPM True if the message was a private message, false otherwise.
     * @param u The SlackUser who sent the pm if there was one.
     */
    public void sendMessage(String message, boolean isPM, SlackUser u) {
        if (isPM) {
            u.sendPrivateMessage(message);
        } else {
            sendMessage(message);
        }
    }

    /**
     * Sends a message to slack.
     *
     * @param message The message to send.
     */
    //sendPost("https://slack.com/api/chat.postMessage?token=" + token + "&channel=%23" + channel + "&text=" + ChatColor.stripColor(message.replaceAll(" ", "%20")) + "&as_user=true&pretty=1");
    public void sendMessage(String message) {
        message = ChatColor.stripColor(message);
        if (message.endsWith("\n")) {
            message = message.substring(0, message.length() - 1);
        }
        JsonObject json = new JsonObject();

        json.addProperty("text", message);
        try {
            HttpsURLConnection con = (HttpsURLConnection) hookURL.openConnection();
            con.setDoOutput(true);
            con.setRequestProperty("Content-Type", "application/json;");
            con.setRequestProperty("Accept", "application/json,text/plain");
            con.setRequestMethod("POST");
            OutputStream os = con.getOutputStream();
            os.write(new Gson().toJson(json).getBytes(StandardCharsets.UTF_8));
            os.close();
            InputStream is = con.getInputStream();
            is.close();
            con.disconnect();
        } catch (Exception ignored) {
        }
    }

    private SlackUser getUserInfo(String id) {
        if (userMap.containsKey(id)) {
            return userMap.get(id);
        }
        //Almost never should get past this point as it maps the users when it connects unless a new user gets invited
        try {
            URL url = new URL("https://slack.com/api/users.info?token=" + token + "&user=" + id + "&pretty=1");
            HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            userMap.put(id, new SlackUser(new Gson().fromJson(response.toString(), JsonObject.class)));
        } catch (Exception ignored) {
        }
        return userMap.get(id);
    }

    private void sendPost(String url) {
        try {
            URL obj = new URL(url);
            HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
            InputStream is = con.getInputStream();
            is.close();
            con.disconnect();
        } catch (Exception ignored) {
        }
    }

    private void connect() {
        if (isConnected) {
            return;
        }
        try {
            URL url = new URL("https://slack.com/api/rtm.connect?token=" + token);
            HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            JsonObject json = new Gson().fromJson(response.toString(), JsonObject.class);
            JsonElement webSocketUrl = json.get("url");
            if (webSocketUrl != null) {
                openWebSocket(webSocketUrl.getAsString());
            }
        } catch (Exception ignored) {
        }
        setUsers();
        setUserChannels();
        sendPost("https://slack.com/api/users.setPresence?token=" + this.token + "&presence=auto");
        isConnected = true;
        sendMessage("Connected.");
    }

    private void setUsers() {
        try {
            URL url = new URL("https://slack.com/api/users.list?token=" + token + "&presence=true");
            HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            JsonObject json = new Gson().fromJson(response.toString(), JsonObject.class);
            //Map users
            JsonArray users = json.getAsJsonArray("members");
            for (JsonElement u : users) {
                JsonObject user = u.getAsJsonObject();
                JsonElement deleted = user.get("deleted");
                if (deleted != null && deleted.getAsBoolean()) {
                    continue;
                }
                JsonElement jsonId = user.get("id");
                String id = jsonId == null ? null : jsonId.getAsString();
                if (!userMap.containsKey(id)) {
                    userMap.put(id, new SlackUser(user));
                }
            }
        } catch (Exception ignored) {
        }
    }

    private void openWebSocket(String url) {
        try {
            ws = new WebSocketFactory().createSocket(url).addListener(new WebSocketAdapter() {
                @Override
                public void onTextMessage(WebSocket websocket, String message) {
                    JsonObject json = new Gson().fromJson(message, JsonObject.class);
                    if (json.has("type")) {
                        JsonElement type = json.get("type");
                        if (type != null && type.getAsString().equals("message")) {
                            //TODO: Figure out if there is a way to get the user id of a bot instead of just using janet's
                            SlackUser info = json.has("bot_id") ? getUserInfo("U2Y19AVNJ")
                                  : getUserInfo(json.get("user").getAsString());
                            String text = json.get("text").getAsString();
                            while (text.contains("<") && text.contains(">")) {
                                text = text.split("<@")[0] + '@' + getUserInfo(text.split("<@")[1].split(">:")[0])
                                      .getName() + ':' + text.split("<@")[1].split(">:")[1];
                            }
                            String channel = json.get("channel").getAsString();
                            if (channel.startsWith("D")) //Direct Message
                            {
                                sendSlackChat(info, text, true);
                            } else if (channel.startsWith("C") || channel.startsWith("G")) //Channel or Group
                            {
                                sendSlackChat(info, text, false);
                            }
                        }
                    }
                }
            }).connect();
        } catch (Exception ignored) {
        }
    }

    private void setUserChannels() {
        try {
            URL url = new URL("https://slack.com/api/im.list?token=" + token);
            HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            JsonObject json = new Gson().fromJson(response.toString(), JsonObject.class);
            //Map user channels
            for (JsonElement i : json.getAsJsonArray("ims")) {
                JsonObject im = i.getAsJsonObject();
                JsonElement user = im.get("user");
                String userID = user == null ? null : im.getAsString();
                if (userMap.containsKey(userID)) {
                    JsonElement id = im.get("id");
                    if (id != null) {
                        userMap.get(userID).setChannel(id.getAsString());
                    }
                }
            }
        } catch (Exception ignored) {
        }
    }

    private String corTime(String time) {
        return time.length() == 1 ? '0' + time : time;
    }

    private String getLine(int page, int time, List<String> helpList) {
        page *= 10;
        if (helpList.size() < time + page + 1 || time == 10) {
            return null;
        }
        return helpList.get(page + time);
    }

    private void setHelp() {
        YamlConfiguration config = Necessities.getInstance().getConfig();
        List<String> temp = new ArrayList<>();
        temp.add("!help <page> ~ View the help messages on <page>.");
        temp.add("!rank ~ Shows you what rank you have.");
        temp.add("!bans ~ Shows you the banlist.");
        temp.add("!whois <name> ~ View information about <name>.");
        temp.add("!who ~ View the online players.");
        if (config.contains("Necessities.Economy") && config.getBoolean("Necessities.Economy")) {
            temp.add("!baltop <page> ~ View <page> of baltop.");
            temp.add("!bal <name> ~ View <name>'s balance.");
        }
        temp.add("!devs ~ View the devs.");
        temp.add("!warn <name> <reason> ~ Warn <name> for <reason>.");
        temp.add("!worlds ~ View the loaded worlds.");
        helpLists.put(0, new ArrayList<>(temp));//Member
        temp.add("!say <message> ~ Sends the message <message> to the players on the server.");
        temp.add("!kick <name> <reason> ~ Kicks <name> for an optional <reason>.");
        temp.add("!tempban <name> <time> <reason> ~ Tempbans <name> for <time> and an optional <reason>.");
        temp.add("!ban <name> <reason> ~ Bans <name> for an optional <reason>.");
        temp.add("!unban <name> ~ Unbans <name>.");
        temp.add("!banip <ip> <reason> ~ Bans <ip> for an optional <reason>.");
        temp.add("!unbanip <ip> ~ Unbans <ip>.");
        temp.add("!slap <name> ~ Slaps <name> sky high.");
        temp.add("!mute <name> ~ Mutes and unmutes <name>.");
        temp.add("!showchat ~ Toggles showing the in game chat. (Only available in private messages)");
        temp.add("!tps ~ Shows the in game ticks per second, and memory usage.");
        temp.add("!reload ~ Reloads the server.");
        temp.add("!restart ~ Restarts the server.");
        temp.add("!consolecmd <command> ~ Perform a command through the console.");
        helpLists.put(1, new ArrayList<>(temp));//Admin
        helpLists.put(2, new ArrayList<>(temp));//Owner
        helpLists.put(3, new ArrayList<>(temp));//Primary owner
        temp.clear();
    }

    private void sendSlackChat(SlackUser info, String message, boolean isPM) {
        if (!info.isMember()) {
            sendMessage("Error: You are restricted or ultra restricted", isPM, info);
            return;
        }
        if (info.isBot) //If bot don't send to game
        {
            return;
        }
        final String name = info.getName();
        Variables var = Necessities.getVar();
        if (message.startsWith("!")) {
            YamlConfiguration config = Necessities.getInstance().getConfig();
            Economy eco = Necessities.getEconomy();
            String m = "";
            if (message.startsWith("!help")) {
                int page = 0;
                if (message.split(" ").length > 1 && !Utils.legalInt(message.split(" ")[1])) {
                    sendMessage("Error: You must enter a valid help page.", isPM, info);
                    return;
                }
                if (message.split(" ").length > 1) {
                    page = Integer.parseInt(message.split(" ")[1]);
                }
                if (message.split(" ").length == 1 || page <= 0) {
                    page = 1;
                }
                int time = 0;
                int rounder = 0;
                List<String> helpList = helpLists.get(info.getRank());
                if (helpList.size() % 10 != 0) {
                    rounder = 1;
                }
                int totalpages = helpList.size() / 10 + rounder;
                if (page > totalpages) {
                    sendMessage("Error: Input a number from 1 to " + totalpages, isPM, info);
                    return;
                }
                m += " ---- Help -- Page " + page + '/' + totalpages + " ---- \n";
                page = page - 1;
                String msg = getLine(page, time, helpList);
                StringBuilder mBuilder = new StringBuilder(m);
                while (msg != null) {
                    mBuilder.append(msg).append('\n');
                    time++;
                    msg = getLine(page, time, helpList);
                }
                m = mBuilder.toString();
                if (page + 1 < totalpages) {
                    m += "Type !help " + (page + 2) + " to read the next page.\n";
                }
            } else if (message.startsWith("!rank")) {
                m += info.getRankName() + '\n';
            } else if (message.startsWith("!bans") || message.startsWith("!banlist") || message
                  .startsWith("!bannedplayers") || message.startsWith("!bannedips")) {
                BanList bans = Bukkit.getBanList(BanList.Type.NAME);
                m += "Banned players: ";
                StringBuilder mBuilder = new StringBuilder(m);
                for (BanEntry e : bans.getBanEntries()) {
                    mBuilder.append(e.getTarget()).append(", ");
                }
                m = mBuilder.toString();
                if (m.endsWith(", ")) {
                    m = m.substring(0, m.length() - 2).trim();
                }
                m += "\nBanned ips: ";
                bans = Bukkit.getBanList(BanList.Type.IP);
                StringBuilder mBuilder1 = new StringBuilder(m);
                for (BanEntry e : bans.getBanEntries()) {
                    mBuilder1.append(e.getTarget()).append(", ");
                }
                m = mBuilder1.toString();
                if (m.endsWith(", ")) {
                    m = m.substring(0, m.length() - 2).trim();
                }
            } else if (message.startsWith("!whois ")) {
                if (message.split(" ").length == 1) {
                    sendMessage("Error: You must enter a player to view info of.", isPM, info);
                    return;
                }
                String target = message.split(" ")[1];
                UUID uuid = Utils.getID(target);
                if (uuid == null) {
                    uuid = Utils.getOfflineID(target);
                    if (uuid == null) {
                        sendMessage(
                              "Error: That player has not joined the server. If the player is offline, please use the full and most recent name.",
                              isPM, info);
                        return;
                    }
                }
                User u = Necessities.getUM().getUser(uuid);
                m += "===== WhoIs: " + u.getName() + " =====\n";
                if (u.getPlayer() != null) {
                    m += " - Nick: " + u.getPlayer().getDisplayName() + '\n';
                } else {
                    m += (u.getNick() == null ? " - Name: " + u.getName() : " - Nick: " + u.getNick()) + '\n';
                    Calendar c = Calendar.getInstance();
                    c.setTimeInMillis(Bukkit.getOfflinePlayer(uuid).getLastPlayed());
                    String second = Integer.toString(c.get(Calendar.SECOND));
                    String minute = Integer.toString(c.get(Calendar.MINUTE));
                    String hour = Integer.toString(c.get(Calendar.HOUR_OF_DAY));
                    String day = Integer.toString(c.get(Calendar.DATE));
                    String month = Integer.toString(c.get(Calendar.MONTH) + 1);
                    String year = Integer.toString(c.get(Calendar.YEAR));
                    hour = corTime(hour);
                    minute = corTime(minute);
                    second = corTime(second);
                    m += " - Seen last on " + month + '/' + day + '/' + year + " at " + hour + ':' + minute + " and "
                          + second + ' ' + (Integer.parseInt(second) > 1 ? "seconds" : "second") + '\n';
                }
                m += " - Time played: " + u.getTimePlayed() + '\n';
                if (u.getRank()
                      != null) //Have had issues in past (probably with a corrupted player where rank was null)
                {
                    m += " - Rank: " + u.getRank().getName() + '\n';
                }
                String subranks = u.getSubranks();
                if (subranks != null) {
                    m += " - Subranks: " + subranks + '\n';
                }
                String permissions = u.getPermissions();
                if (permissions != null) {
                    m += " - Permission nodes: " + permissions + '\n';
                }
                if (u.getPlayer() != null) {
                    Player p = u.getPlayer();
                    m += " - Exp: " + Utils.addCommas(p.getTotalExperience()) + " (Level " + p.getLevel() + ")\n";
                    m += " - Location: (" + p.getWorld().getName() + ", " + p.getLocation().getBlockX() + ", " + p
                          .getLocation().getBlockY() + ", " + p.getLocation().getBlockZ() + ")\n";
                }
                if (config.contains("Necessities.Economy") && config.getBoolean("Necessities.Economy")) {
                    m += " - Money: " + Economy.format(eco.getBalance(uuid)) + '\n';
                }
                if (u.getPlayer() != null) {
                    Player p = u.getPlayer();
                    m += " - IP Address: " + p.getAddress().toString().split("/")[1].split(":")[0] + '\n';
                    String gamemode = "Survival";
                    if (p.getGameMode().equals(GameMode.ADVENTURE)) {
                        gamemode = "Adventure";
                    } else if (p.getGameMode().equals(GameMode.CREATIVE)) {
                        gamemode = "Creative";
                    } else if (p.getGameMode().equals(GameMode.SPECTATOR)) {
                        gamemode = "Spectator";
                    }
                    m += " - Gamemode: " + gamemode + '\n';
                    m += " - Banned: " + (p.isBanned() ? "true" : "false") + '\n';
                    m += " - Visible: " + (Necessities.getHide().isHidden(p) ? "false" : "true") + '\n';
                } else {
                    m += " - Banned: " + (Bukkit.getOfflinePlayer(u.getUUID()).isBanned() ? "true" : "false") + '\n';
                }
            } else if (message.startsWith("!who") || message.startsWith("!players")) {
                RankManager rm = Necessities.getRM();
                int numbOnline = Bukkit.getOnlinePlayers().size() + 1;
                Map<Rank, String> online = new HashMap<>();
                if (!rm.getOrder().isEmpty()) {
                    online.put(rm.getRank(rm.getOrder().size() - 1),
                          rm.getRank(rm.getOrder().size() - 1).getColor() + "Janet, ");
                }
                UserManager um = Necessities.getUM();
                if (!um.getUsers().isEmpty()) {
                    CmdHide hide = Necessities.getHide();
                    for (User u : um.getUsers().values()) {
                        if (u.isAfk() && hide.isHidden(u.getPlayer())) {
                            online.put(u.getRank(),
                                  online.containsKey(u.getRank()) ? online.get(u.getRank()) + "[AFK][HIDDEN]" + u
                                        .getPlayer().getDisplayName() + ", " : "[AFK][HIDDEN]" +
                                        u.getPlayer().getDisplayName() + ", ");
                        } else if (u.isAfk()) {
                            online.put(u.getRank(),
                                  online.containsKey(u.getRank()) ? online.get(u.getRank()) + "[AFK]" + u.getPlayer()
                                        .getDisplayName() + ", " : "[AFK]" + u.getPlayer().getDisplayName() + ", ");
                        } else if (hide.isHidden(u.getPlayer())) {
                            online.put(u.getRank(),
                                  online.containsKey(u.getRank()) ? online.get(u.getRank()) + "[HIDDEN]" + u.getPlayer()
                                        .getDisplayName() + ", " : "[HIDDEN]" + u.getPlayer().getDisplayName() + ", ");
                        } else {
                            online.put(u.getRank(),
                                  online.containsKey(u.getRank()) ? online.get(u.getRank()) + u.getPlayer()
                                        .getDisplayName() + ", " : u.getPlayer().getDisplayName() + ", ");
                        }
                    }
                }
                m += "There " + (numbOnline == 1 ? "is " : "are ") + numbOnline + " out of a maximum " + Bukkit
                      .getMaxPlayers() + " players online.\n";
                StringBuilder mBuilder = new StringBuilder(m);
                for (int i = rm.getOrder().size() - 1; i >= 0; i--) {
                    Rank r = rm.getRank(i);
                    if (online.containsKey(r)) {
                        mBuilder.append(r.getName()).append("s: ")
                              .append(online.get(r).trim(), 0, online.get(r).length() - 2).append('\n');
                    }
                }
                m = mBuilder.toString();
            } else if (message.startsWith("!devs")) {
                StringBuilder d = new StringBuilder("The Devs for Necessities are: ");
                List<Necessities.DevInfo> devs = Necessities.getInstance().getDevs();
                for (int i = 0; i < devs.size(); i++) {
                    d.append(
                          i + 1 >= devs.size() ? "and " + devs.get(i).getName() + '.' : devs.get(i).getName() + ", ");
                }
                m += d + "\n";
            } else if (config.contains("Necessities.Economy") && config.getBoolean("Necessities.Economy") && (
                  message.startsWith("!baltop") || message.startsWith("!moneytop") || message
                        .startsWith("!balancetop"))) {
                int page = 0;
                if (message.split(" ").length > 1) {
                    if (!Utils.legalInt(message.split(" ")[1])) {
                        sendMessage("Error: You must enter a valid baltop page.", isPM, info);
                        return;
                    }
                    page = Integer.parseInt(message.split("!baltop ")[1]);
                }
                if (message.split(" ").length <= 1 || page == 0) {
                    page = 1;
                }
                int time = 0;
                int totalpages = eco.baltopPages();
                if (page > totalpages) {
                    sendMessage("Error: Input a number from 1 to " + totalpages, isPM, info);
                    return;
                }
                m += "Balance Top Page [" + page + '/' + totalpages + "]\n";
                List<String> balTop = eco.getBalTop(page);
                page -= 1;
                StringBuilder mBuilder = new StringBuilder(m);
                for (String bal : balTop) {
                    mBuilder.append((page * 10 + time++ + 1)).append(". ")
                          .append(Utils.nameFromString(bal.split(" ")[0])).append(" has: ")
                          .append(Economy.format(Double.parseDouble(bal.split(" ")[1]))).append('\n');
                }
                m = mBuilder.toString();
            } else if (config.contains("Necessities.Economy") && config.getBoolean("Necessities.Economy") && (
                  message.startsWith("!bal ") || message.startsWith("!balance ") || message.startsWith("!money "))) {
                if (message.split(" ").length == 1) {
                    sendMessage("Error: You must enter a player to view info of.", isPM, info);
                    return;
                }
                String target = message.split(" ")[1];
                String playersname;
                UUID uuid = Utils.getID(target);
                if (uuid == null) {
                    uuid = Utils.getOfflineID(target);
                    if (uuid == null) {
                        sendMessage(
                              "Error: That player has not joined the server. If the player is offline, please use the full and most recent name.",
                              isPM, info);
                        return;
                    }
                    playersname = Bukkit.getOfflinePlayer(uuid).getName();
                } else {
                    playersname = Bukkit.getPlayer(uuid).getName();
                }
                double balance = eco.getBalance(uuid);
                if (balance == -1.0) {
                    sendMessage(
                          "Error: That player is not in my records. If the player is offline, please use the full and most recent name.",
                          isPM, info);
                    return;
                }
                m += (playersname.endsWith("s") || playersname.endsWith("S") ? playersname + '\'' : playersname + "'s")
                      + " balance is: " + Economy.format(balance) + '\n';
            } else if (message.startsWith("!warn ")) {
                message = message.replaceFirst("!warn ", "");
                if (message.split(" ").length < 2) {
                    sendMessage("Error: You must enter a player to warn and a reason.", isPM, info);
                    return;
                }
                UUID uuid = Utils.getID(message.split(" ")[0]);
                if (uuid == null) {
                    sendMessage("Error: Invalid player.", isPM, info);
                    return;
                }
                final Player target = Bukkit.getPlayer(uuid);
                if (target.hasPermission("Necessities.antiPWarn")) {
                    sendMessage("Error: You may not warn someone who has Necessities.antiPWarn.", isPM, info);
                    return;
                }
                final String reason = message.replaceFirst(message.split(" ")[0], "").trim();
                Bukkit.getScheduler().scheduleSyncDelayedTask(Necessities.getInstance(),
                      () -> Necessities.getWarns().warn(target.getUniqueId(), reason, name));
                m += target.getName() + " was warned by " + name + " for " + reason + ".\n";
            } else if (message.startsWith("!worlds")) {
                List<String> worlds = Bukkit.getWorlds().stream().map(World::getName)
                      .collect(Collectors.toCollection(ArrayList::new));
                StringBuilder levelsBuilder = new StringBuilder();
                for (int i = 0; i < worlds.size() - 1; i++) {
                    levelsBuilder.append(worlds.get(i)).append(", ");
                }
                if (worlds.size() > 1) {
                    levelsBuilder.append("and ");
                }
                levelsBuilder.append(worlds.get(worlds.size() - 1)).append('.');
                m += "The worlds are: " + levelsBuilder.toString() + '\n';
            } else if (message.startsWith("!say ") && info.isAdmin()) {
                Bukkit.broadcastMessage(ChatColor.WHITE + name + ": " + message.replaceFirst("!say ", ""));
                return;
            } else if (message.startsWith("!kick ") && info.isAdmin()) {
                message = message.replaceFirst("!kick ", "");
                if (message.split(" ").length == 0) {
                    sendMessage("Error: You must enter a player to kick and a reason.", isPM, info);
                    return;
                }
                UUID uuid = Utils.getID(message.split(" ")[0]);
                if (uuid == null) {
                    sendMessage("Error: Invalid player.", isPM, info);
                    return;
                }
                final Player target = Bukkit.getPlayer(uuid);
                final String reason = ChatColor
                      .translateAlternateColorCodes('&', message.replaceFirst(message.split(" ")[0], "").trim());
                Bukkit.broadcastMessage(
                      var.getMessages() + name + " kicked " + var.getObj() + target.getName() + (reason.equals("") ? ""
                            : var.getMessages() + " for " + var.getObj() + reason));
                m += name + " kicked " + target.getName() + (reason.equals("") ? "" : " for " + reason) + '\n';
                Bukkit.getScheduler()
                      .scheduleSyncDelayedTask(Necessities.getInstance(), () -> target.kickPlayer(reason));
            } else if (message.startsWith("!ban ") && info.isAdmin()) {
                message = message.replaceFirst("!ban ", "");
                if (message.split(" ").length == 0) {
                    sendMessage("Error: You must enter a player to ban.", isPM, info);
                    return;
                }
                UUID uuid = Utils.getID(message.split(" ")[0]);
                if (uuid == null) {
                    uuid = Utils.getOfflineID(message.split(" ")[0]);
                    if (uuid == null || !Bukkit.getOfflinePlayer(uuid).hasPlayedBefore()) {
                        sendMessage(
                              "Error: That player does not exist or has not joined the server. If the player is offline, please use the full and most recent name.",
                              isPM, info);
                        return;
                    }
                }
                final OfflinePlayer target = Bukkit.getOfflinePlayer(uuid);
                if (target.getPlayer() != null && target.getPlayer().hasPermission("Necessities.antiBan") && !info
                      .isOwner()) {
                    sendMessage("Error: You may not ban someone who has Necessities.antiBan.", isPM, info);
                    return;
                }
                final String reason =
                      message.split(" ").length > 1 ? message.replaceFirst(message.split(" ")[0], "").trim() : "";
                String theirName = target.getName();
                BanList bans = Bukkit.getBanList(BanList.Type.NAME);
                if (target.getPlayer() != null) {
                    Bukkit.getScheduler().scheduleSyncDelayedTask(Necessities.getInstance(),
                          () -> target.getPlayer().kickPlayer(reason));
                }
                bans.addBan(theirName, reason, null, "Slack_" + name);
                Bukkit.broadcastMessage(
                      var.getMessages() + name + " banned " + var.getObj() + theirName + var.getMessages() + (
                            reason.equals("") ? "." : " for " + var.getObj() + reason + var.getMessages() + '.'));
                m += name + " banned " + theirName + (reason.equals("") ? "." : " for " + reason + '.') + '\n';
            } else if (message.startsWith("!unban ") && info.isAdmin()) {
                message = message.replaceFirst("!unban ", "");
                if (message.split(" ").length == 0) {
                    sendMessage("Error: You must enter a player to unban.", isPM, info);
                    return;
                }
                UUID uuid = Utils.getID(message.split(" ")[0]);
                if (uuid == null) {
                    uuid = Utils.getOfflineID(message.split(" ")[0]);
                    if (uuid == null || !Bukkit.getOfflinePlayer(uuid).hasPlayedBefore()) {
                        sendMessage(
                              "Error: That player does not exist or has not joined the server. If the player is offline, please use the full and most recent name.",
                              isPM, info);
                        return;
                    }
                }
                OfflinePlayer target = Bukkit.getOfflinePlayer(uuid);
                BanList bans = Bukkit.getBanList(BanList.Type.NAME);
                String theirName = target.getName();
                if (!bans.isBanned(theirName)) {
                    sendMessage("Error: That player is not banned.", isPM, info);
                    return;
                }
                bans.pardon(theirName);
                Bukkit.broadcastMessage(var.getMessages() + name + " unbanned " + theirName + '.');
                m += name + " unbanned " + theirName + ".\n";
            } else if (message.startsWith("!mute ") && info.isAdmin()) {
                message = message.replaceFirst("!mute ", "");
                if (message.split(" ").length == 0) {
                    sendMessage("Error: You must enter a player to mute.", isPM, info);
                    return;
                }
                UUID uuid = Utils.getID(message.split(" ")[0]);
                if (uuid == null) {
                    sendMessage("Error: Invalid player.", isPM, info);
                    return;
                }
                User u = Necessities.getUM().getUser(uuid);
                Bukkit.broadcastMessage(
                      var.getObj() + name + var.getMessages() + (!u.isMuted() ? " muted " : " unmuted ") + var.getObj()
                            + u.getPlayer().getDisplayName() + var.getMessages() + '.');
                u.getPlayer().sendMessage(
                      var.getDemote() + "You have been " + var.getObj() + (!u.isMuted() ? "muted" : "unmuted") + var
                            .getMessages() + '.');
                m += name + (!u.isMuted() ? " muted " : " unmuted ") + u.getPlayer().getDisplayName() + ".\n";
                u.setMuted(!u.isMuted());
            } else if (message.startsWith("!slap ") && info.isAdmin()) {
                message = message.replaceFirst("!slap ", "");
                if (message.split(" ").length == 0) {
                    sendMessage("Error: You must enter a player to slap.", isPM, info);
                    return;
                }
                UUID uuid = Utils.getID(message.split(" ")[0]);
                if (uuid == null) {
                    sendMessage("Error: Invalid player.", isPM, info);
                    return;
                }
                Player target = Bukkit.getPlayer(uuid);
                Location loc = target.getLocation().clone().add(0, 2500, 0);
                target.teleport(loc);
                Bukkit.broadcastMessage(var.getMessages() + target.getName() + " was slapped sky high by " + name);
                m += target.getName() + " was slapped sky high by " + name + '\n';
            } else if (message.startsWith("!tempban ") && info.isAdmin()) {
                message = message.replaceFirst("!tempban ", "");
                if (message.split(" ").length < 2) {
                    sendMessage("Error: You must enter a player to ban and a duration in minutes.", isPM, info);
                    return;
                }
                UUID uuid = Utils.getID(message.split(" ")[0]);
                if (uuid == null) {
                    uuid = Utils.getOfflineID(message.split(" ")[0]);
                    if (uuid == null || !Bukkit.getOfflinePlayer(uuid).hasPlayedBefore()) {
                        sendMessage(
                              "Error: That player does not exist or has not joined the server. If the player is offline, please use the full and most recent name.",
                              isPM, info);
                        return;
                    }
                }
                final OfflinePlayer target = Bukkit.getOfflinePlayer(uuid);
                if (target.getPlayer() != null && target.getPlayer().hasPermission("Necessities.antiBan") && !info
                      .isOwner()) {
                    sendMessage("Error: You may not ban someone who has Necessities.antiBan.", isPM, info);
                    return;
                }
                int minutes;
                try {
                    minutes = Integer.parseInt(message.split(" ")[1]);
                } catch (Exception e) {
                    sendMessage("Error: Invalid time, please enter a time in minutes.", isPM, info);
                    return;
                }
                if (minutes < 0) {
                    sendMessage("Error: Invalid time, please enter a time in minutes.", isPM, info);
                    return;
                }
                final String reason =
                      message.split(" ").length > 2 ? message.replaceFirst(message.split(" ")[0], "").trim() : "";
                BanList bans = Bukkit.getBanList(BanList.Type.NAME);
                String theirName = target.getName();
                if (target.getPlayer() != null) {
                    Bukkit.getScheduler().scheduleSyncDelayedTask(Necessities.getInstance(),
                          () -> target.getPlayer().kickPlayer(reason));
                }
                Date date = new Date(System.currentTimeMillis() + minutes * 60 * 1000);
                bans.addBan(theirName, reason, date, "Slack_" + name);
                Bukkit.broadcastMessage(
                      var.getMessages() + name + " banned " + var.getObj() + theirName + var.getMessages() + " for "
                            + var.getObj() + minutes + var.getMessages() +
                            ' ' + (minutes == 1 ? "minute" : "minutes") + (reason.equals("") ? "."
                            : " for the reason " + var.getObj() + reason + var.getMessages() + '.'));
                m += name + " banned " + theirName + " for " + minutes + ' ' + (minutes == 1 ? "minute" : "minutes") + (
                      reason.equals("") ? "." : " for the reason " + reason + '.') + '\n';
            } else if (message.startsWith("!banip ") && info.isAdmin()) {
                message = message.replaceFirst("!banip ", "");
                if (message.split(" ").length == 0) {
                    sendMessage("Error: You must enter an ip to ban.", isPM, info);
                    return;
                }
                UUID uuid = Utils.getID(message.split(" ")[0]);
                if (uuid != null) {
                    final Player target = Bukkit.getPlayer(uuid);
                    if (target.hasPermission("Necessities.antiBan") && !info.isOwner()) {
                        sendMessage("Error: You may not ban someone who has Necessities.antiBan.", isPM, info);
                        return;
                    }
                    final String reason =
                          message.split(" ").length > 1 ? message.replaceFirst(message.split(" ")[0], "").trim() : "";
                    String theirName = target.getName();
                    BanList bans = Bukkit.getBanList(BanList.Type.IP);
                    String theirIP = target.getAddress().toString().split("/")[1].split(":")[0];
                    Bukkit.getScheduler().scheduleSyncDelayedTask(Necessities.getInstance(),
                          () -> target.getPlayer().kickPlayer(reason));
                    bans.addBan(theirIP, reason, null, "Slack_" + name);
                    Bukkit.broadcastMessage(
                          var.getMessages() + name + " banned " + var.getObj() + theirName + var.getMessages() + (
                                reason.equals("") ? "." : " for " + var.getObj() + reason + var.getMessages() + '.'));
                    m += name + " banned " + theirName + (reason.equals("") ? "." : " for " + reason + '.') + '\n';
                } else {
                    boolean validIp = false;
                    try {
                        Pattern ipAdd = Pattern.compile(
                              "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
                                    + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");
                        validIp = ipAdd.matcher(message.split(" ")[0]).matches();
                    } catch (Exception ignored) {
                    }
                    if (!validIp) {
                        sendMessage("Error: Invalid ip.", isPM, info);
                        return;
                    }
                    final String reason =
                          message.split(" ").length > 1 ? message.replaceFirst(message.split(" ")[0], "").trim() : "";
                    BanList bans = Bukkit.getBanList(BanList.Type.IP);
                    String theirIP = message.split(" ")[0];
                    Bukkit.getOnlinePlayers().stream()
                          .filter(t -> t.getAddress().toString().split("/")[1].split(":")[0].equals(theirIP))
                          .findFirst().ifPresent(t -> Bukkit.getScheduler()
                          .scheduleSyncDelayedTask(Necessities.getInstance(), () -> t.getPlayer().kickPlayer(reason)));
                    bans.addBan(theirIP, reason, null, "Console");
                    Bukkit.broadcastMessage(
                          var.getMessages() + name + " banned " + var.getObj() + theirIP + var.getMessages() + (
                                reason.equals("") ? "." : " for " + var.getObj() + reason + var.getMessages() + '.'));
                    m += name + " banned " + theirIP + (reason.equals("") ? "." : " for " + reason + '.') + '\n';
                }
            } else if (message.startsWith("!unbanip ") && info.isAdmin()) {
                message = message.replaceFirst("!unbanip ", "");
                if (message.split(" ").length == 0) {
                    sendMessage("Error: You must enter an ip to unban.", isPM, info);
                    return;
                }
                boolean validIp = false;
                try {
                    Pattern ipAdd = Pattern.compile(
                          "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
                                + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");
                    validIp = ipAdd.matcher(message.split(" ")[0]).matches();
                } catch (Exception ignored) {
                }
                if (!validIp) {
                    sendMessage("Error: Invalid ip.", isPM, info);
                    return;
                }
                BanList bans = Bukkit.getBanList(BanList.Type.IP);
                String theirIP = message.split(" ")[0];
                if (!bans.isBanned(theirIP)) {
                    sendMessage("Error: That ip is not banned.", isPM, info);
                    return;
                }
                bans.pardon(theirIP);
                Bukkit.broadcastMessage(var.getMessages() + name + " unbanned " + theirIP + '.');
                m += name + " unbanned " + theirIP + ".\n";
            } else if (message.startsWith("!tps") && info.isAdmin()) {
                m += Utils.getTPS() + '\n';
                int mb = 1024 * 1024;
                Runtime runtime = Runtime.getRuntime();
                m += "Used Memory: " + (runtime.totalMemory() - runtime.freeMemory()) / mb + " mb / "
                      + runtime.maxMemory() / mb + " mb\n";
                StringBuilder mBuilder = new StringBuilder(m);
                for (World w : Bukkit.getWorlds()) {
                    mBuilder.append("World: ").append(w.getName()).append(", Entities Loaded: ")
                          .append(w.getEntities().size()).append(", Chunks Loaded: ").append(w.getLoadedChunks().length)
                          .append('\n');
                }
                m = mBuilder.toString();
            } else if (message.startsWith("!reload") && info.isAdmin()) {
                sendMessage("Reloading...", isPM, info);
                Bukkit.getScheduler().scheduleSyncDelayedTask(Necessities.getInstance(), Bukkit::reload);
                return;
            } else if (message.startsWith("!restart") && info.isAdmin()) {
                sendMessage("Restarting...", isPM, info);
                Utils.restartServer();
                return;
            } else if ((message.startsWith("!consolecmd") || message.startsWith("!ccmd") || message
                  .startsWith("!consolecommand")) && info.isAdmin()) {//TODO Make it show the result to slack
                final String command = message.replaceFirst(message.split(" ")[0], "").trim();
                Bukkit.getScheduler().scheduleSyncDelayedTask(Necessities.getInstance(),
                      () -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command));
                return;
            } else if ((message.startsWith("!showchat") || message.startsWith("!togglechat") || message
                  .startsWith("!showingamechat") || message.startsWith("!ingamechat")) && info.isAdmin()) {
                if (!isPM) {
                    m += "Error: You must pm me to be able to view in game chat.\n";
                } else {
                    m += (info.viewingChat() ? "No longer" : "You are now") + " viewing the in game chat.\n";
                    info.toggleViewingChat();
                }
            } else if (!isPM) {
                Bukkit.broadcast(var.getMessages() + "From Slack - " + ChatColor.WHITE + name + ": " + message,
                      "Necessities.slack");
                return;
            }
            sendMessage(ChatColor.stripColor(m), isPM, info);
        } else if (!isPM) {
            Bukkit.broadcast(var.getMessages() + "From Slack - " + ChatColor.WHITE + name + ": " + message,
                  "Necessities.slack");
        }
        Necessities.getAI().parseMessage(null, message, JanetAI.Source.Slack, isPM, info);
    }

    class SlackUser {

        private boolean justLoaded = true, viewingChat, isBot;
        private final String id;
        private final String name;
        private String latest;
        private String channel;
        private int rank;

        SlackUser(JsonObject json) {
            this.id = json.get("id").getAsString();
            this.name = json.get("name").getAsString();
            if (json.get("is_bot").getAsBoolean()) {
                this.isBot = true;
                this.rank = 2;
            } else if (json.get("is_primary_owner").getAsBoolean()) {
                this.rank = 3;
            } else if (json.get("is_owner").getAsBoolean()) {
                this.rank = 2;
            } else if (json.get("is_admin").getAsBoolean()) {
                this.rank = 1;
            } else if (json.get("is_ultra_restricted").getAsBoolean()) {
                this.rank = -2;
            } else if (json.get("is_restricted").getAsBoolean()) {
                this.rank = -1;
            }
            //else leave it at 0 for member
        }

        String getName() {
            return this.name;
        }

        String getID() {
            return this.id;
        }

        int getRank() {
            return this.rank;
        }

        boolean isBot() {
            return this.isBot;
        }

        boolean isUltraRestricted() {
            return this.rank >= -2;
        }

        boolean isRestricted() {
            return this.rank >= -1;
        }

        boolean isMember() {
            return this.rank >= 0;
        }

        boolean isAdmin() {
            return this.rank >= 1;
        }

        boolean isOwner() {
            return this.rank >= 2;
        }

        boolean isPrimaryOwner() {
            return this.rank >= 3;
        }

        String getRankName() {
            if (isPrimaryOwner()) {
                return "Primary Owner";
            } else if (isOwner()) {
                return "Owner";
            } else if (isAdmin()) {
                return "Admin";
            } else if (isMember()) {
                return "Member";
            } else if (isRestricted()) {
                return "Restricted";
            } else if (isUltraRestricted()) {
                return "Ultra Restricted";
            }
            return "Error";
        }

        boolean viewingChat() {
            return this.viewingChat;
        }

        void toggleViewingChat() {
            this.viewingChat = !this.viewingChat;
        }

        void setJustLoaded(boolean loaded) {
            this.justLoaded = loaded;
        }

        boolean getJustLoaded() {
            return this.justLoaded;
        }

        String getLatest() {
            return this.latest;
        }

        void setLatest(String latest) {
            this.latest = latest;
        }

        String getChannel() {
            return this.channel;
        }

        void setChannel(String channel) {
            this.channel = channel;
        }

        void sendPrivateMessage(String message) {
            if (message.endsWith("\n")) {
                message = message.substring(0, message.length() - 1);
            }
            JsonObject json = new JsonObject();
            json.addProperty("text", message);
            json.addProperty("channel", this.channel);
            try {
                HttpsURLConnection con = (HttpsURLConnection) hookURL.openConnection();
                con.setDoOutput(true);
                con.setRequestProperty("Content-Type", "application/json;");
                con.setRequestProperty("Accept", "application/json,text/plain");
                con.setRequestMethod("POST");
                OutputStream os = con.getOutputStream();
                os.write(new Gson().toJson(json).getBytes(StandardCharsets.UTF_8));
                os.close();
                InputStream is = con.getInputStream();
                is.close();
                con.disconnect();
            } catch (Exception ignored) {
            }
        }
    }
}