package com.crossge.necessities.Commands.Guilds;

import com.crossge.necessities.Guilds.Guild;
import com.crossge.necessities.RankManager.User;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;

public class CmdMap extends GuildCmd {
    public boolean commandUse(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (!p.hasPermission("Necessities.guilds.map")) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You have not have permission to use /guild map.");
                return true;
            }
            Chunk c = p.getLocation().getChunk();
            String up = up(p.getLocation().getYaw());
            ArrayList<Chunk> chunks = new ArrayList<>();
            HashMap<Guild, String> symbols = new HashMap<>();
            for (int z = -4; z < 5; z++)
                for (int x = -19; x < 20; x++) {
                    int cX = c.getX();
                    int cZ = c.getZ();
                    if (up.equals("N")) {
                        cX += x;
                        cZ += z;
                    } else if (up.equals("E")) {
                        cX += z;
                        cZ += x;
                    } else if (up.equals(var.getObj() + "S")) {
                        cX -= x;
                        cZ -= z;
                    } else if (up.equals("W")) {
                        cX -= z;
                        cZ -= x;
                    }
                    if (!p.getWorld().getChunkAt(cX, cZ).isLoaded())
                        p.getWorld().getChunkAt(cX, cZ).load(true);
                    chunks.add(p.getWorld().getChunkAt(cX, cZ));
                    Guild g = gm.chunkOwner(p.getWorld().getChunkAt(cX, cZ));
                    if (g != null && !symbols.containsKey(g)) {
                        if (!symbols.containsValue("/"))
                            symbols.put(g, "/");
                        else if (!symbols.containsValue("#"))
                            symbols.put(g, "#");
                        else if (!symbols.containsValue("~"))
                            symbols.put(g, "~");
                        else if (!symbols.containsValue("&"))
                            symbols.put(g, "&");
                        else if (!symbols.containsValue("%"))
                            symbols.put(g, "%");
                        else if (!symbols.containsValue("$"))
                            symbols.put(g, "$");
                        else if (!symbols.containsValue("@"))
                            symbols.put(g, "@");
                        else if (!symbols.containsValue("!"))
                            symbols.put(g, "!");
                        else if (!symbols.containsValue("^"))
                            symbols.put(g, "^");
                        else
                            symbols.put(g, "*");//TODO: perhaps add more symbols and stuff but this will do for now
                    }
                }
            User u = um.getUser(p.getUniqueId());
            Guild owner = gm.chunkOwner(c);
            String name = "(" + c.getX() + "," + c.getZ() + ") ";
            if (owner != null) {
                name += owner.getName();
                sender.sendMessage(var.getMessages() + parant(name.length() / 2) + ".[ " + owner.relation(u.getGuild()) + name + var.getMessages() + " ]." + parant(name.length() / 2));
            } else {
                name += "Wilderness";
                sender.sendMessage(var.getMessages() + parant(name.length() / 2) + ".[ " + var.getWild() + name + var.getMessages() + " ]." + parant(name.length() / 2));
            }
            String line = "";
            int row = 0;
            for (int i = 0; i < chunks.size(); i++) {
                if (i % 39 == 0 && i != 0) {
                    if (row == 0)
                        sender.sendMessage(line + var.getMessages() + "   " + up);
                    else if (row == 1)
                        sender.sendMessage(line + var.getMessages() + " " + left(up) + var.getMessages() + "   " + right(up));
                    else if (row == 2)
                        sender.sendMessage(line + var.getMessages() + "   " + down(up));
                    else
                        sender.sendMessage(line);
                    row++;
                    line = "";
                }
                Guild g = gm.chunkOwner(chunks.get(i));
                if (i == 175)
                    line += ChatColor.AQUA + "+";
                else if (u.getGuild() == null)
                    line += var.getNeutral() + (g == null ? "-" : symbols.get(g));
                else
                    line += (g == null ? var.getNeutral() + "-" : u.getGuild().relation(g) + symbols.get(g));

            }
            String key = "";
            for (Guild g : symbols.keySet())
                key += (u.getGuild() == null ? var.getNeutral() + symbols.get(g) + ": " + g.getName() + ", " : u.getGuild().relation(g) + symbols.get(g) + ": " + g.getName() + ", ");
            if (!key.equals(""))
                sender.sendMessage(key.substring(0, key.length() - 2));
        } else
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must be a player to view a map of claimed territories around you.");
        return true;
    }

    private String parant(int nameLength) {
        String p = "";
        for (int i = 0; i < 21 - nameLength; i++)
            p += "_";
        return p;
    }

    private String up(float yaw) {
        double rotation = (yaw - 90) % 360;
        if (rotation < 0)
            rotation += 360.0;
        if (67.5 <= rotation && rotation < 112.5 || 112.5 <= rotation && rotation < 157.5)
            return "N";
        else if (157.5 <= rotation && rotation < 202.5 || 202.5 <= rotation && rotation < 247.5)
            return "E";
        else if (247.5 <= rotation && rotation < 292.5 || 292.5 <= rotation && rotation < 337.5)
            return var.getObj() + "S";
        return "W";
    }

    private String left(String up) {
        if (up.equals("N"))
            return "W";
        else if (up.equals("E"))
            return "N";
        else if (up.equals(var.getObj() + "S"))
            return "E";
        return var.getObj() + "S";
    }

    private String right(String up) {
        if (up.equals("N"))
            return "E";
        else if (up.equals("E"))
            return var.getObj() + "S";
        else if (up.equals(var.getObj() + "S"))
            return "W";
        return "N";
    }

    private String down(String up) {
        if (up.equals("N"))
            return var.getObj() + "S";
        else if (up.equals("E"))
            return "W";
        else if (up.equals("W"))
            return "E";
        return "N";
    }
}