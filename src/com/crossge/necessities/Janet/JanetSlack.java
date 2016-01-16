package com.crossge.necessities.Janet;

import com.crossge.necessities.Commands.CmdHide;
import com.crossge.necessities.Economy.BalChecks;
import com.crossge.necessities.Formatter;
import com.crossge.necessities.GetUUID;
import com.crossge.necessities.Necessities;
import com.crossge.necessities.RankManager.Rank;
import com.crossge.necessities.RankManager.RankManager;
import com.crossge.necessities.RankManager.User;
import com.crossge.necessities.RankManager.UserManager;
import com.crossge.necessities.Variables;
import org.bukkit.*;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.regex.Pattern;

public class JanetSlack {
    private static File configFile = new File("plugins/Necessities", "config.yml");
    private static HashMap<String, SlackUser> userMap = new HashMap<>();
    private static HashMap<Integer, ArrayList<String>> helpLists = new HashMap<>();
    private static boolean justLoaded = true, isConnected = false, stopping = false;
    private static String token, channel, channelID, hook, latestInChanel;
    private static JanetRandom r = new JanetRandom();
    private static BukkitRunnable historyReader, keepAlive;
    private static URL hookURL;
    RankManager rm = new RankManager();
    UserManager um = new UserManager();
    JanetWarn warns = new JanetWarn();
    Formatter form = new Formatter();
    BalChecks balc = new BalChecks();
    Variables var = new Variables();
    CmdHide hide = new CmdHide();
    GetUUID get = new GetUUID();

    public void init() {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        token = config.contains("Necessities.SlackToken") ? config.getString("Necessities.SlackToken") : "token";
        channel = config.contains("Necessities.SlackChanel") ? config.getString("Necessities.SlackChanel") : "channel";
        channelID = config.contains("Necessities.ChannelID") ? config.getString("Necessities.ChannelID") : "channelID";
        hook = config.contains("Necessities.WebHook") ? config.getString("Necessities.WebHook") : "webHook";
        if (token.equals("token") || channel.equals("channel") || channelID.equals("channelID") || hook.equals("webHook"))
            return;
        try {
            hookURL = new URL(hook);
        } catch (Exception e) {
            return;
        }
        connect();
    }

    public void disconnect() {
        if (!isConnected)
            return;
        stopping = true;
        historyReader.cancel();
        userMap.clear();
        helpLists.clear();
        keepAlive.cancel();
        sendMessage("Disconnected.");
        sendPost("https://slack.com/api/users.setPresence?token=" + token + "&presence=away&pretty=1");
        isConnected = false;
    }

    public void handleIngameChat(String message) {
        for (SlackUser u : userMap.values())
            if (u.viewingChat())
                u.sendPrivateMessage(message);
    }

    public void sendMessage(String message, boolean isPM, SlackUser u) {
        if (isPM)
            u.sendPrivateMessage(message);
        else
            sendMessage(message);
    }

    public void sendMessage(String message) {
        sendViaHook(message);
        //sendPost("https://slack.com/api/chat.postMessage?token=" + token + "&channel=%23" + channel + "&text=" + ChatColor.stripColor(message.replaceAll(" ", "%20")) + "&as_user=true&pretty=1");
    }

    private void sendViaHook(String message) {
        message = ChatColor.stripColor(message);
        if (message.endsWith("\n"))
            message = message.substring(0, message.length() - 1);
        JSONObject json = new JSONObject();
        json.put("text", message);
        try {
            HttpsURLConnection con = (HttpsURLConnection) hookURL.openConnection();
            con.setDoOutput(true);
            con.setRequestProperty("Content-Type", "application/json;");
            con.setRequestProperty("Accept", "application/json,text/plain");
            con.setRequestMethod("POST");
            OutputStream os = con.getOutputStream();
            os.write(json.toString().getBytes("UTF-8"));
            os.close();
            InputStream is = con.getInputStream();
            is.close();
            con.disconnect();
        } catch (Exception e) {
        }
    }

