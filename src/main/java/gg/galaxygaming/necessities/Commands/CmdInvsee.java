package gg.galaxygaming.necessities.Commands;

import gg.galaxygaming.necessities.Necessities;
import gg.galaxygaming.necessities.Utils;
import gg.galaxygaming.necessities.Variables;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

import java.util.HashMap;
import java.util.UUID;

public class CmdInvsee implements Cmd {
    private final HashMap<PlayerInventory, Player> openInvs = new HashMap<>();

    public Player getFromInv(PlayerInventory inv) {
        return openInvs.get(inv);
    }

    public void closeInv(PlayerInventory inv) {
        openInvs.remove(inv);
    }

    public boolean commandUse(CommandSender sender, String[] args) {
        Variables var = Necessities.getInstance().getVar();
        if (args.length == 0) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Must enter a player to view inventory of.");
            return true;
        }
        if (sender instanceof Player) {
            Player p = (Player) sender;
            UUID uuid = Utils.getID(args[0]);
            if (uuid == null) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Invalid player.");
                return true;
            }
            Player target = Bukkit.getPlayer(uuid);
            if (target.equals(p)) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Must enter a player other than yourself.");
                return true;
            }
            PlayerInventory inv = target.getInventory();
            openInvs.put(inv, target);
            p.sendMessage(var.getObj() + Utils.ownerShip(target.getName()) + var.getMessages() + " inventory opened.");
            p.openInventory(inv);
        } else
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You do not have an inventory.");
        return true;
    }
}