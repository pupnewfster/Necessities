package gg.galaxygaming.necessities.Commands;

import gg.galaxygaming.necessities.Necessities;
import gg.galaxygaming.necessities.RankManager.Rank;
import gg.galaxygaming.necessities.RankManager.RankManager;
import gg.galaxygaming.necessities.RankManager.User;
import gg.galaxygaming.necessities.RankManager.UserManager;
import gg.galaxygaming.necessities.Variables;
import java.util.HashMap;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdWho implements Cmd {

    public boolean commandUse(CommandSender sender, String[] args) {
        Variables var = Necessities.getVar();
        RankManager rm = Necessities.getRM();
        UserManager um = Necessities.getUM();
        CmdHide hide = Necessities.getHide();
        if (sender instanceof Player && !sender.hasPermission("Necessities.seehidden")) {
            HashMap<Rank, String> online = new HashMap<>();
            int numbOnline = 1;
            if (!rm.getOrder().isEmpty()) {
                online.put(rm.getRank(rm.getOrder().size() - 1),
                      rm.getRank(rm.getOrder().size() - 1).getColor() + "Janet, ");
            }
            if (!um.getUsers().isEmpty()) {
                for (User u : um.getUsers().values()) {
                    if (!hide.isHidden(u.getPlayer())) {
                        if (u.isAfk()) {
                            online.put(u.getRank(),
                                  online.containsKey(u.getRank()) ? online.get(u.getRank()) + "[AFK]" + u.getPlayer()
                                        .getDisplayName() + ", " : "[AFK]" + u.getPlayer().getDisplayName() + ", ");
                        } else {
                            online.put(u.getRank(),
                                  online.containsKey(u.getRank()) ? online.get(u.getRank()) + u.getPlayer()
                                        .getDisplayName() + ", " : u.getPlayer().getDisplayName() + ", ");
                        }
                        numbOnline++;
                    }
                }
            }
            sender.sendMessage(var.getMessages() + "There " + amount(numbOnline) + ' ' + var.getObj() + numbOnline + var
                  .getMessages() + " out of a maximum " + var.getObj() + Bukkit.getMaxPlayers() +
                  var.getMessages() + " players online.");
            for (int i = rm.getOrder().size() - 1; i >= 0; i--) {
                Rank r = rm.getRank(i);
                if (online.containsKey(r)) {
                    sender.sendMessage(r.getColor() + r.getName() + "s: " + ChatColor.WHITE + online.get(r).trim()
                          .substring(0, online.get(r).length() - 2));
                }
            }
            return true;
        }
        int numbOnline = Bukkit.getOnlinePlayers().size() + 1;
        sender.sendMessage(
              var.getMessages() + "There " + amount(numbOnline) + ' ' + var.getObj() + numbOnline + var.getMessages()
                    + " out of a maximum " +
                    var.getObj() + Bukkit.getMaxPlayers() + var.getMessages() + " players online.");
        HashMap<Rank, String> online = new HashMap<>();
        if (!rm.getOrder().isEmpty()) {
            online.put(rm.getRank(rm.getOrder().size() - 1),
                  rm.getRank(rm.getOrder().size() - 1).getColor() + "Janet, ");
        }
        if (!um.getUsers().isEmpty()) {
            for (User u : um.getUsers().values()) {
                if (u.isAfk() && hide.isHidden(u.getPlayer())) {
                    online.put(u.getRank(),
                          online.containsKey(u.getRank()) ? online.get(u.getRank()) + "[AFK][HIDDEN]" + u.getPlayer()
                                .getDisplayName() + ", " : "[AFK][HIDDEN]" +
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
                          online.containsKey(u.getRank()) ? online.get(u.getRank()) + u.getPlayer().getDisplayName()
                                + ", " : u.getPlayer().getDisplayName() + ", ");
                }
            }
        }
        for (int i = rm.getOrder().size() - 1; i >= 0; i--) {
            Rank r = rm.getRank(i);
            if (online.containsKey(r)) {
                sender.sendMessage(r.getColor() + r.getName() + "s: " + ChatColor.WHITE + online.get(r).trim()
                      .substring(0, online.get(r).length() - 2));
            }
        }
        return true;
    }

    private String amount(int a) {
        return a == 1 ? "is" : "are";
    }

    public boolean isPaintballEnabled() {
        return true;
    }
}