    public void getPms() {
        if (stopping)
            return;
        for (SlackUser u : userMap.values()) {
            if (stopping)
                return;
            try {
                URL url = new URL("https://slack.com/api/im.history?token=" + token + "&channel=" + u.getChannel() + "&oldest=" + u.getLatest() + "&pretty=1");
                HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null)
                    response.append(inputLine);
                in.close();
                JSONParser jsonParser = new JSONParser();
                JSONObject json = (JSONObject) jsonParser.parse(response.toString());
                JSONArray messages = (JSONArray) json.get("messages");
                if (messages != null)//If no messages from someone ignore them
                    for (int i = messages.size() - 1; i >= 0; i--) {
                        JSONObject message = (JSONObject) messages.get(i);
                        if (!message.containsKey("subtype") && !u.getJustLoaded()) {
                            SlackUser info = getUserInfo((String) message.get("user"));
                            if (!info.getName().contains("janet")) {
                                String text = (String) message.get("text");
                                while (text.contains("<") && text.contains(">"))
                                    text = text.split("<@")[0] + "@" + getUserInfo(text.split("<@")[1].split(">:")[0]).getName() + ":" + text.split("<@")[1].split(">:")[1];
                                sendSlackChat(u, text, true);
                            }
                        }
                        if (i == 0)
                            u.setLatest((String) message.get("ts"));
                    }
            } catch (Exception e) {
            }
            u.setJustLoaded(false);
            if (stopping)
                return;
        }
    }

    public void getHistory() {
        try {
            URL url = new URL("https://slack.com/api/channels.history?token=" + token + "&channel=" + channelID + "&oldest=" + latestInChanel + "&pretty=1");
            HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null)
                response.append(inputLine);
            in.close();
            JSONParser jsonParser = new JSONParser();
            JSONObject json = (JSONObject) jsonParser.parse(response.toString());
            JSONArray messages = (JSONArray) json.get("messages");
            for (int i = messages.size() - 1; i >= 0; i--) {
                JSONObject message = (JSONObject) messages.get(i);
                if (!message.containsKey("subtype") && !justLoaded) {
                    SlackUser info = getUserInfo((String) message.get("user"));
                    if (!info.getName().contains("janet")) {
                        String text = (String) message.get("text");
                        while (text.contains("<") && text.contains(">"))
                            text = text.split("<@")[0] + "@" + getUserInfo(text.split("<@")[1].split(">:")[0]).getName() + ":" + text.split("<@")[1].split(">:")[1];
                        sendSlackChat(info, text, false);
                    }
                }
                if (i == 0)
                    latestInChanel = (String) message.get("ts");
            }
        } catch (Exception e) {
        }
        justLoaded = false;
    }

    private SlackUser getUserInfo(String id) {
        if (userMap.containsKey(id))
            return userMap.get(id);
        //Almost never should get past this point as it maps the users when it connects unless a new user gets invited
        try {
            URL url = new URL("https://slack.com/api/users.info?token=" + token + "&user=" + id + "&pretty=1");
            HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null)
                response.append(inputLine);
            in.close();
            JSONParser jsonParser = new JSONParser();
            userMap.put(id, new SlackUser((JSONObject) jsonParser.parse(response.toString())));
        } catch (Exception e) {
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
        } catch (Exception e) {
        }
    }

    private void connect() {
        if (isConnected)
            return;
        try {
            URL url = new URL("https://slack.com/api/rtm.start?token=" + token + "&simple_latest=true&pretty=1");
            HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null)
                response.append(inputLine);
            in.close();

            JSONParser jsonParser = new JSONParser();
            JSONObject json = (JSONObject) jsonParser.parse(response.toString());
            //Get a more recent timestamp than zero so first history pass is more efficient
            latestInChanel = (String) json.get("latest_event_ts");
            //Map users
            JSONArray users = (JSONArray) json.get("users");
            for (int i = users.size() - 1; i >= 0; i--) {
                JSONObject user = (JSONObject) users.get(i);
                String id = (String) user.get("id");
                if (!userMap.containsKey(id))
                    userMap.put(id, new SlackUser(user));
            }
        } catch (Exception e) {
        }
        setUserChannels();
        historyReader = new BukkitRunnable() {
            @Override
            public void run() {
                getHistory();
                getPms();
            }
        };
        historyReader.runTaskTimerAsynchronously(Necessities.getInstance(), 0, 20);
        keepAlive = new BukkitRunnable() {
            @Override
            public void run() {
                sendPost("https://slack.com/api/users.setPresence?token=" + token + "&presence=auto&pretty=1");
                sendPost("https://slack.com/api/users.setActive?token=" + token + "&pretty=1");
            }
        };
        keepAlive.runTaskTimerAsynchronously(Necessities.getInstance(), 0, 30 * 60 * 20);//Every thirty minutes force it to show that janet is still alive
        setHelp();
        sendMessage("Connected.");
        isConnected = true;
    }

    private void setUserChannels() {
        try {
            URL url = new URL("https://slack.com/api/im.list?token=" + token + "&pretty=1");
            HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null)
                response.append(inputLine);
            in.close();

            JSONParser jsonParser = new JSONParser();
            JSONObject json = (JSONObject) jsonParser.parse(response.toString());
            //Map user channels
            JSONArray ims = (JSONArray) json.get("ims");
            for (int i = ims.size() - 1; i >= 0; i--) {
                JSONObject im = (JSONObject) ims.get(i);
                String userID = (String) im.get("user");
                if (userMap.containsKey(userID))
                    userMap.get(userID).setChannel((String) im.get("id"));
            }
        } catch (Exception e) {
        }
    }

    private String corTime(String time) {
        return time.length() == 1 ? "0" + time : time;
    }

    private String getLine(int page, int time, ArrayList<String> helpList) {
        page *= 10;
        if (helpList.size() < time + page + 1 || time == 10)
            return null;
        return helpList.get(page + time);
    }

    private void setHelp() {
        ArrayList<String> temp = new ArrayList<>();
        temp.add("!help <page> ~ View the help messages on <page>.");
        temp.add("!rank ~ Shows you what rank you have.");
        temp.add("!bans ~ Shows you the banlist.");
        temp.add("!whois <name> ~ View information about <name>.");
        temp.add("!who ~ View the online players.");
        temp.add("!baltop <page> ~ View <page> of baltop.");
        temp.add("!bal <name> ~ View <name>'s balance.");
        temp.add("!devs ~ View the devs.");
        temp.add("!warn <name> <reason> ~ Warn <name> for <reason>.");
        temp.add("!worlds ~ View the loaded worlds.");
        helpLists.put(0, (ArrayList<String>) temp.clone());//Member
        temp.add("!meme <number> ~ Generate a number between 0 and <number>.");
        temp.add("!say <message> ~ Sends the message <message> to the playesr on the server.");
        temp.add("!kick <name> <reason> ~ Kicks <name> for an optional <reason>.");
        temp.add("!tempban <name> <time> <reason> ~ Tempbans <name> for <time> and an optional <reason>.");
        temp.add("!ban <name> <reason> ~ Bans <name> for an optional <reason>.");
        temp.add("!unban <name> ~ Unbans <name>.");
        temp.add("!banip <ip> <reason> ~ Bans <ip> for an optional <reason>.");
        temp.add("!unbanip <ip> ~ Unbans <ip>.");
        temp.add("!slap <name> ~ Slaps <name> sky high.");
        temp.add("!mute <name> ~ Mutes and unmutes <name>.");
        temp.add("!showchat ~ Toggles showing the in game chat. (Only available in private messages)");
        temp.add("!tps ~ Shows the ingame ticks per second, and memory usage.");
        helpLists.put(1, (ArrayList<String>) temp.clone());//Admin
        helpLists.put(2, (ArrayList<String>) temp.clone());//Owner
        helpLists.put(3, (ArrayList<String>) temp.clone());//Primary owner
        temp.clear();
    }

    private void sendSlackChat(SlackUser info, String message, boolean isPM) {
        if (!info.isMember()) {
            sendMessage("Error: You are restricted or ultra restricted", isPM, info);
            return;
        }
        final String name = info.getName();
        if (message.startsWith("!")) {
            String m = "";
            if (message.startsWith("!help")) {
                int page = 0;
                if (message.split(" ").length > 1 && !form.isLegal(message.split(" ")[1])) {
                    sendMessage("Error: You must enter a valid help page.", isPM, info);
                    return;
                }
                if (message.split(" ").length > 1)
                    page = Integer.parseInt(message.split(" ")[1]);
                if (message.split(" ").length == 1 || page <= 0)
                    page = 1;
                int time = 0;
                int rounder = 0;
                ArrayList<String> helpList = helpLists.get(info.getRank());
                if (helpList.size() % 10 != 0)
                    rounder = 1;
                int totalpages = (helpList.size() / 10) + rounder;
                if (page > totalpages) {
                    sendMessage("Error: Input a number from 1 to " + Integer.toString(totalpages), isPM, info);
                    return;
                }
                m += " ---- Help -- Page " + Integer.toString(page) + "/" + Integer.toString(totalpages) + " ---- \n";
                page = page - 1;
                String msg = getLine(page, time, helpList);
                while (msg != null) {
                    m += msg + "\n";
                    time++;
                    msg = getLine(page, time, helpList);
                }
                if (page + 1 < totalpages)
                    m += "Type !help " + Integer.toString(page + 2) + " to read the next page.\n";
            } else if (message.startsWith("!rank")) {
                m += info.getRankName() + "\n";
            } else if (message.startsWith("!bans") || message.startsWith("!banlist") || message.startsWith("!bannedplayers") || message.startsWith("!bannedips")) {
                BanList bans = Bukkit.getBanList(BanList.Type.NAME);
                m += "Banned players: ";
                for (BanEntry e : bans.getBanEntries())
                    m += e.getTarget() + ", ";
                if (m.endsWith(", "))
                    m = m.substring(0, m.length() - 2).trim();
                m += "\nBanned ips: ";
                bans = Bukkit.getBanList(BanList.Type.IP);
                for (BanEntry e : bans.getBanEntries())
                    m += e.getTarget() + ", ";
                if (m.endsWith(", "))
                    m = m.substring(0, m.length() - 2).trim();
            } else if (message.startsWith("!whois ")) {
                if (message.split(" ").length == 1) {
                    sendMessage("Error: You must enter a player to view info of.", isPM, info);
                    return;
                }
                String target = message.split(" ")[1];
                UUID uuid = get.getID(target);
                if (uuid == null) {
                    uuid = get.getOfflineID(target);
                    if (uuid == null) {
                        sendMessage("Error: That player has not joined the server. If the player is offline, please use the full and most recent name.", isPM, info);
                        return;
                    }
                }
                User u = um.getUser(uuid);
                YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);
                m += "===== WhoIs: " + u.getName() + " =====\n";
                if (u.getPlayer() != null)
                    m += " - Nick: " + u.getPlayer().getDisplayName() + "\n";
                else {
                    m += (u.getNick() == null ? " - Name: " + u.getName() : " - Nick: " + u.getNick()) + "\n";
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
                    m += " - Seen last on " + month + "/" + day + "/" + year + " at " + hour + ":" + minute + " and " + second + " " + (Integer.parseInt(second) > 1 ? "seconds" : "second") + "\n";
                }
                m += " - Time played: " + u.getTimePlayed() + "\n";
                m += " - Rank: " + u.getRank().getName() + "\n";
                String subranks = u.getSubranks();
                if (subranks != null)
                    m += " - Subranks: " + subranks + "\n";
                String permissions = u.getPermissions();
                if (permissions != null)
                    m += " - Permission nodes: " + permissions + "\n";
                if (u.getPlayer() != null) {
                    Player p = u.getPlayer();
                    m += " - Exp: " + form.addCommas(p.getTotalExperience()) + " (Level " + p.getLevel() + ")\n";
                    m += " - Location: (" + p.getWorld().getName() + ", " + p.getLocation().getBlockX() + ", " + p.getLocation().getBlockY() + ", " + p.getLocation().getBlockZ() + ")\n";
                }
                if (config.contains("Necessities.Economy") && config.getBoolean("Necessities.Economy"))
                    m += " - Money: " + "$" + form.addCommas(balc.bal(uuid)) + "\n";
                if (u.getPlayer() != null) {
                    Player p = u.getPlayer();
                    m += " - IP Adress: " + p.getAddress().toString().split("/")[1].split(":")[0] + "\n";
                    String gamemode = "Survival";
                    if (p.getGameMode().equals(GameMode.ADVENTURE))
                        gamemode = "Adventure";
                    else if (p.getGameMode().equals(GameMode.CREATIVE))
                        gamemode = "Creative";
                    else if (p.getGameMode().equals(GameMode.SPECTATOR))
                        gamemode = "Spectator";
                    m += " - Gamemode: " + gamemode + "\n";
                    m += " - Banned: " + (p.isBanned() ? "true" : "false") + "\n";
                    m += " - Visible: " + (hide.isHidden(p) ? "false" : "true") + "\n";
                } else
                    m += " - Banned: " + (Bukkit.getOfflinePlayer(u.getUUID()).isBanned() ? "true" : "false") + "\n";
            } else if (message.startsWith("!who")) {
                int numbOnline = Bukkit.getOnlinePlayers().size() + 1;
                HashMap<Rank, String> online = new HashMap<>();
                if (!rm.getOrder().isEmpty())
                    online.put(rm.getRank(rm.getOrder().size() - 1), rm.getRank(rm.getOrder().size() - 1).getColor() + "Janet, ");
                if (!um.getUsers().isEmpty())
                    for (User u : um.getUsers().values())
                        if (u.isAfk() && hide.isHidden(u.getPlayer()))
                            online.put(u.getRank(), online.containsKey(u.getRank()) ? online.get(u.getRank()) + "[AFK][HIDDEN]" + u.getPlayer().getDisplayName() + ", " : "[AFK][HIDDEN]" +
                                    u.getPlayer().getDisplayName() + ", ");
                        else if (u.isAfk())
                            online.put(u.getRank(), online.containsKey(u.getRank()) ? online.get(u.getRank()) + "[AFK]" + u.getPlayer().getDisplayName() + ", " : "[AFK]" + u.getPlayer().getDisplayName() + ", ");
                        else if (hide.isHidden(u.getPlayer()))
                            online.put(u.getRank(), online.containsKey(u.getRank()) ? online.get(u.getRank()) + "[HIDDEN]" + u.getPlayer().getDisplayName() + ", " : "[HIDDEN]" + u.getPlayer().getDisplayName() + ", ");
                        else
                            online.put(u.getRank(), online.containsKey(u.getRank()) ? online.get(u.getRank()) + u.getPlayer().getDisplayName() + ", " : u.getPlayer().getDisplayName() + ", ");
                m += "There " + (numbOnline == 1 ? "is " : "are ") + numbOnline + " out of a maximum " + Bukkit.getMaxPlayers() + " players online.\n";
                for (int i = rm.getOrder().size() - 1; i >= 0; i--) {
                    Rank r = rm.getRank(i);
                    if (online.containsKey(r))
                        m += r.getName() + "s: " + online.get(r).trim().substring(0, online.get(r).length() - 2) + "\n";
                }
            } else if (message.startsWith("!devs")) {
                String d = var.getMessages() + "The Devs for Necessities are: ";
                List<String> devs = Necessities.getInstance().getDevs();
                for (int i = 0; i < devs.size(); i++)
                    d += (i + 1 >= devs.size() ? "and " + devs.get(i) + "." : devs.get(i) + ", ");
                m += d + "\n";
            } else if (message.startsWith("!baltop") || message.startsWith("!moneytop") || message.startsWith("!balancetop")) {
                int page = 0;
                if (message.split(" ").length > 2) {
                    if (!form.isLegal(message.split(" ")[1])) {
                        sendMessage("Error: You must enter a valid baltop page.", isPM, info);
                        return;
                    }
                    page = Integer.parseInt(message.split("!baltop ")[1]);
                }
                if (message.split(" ").length <= 1 || page == 0)
                    page = 1;
                int time = 0;
                String bal;
                int totalpages = balc.baltopPages();
                if (page > totalpages) {
                    sendMessage("Error: Input a number from 1 to " + Integer.toString(totalpages), isPM, info);
                    return;
                }
                m += "Balance Top Page [" + Integer.toString(page) + "/" + Integer.toString(totalpages) + "]\n";
                page = page - 1;
                bal = balc.balTop(page, time);
                while (bal != null) {
                    bal = ChatColor.GOLD + Integer.toString((page * 10) + time + 1) + ". " + var.getCatalog() + bal.split(" ")[0] + " has: " + var.getMoney() + "$" + form.addCommas(bal.split(" ")[1]);
                    m += bal + "\n";
                    time++;
                    bal = balc.balTop(page, time);
                }
            } else if (message.startsWith("!bal ") || message.startsWith("!balance ") || message.startsWith("!money ")) {
                if (message.split(" ").length == 1) {
                    sendMessage("Error: You must enter a player to view info of.", isPM, info);
                    return;
                }
                String target = message.split(" ")[1];
                String playersname;
                UUID uuid = get.getID(target);
                if (uuid == null) {
                    uuid = get.getOfflineID(target);
                    if (uuid == null) {
                        sendMessage("Error: That player has not joined the server. If the player is offline, please use the full and most recent name.", isPM, info);
                        return;
                    }
                    playersname = Bukkit.getOfflinePlayer(uuid).getName();
                } else
                    playersname = Bukkit.getPlayer(uuid).getName();
                String balance = balc.bal(uuid);
                if (balance == null) {
                    sendMessage("Error: That player is not in my records. If the player is offline, please use the full and most recent name.", isPM, info);
                    return;
                }
                m += ((playersname.endsWith("s") || playersname.endsWith("S")) ? playersname + "'" : playersname + "'s") + " balance is: " + "$" + form.addCommas(balance) + "\n";
            } else if (message.startsWith("!warn ")) {
                message = message.replaceFirst("!warn ", "");
                if (message.split(" ").length < 2) {
                    sendMessage("Error: You must enter a player to warn and a reason.", isPM, info);
                    return;
                }
                UUID uuid = get.getID(message.split(" ")[0]);
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
                Bukkit.getScheduler().scheduleSyncDelayedTask(Necessities.getInstance(), new Runnable() {
                    @Override
                    public void run() {
                        warns.warn(target.getUniqueId(), reason, name);
                    }
                });
                m += target.getName() + " was warned by " + name + " for " + reason + ".\n";
            } else if (message.startsWith("!worlds")) {
                String levels = "";
                ArrayList<String> worlds = new ArrayList<>();
                for (World world : Bukkit.getWorlds())
                    worlds.add(world.getName());
                for (int i = 0; i < worlds.size() - 1; i++)
                    levels += worlds.get(i) + ", ";
                levels += "and " + worlds.get(worlds.size() - 1) + ".";
                m += "The worlds are: " + levels + "\n";
            } else if (message.startsWith("!say ") && info.isAdmin()) {
                Bukkit.broadcastMessage(ChatColor.WHITE + name + ": " + message.replaceFirst("!say ", ""));
                return;
            } else if (message.startsWith("!meme ") && info.isAdmin()) {
                int applePie = 0;
                try {
                    applePie = Integer.parseInt(message.split(" ")[1]);
                } catch (Exception e) {
                }
                m += r.memeRandom(applePie) + "\n";
            } else if (message.startsWith("!kick ") && info.isAdmin()) {
                message = message.replaceFirst("!kick ", "");
                if (message.split(" ").length == 0) {
                    sendMessage("Error: You must enter a player to kick and a reason.", isPM, info);
                    return;
                }
                UUID uuid = get.getID(message.split(" ")[0]);
                if (uuid == null) {
                    sendMessage("Error: Invalid player.", isPM, info);
                    return;
                }
                final Player target = Bukkit.getPlayer(uuid);
                final String reason = ChatColor.translateAlternateColorCodes('&', message.replaceFirst(message.split(" ")[0], "").trim());
                Bukkit.broadcastMessage(var.getMessages() + name + " kicked " + var.getObj() + target.getName() + (reason.equals("") ? "" : var.getMessages() + " for " + var.getObj() + reason));
                m += name + " kicked " + target.getName() + (reason.equals("") ? "" : " for " + reason) + "\n";
                Bukkit.getScheduler().scheduleSyncDelayedTask(Necessities.getInstance(), new Runnable() {
                    @Override
                    public void run() {
                        target.kickPlayer(reason);
                    }
                });
            } else if (message.startsWith("!ban ") && info.isAdmin()) {
                message = message.replaceFirst("!ban ", "");
                if (message.split(" ").length == 0) {
                    sendMessage("Error: You must enter a player to ban.", isPM, info);
                    return;
                }
                UUID uuid = get.getID(message.split(" ")[0]);
                if (uuid == null)
                    uuid = get.getOfflineID(message.split(" ")[0]);
                if (uuid == null) {
                    sendMessage("Error: Invalid player.", isPM, info);
                    return;
                }
                final OfflinePlayer target = Bukkit.getOfflinePlayer(uuid);
                if (target.getPlayer() != null && target.getPlayer().hasPermission("Necessities.antiBan") && !info.isOwner()) {
                    sendMessage("Error: You may not ban someone who has Necessities.antiBan.", isPM, info);
                    return;
                }
                final String reason = (message.split(" ").length > 1) ? message.replaceFirst(message.split(" ")[0], "").trim() : "";
                String theirName = target.getName();
                BanList bans = Bukkit.getBanList(BanList.Type.NAME);
                if (target.getPlayer() != null) {
                    Bukkit.getScheduler().scheduleSyncDelayedTask(Necessities.getInstance(), new Runnable() {
                        @Override
                        public void run() {
                            target.getPlayer().kickPlayer(reason);
                        }
                    });
                }
                bans.addBan(theirName, reason, null, "Slack_" + name);
                Bukkit.broadcastMessage(var.getMessages() + name + " banned " + var.getObj() + theirName + var.getMessages() + (reason.equals("") ? "." : " for " + var.getObj() + reason + var.getMessages() + "."));
                m += name + " banned " + theirName + (reason.equals("") ? "." : " for " + reason + ".") + "\n";
            } else if (message.startsWith("!unban ") && info.isAdmin()) {
                message = message.replaceFirst("!unban ", "");
                if (message.split(" ").length == 0) {
                    sendMessage("Error: You must enter a player to unban.", isPM, info);
                    return;
                }
                UUID uuid = get.getID(message.split(" ")[0]);
                if (uuid == null)
                    uuid = get.getOfflineID(message.split(" ")[0]);
                if (uuid == null) {
                    sendMessage("Error: Invalid player.", isPM, info);
                    return;
                }
                OfflinePlayer target = Bukkit.getOfflinePlayer(uuid);
                BanList bans = Bukkit.getBanList(BanList.Type.NAME);
                String theirName = target.getName();
                if (!bans.isBanned(theirName)) {
                    sendMessage("Error: That player is not banned.", isPM, info);
                    return;
                }
                bans.pardon(theirName);
                Bukkit.broadcastMessage(var.getMessages() + name + " unbanned " + theirName + ".");
                m += name + " unbanned " + theirName + ".\n";
            } else if (message.startsWith("!mute ") && info.isAdmin()) {
                message = message.replaceFirst("!mute ", "");
                if (message.split(" ").length == 0) {
                    sendMessage("Error: You must enter a player to mute.", isPM, info);
                    return;
                }
                UUID uuid = get.getID(message.split(" ")[0]);
                if (uuid == null) {
                    sendMessage("Error: Invalid player.", isPM, info);
                    return;
                }
                User u = um.getUser(uuid);
                Bukkit.broadcastMessage(var.getObj() + name + var.getMessages() + (!u.isMuted() ? " muted " : " unmuted ") + var.getObj() + u.getPlayer().getDisplayName() + var.getMessages() + ".");
                u.getPlayer().sendMessage(var.getDemote() + "You have been " + var.getObj() + (!u.isMuted() ? "muted" : "unmuted") + var.getMessages() + ".");
                m += name + (!u.isMuted() ? " muted " : " unmuted ") + u.getPlayer().getDisplayName() + ".\n";
                u.setMuted(!u.isMuted());
            } else if (message.startsWith("!slap ") && info.isAdmin()) {
                message = message.replaceFirst("!slap ", "");
                if (message.split(" ").length == 0) {
                    sendMessage("Error: You must enter a player to slap.", isPM, info);
                    return;
                }
                UUID uuid = get.getID(message.split(" ")[0]);
                if (uuid == null) {
                    sendMessage("Error: Invalid player.", isPM, info);
                    return;
                }
                Player target = Bukkit.getPlayer(uuid);
                Location loc = target.getLocation().clone().add(0, 2500, 0);
                target.teleport(loc);
                Bukkit.broadcastMessage(var.getMessages() + target.getName() + " was slapped sky high by " + name);
                m += target.getName() + " was slapped sky high by " + name + "\n";
            } else if (message.startsWith("!tempban ") && info.isAdmin()) {
                message = message.replaceFirst("!tempban ", "");
                if (message.split(" ").length < 2) {
                    sendMessage("Error: You must enter a player to ban and a duration in minutes.", isPM, info);
                    return;
                }
                UUID uuid = get.getID(message.split(" ")[0]);
                if (uuid == null)
                    uuid = get.getOfflineID(message.split(" ")[0]);
                if (uuid == null) {
                    sendMessage("Error: Invalid player.", isPM, info);
                    return;
                }
                final OfflinePlayer target = Bukkit.getOfflinePlayer(uuid);
                if (target.getPlayer() != null && target.getPlayer().hasPermission("Necessities.antiBan") && !info.isOwner()) {
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
                final String reason = (message.split(" ").length > 2) ? message.replaceFirst(message.split(" ")[0], "").trim() : "";
                BanList bans = Bukkit.getBanList(BanList.Type.NAME);
                String theirName = target.getName();
                if (target.getPlayer() != null) {
                    Bukkit.getScheduler().scheduleSyncDelayedTask(Necessities.getInstance(), new Runnable() {
                        @Override
                        public void run() {
                            target.getPlayer().kickPlayer(reason);
                        }
                    });
                }
                Date date = new Date(System.currentTimeMillis() + minutes * 60 * 1000);
                bans.addBan(theirName, reason, date, "Slack_" + name);
                Bukkit.broadcastMessage(var.getMessages() + name + " banned " + var.getObj() + theirName + var.getMessages() + " for " + var.getObj() + minutes + var.getMessages() +
                        " " + (minutes == 1 ? "minute" : "minutes") + (reason.equals("") ? "." : " for the reason " + var.getObj() + reason + var.getMessages() + "."));
                m += name + " banned " + theirName + " for " + minutes + " " + (minutes == 1 ? "minute" : "minutes") + (reason.equals("") ? "." : " for the reason " + reason + ".") + "\n";
            } else if (message.startsWith("!banip ") && info.isAdmin()) {//TODO
                message = message.replaceFirst("!banip ", "");
                if (message.split(" ").length == 0) {
                    sendMessage("Error: You must enter an ip to ban.", isPM, info);
                    return;
                }
                UUID uuid = get.getID(message.split(" ")[0]);
                if (uuid != null) {
                    final Player target = Bukkit.getPlayer(uuid);
                    if (target.hasPermission("Necessities.antiBan") && !info.isOwner()) {
                        sendMessage("Error: You may not ban someone who has Necessities.antiBan.", isPM, info);
                        return;
                    }
                    final String reason = (message.split(" ").length > 1) ? message.replaceFirst(message.split(" ")[0], "").trim() : "";
                    String theirName = target.getName();
                    BanList bans = Bukkit.getBanList(BanList.Type.IP);
                    String theirIP = target.getAddress().toString().split("/")[1].split(":")[0];
                    Bukkit.getScheduler().scheduleSyncDelayedTask(Necessities.getInstance(), new Runnable() {
                        @Override
                        public void run() {
                            target.getPlayer().kickPlayer(reason);
                        }
                    });
                    bans.addBan(theirIP, reason, null, "Slack_" + name);
                    Bukkit.broadcastMessage(var.getMessages() + name + " banned " + var.getObj() + theirName + var.getMessages() + (reason.equals("") ? "." : " for " + var.getObj() + reason + var.getMessages() + "."));
                    m += name + " banned " + theirName + (reason.equals("") ? "." : " for " + reason + ".") + "\n";
                } else {
                    boolean validIp = false;
                    try {
                        Pattern ipAdd = Pattern.compile("^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");
                        validIp = ipAdd.matcher(message.split(" ")[0]).matches();
                    } catch (Exception e) {
                    }
                    if (!validIp) {
                        sendMessage("Error: Invalid ip.", isPM, info);
                        return;
                    }
                    final String reason = (message.split(" ").length > 1) ? message.replaceFirst(message.split(" ")[0], "").trim() : "";
                    BanList bans = Bukkit.getBanList(BanList.Type.IP);
                    String theirIP = message.split(" ")[0];
                    for (final Player t : Bukkit.getOnlinePlayers())
                        if (t.getAddress().toString().split("/")[1].split(":")[0].equals(theirIP)) {
                            Bukkit.getScheduler().scheduleSyncDelayedTask(Necessities.getInstance(), new Runnable() {
                                @Override
                                public void run() {
                                    t.getPlayer().kickPlayer(reason);
                                }
                            });
                            break;
                        }
                    bans.addBan(theirIP, reason, null, "Console");
                    Bukkit.broadcastMessage(var.getMessages() + name + " banned " + var.getObj() + theirIP + var.getMessages() + (reason.equals("") ? "." : " for " + var.getObj() + reason + var.getMessages() + "."));
                    m += name + " banned " + theirIP + (reason.equals("") ? "." : " for " + reason + ".") + "\n";
                }
            } else if (message.startsWith("!unbanip ") && info.isAdmin()) {
                message = message.replaceFirst("!unbanip ", "");
                if (message.split(" ").length == 0) {
                    sendMessage("Error: You must enter an ip to unban.", isPM, info);
                    return;
                }
                boolean validIp = false;
                try {
                    Pattern ipAdd = Pattern.compile("^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");
                    validIp = ipAdd.matcher(message.split(" ")[0]).matches();
                } catch (Exception e) {
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
                Bukkit.broadcastMessage(var.getMessages() + name + " unbanned " + theirIP + ".");
                m += name + " unbanned " + theirIP + ".\n";
            } else if (message.startsWith("!tps")) {
                m += form.getTPS() + "\n";
                int mb = 1024 * 1024;
                Runtime runtime = Runtime.getRuntime();
                m += "Max Memory: " + runtime.maxMemory() / mb + " mb.\n";
                m += "Total Memory: " + runtime.totalMemory() / mb + " mb.\n";
                m += "Free Memory: " + runtime.freeMemory() / mb + " mb.\n";
                m += "Used Memory: " + (runtime.totalMemory() - runtime.freeMemory()) / mb + " mb.\n";
                for (World w : Bukkit.getWorlds()) {
                    m += "World: " + w.getName() + "\n";
                    m += "    Entities Loaded: " + w.getEntities().size() + "\n";
                }
            } else if ((message.startsWith("!showchat") || message.startsWith("!togglechat") || message.startsWith("!showingamechat") || message.startsWith("!ingamechat")) && info.isAdmin()) {
                if (!isPM)
                    m += "Error: You must pm me to be able to view in game chat.\n";
                else {
                    m += (info.viewingChat() ? "No longer" : "You are now") + " viewing the in game chat.\n";
                    info.toggleViewingChat();
                }
            } else if (!isPM) {
                Bukkit.broadcast(var.getMessages() + "From Slack - " + ChatColor.WHITE + name + ": " + message, "Necessities.slack");
                return;
            }
            sendMessage(m, isPM, info);
        } else if (!isPM)
            Bukkit.broadcast(var.getMessages() + "From Slack - " + ChatColor.WHITE + name + ": " + message, "Necessities.slack");
    }

    private class SlackUser {
        private boolean justLoaded = true, viewingChat = false;
        private String id, name, latest, channel;
        private int rank = 0;

        public SlackUser(JSONObject json) {
            this.id = (String) json.get("id");
            this.name = (String) json.get("name");
            this.latest = latestInChanel;
            if ((boolean) json.get("is_primary_owner"))
                this.rank = 3;
            else if ((boolean) json.get("is_owner"))
                this.rank = 2;
            else if ((boolean) json.get("is_admin"))
                this.rank = 1;
            else if ((boolean) json.get("is_ultra_restricted"))
                this.rank = -2;
            else if ((boolean) json.get("is_restricted"))
                this.rank = -1;
            //else leave it at 0 for member
        }

        public String getName() {
            return this.name;
        }

        public String getID() {
            return this.id;
        }

        public int getRank() {
            return this.rank;
        }

        public boolean isUltraRestricted() {
            return this.rank >= -2;
        }

        public boolean isRestricted() {
            return this.rank >= -1;
        }

        public boolean isMember() {
            return this.rank >= 0;
        }

        public boolean isAdmin() {
            return this.rank >= 1;
        }

        public boolean isOwner() {
            return this.rank >= 2;
        }

        public boolean isPrimaryOwner() {
            return this.rank >= 3;
        }

        public String getRankName() {
            if (isPrimaryOwner())
                return "Primary Owner";
            else if (isOwner())
                return "Owner";
            else if (isAdmin())
                return "Admin";
            else if (isMember())
                return "Member";
            else if (isRestricted())
                return "Restricted";
            else if (isUltraRestricted())
                return "Ultra Restricted";
            return "Error";
        }

        public boolean viewingChat() {
            return this.viewingChat;
        }

        public void toggleViewingChat() {
            this.viewingChat = !this.viewingChat;
        }

        public void setJustLoaded(boolean loaded) {
            this.justLoaded = loaded;
        }

        public boolean getJustLoaded() {
            return this.justLoaded;
        }

        public String getLatest() {
            return this.latest;
        }

        public void setLatest(String latest) {
            this.latest = latest;
        }

        public String getChannel() {
            return this.channel;
        }

        public void setChannel(String channel) {
            this.channel = channel;
        }

        public void sendPrivateMessage(String message) {
            message = ChatColor.stripColor(message);
            if (message.endsWith("\n"))
                message = message.substring(0, message.length() - 1);
            JSONObject json = new JSONObject();
            json.put("text", message);
            json.put("channel", this.channel);
            try {
                HttpsURLConnection con = (HttpsURLConnection) hookURL.openConnection();
                con.setDoOutput(true);
                con.setRequestProperty("Content-Type", "application/json;");
                con.setRequestProperty("Accept", "application/json,text/plain");
                con.setRequestMethod("POST");
                OutputStream os = con.getOutputStream();
                os.write(json.toString().getBytes("UTF-8"));
                os.close();
                InputStream is = con.getInputStream();
                is.close();
                con.disconnect();
            } catch (Exception e) {
            }
        }
    }
}