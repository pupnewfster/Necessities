package gg.galaxygaming.necessities.Commands;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import gg.galaxygaming.necessities.Necessities;
import gg.galaxygaming.necessities.Utils;
import gg.galaxygaming.necessities.Variables;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class CmdSkull implements Cmd {

    public boolean commandUse(CommandSender sender, String[] args) {
        Variables var = Necessities.getVar();
        if (args.length == 0) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must enter a player to get their head.");
            return true;
        }
        if (sender instanceof Player) {
            Player p = (Player) sender;
            ItemStack skull = new ItemStack(Material.PLAYER_HEAD, 1);
            SkullMeta meta = (SkullMeta) skull.getItemMeta();
            UUID uuid = Utils.getOfflineID(args[0]);
            ProfileProperty textures = Utils.getPlayerSkin(uuid);
            if (textures == null) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg()
                      + "Unable to retrieve skin of entered player, falling back to no skin.");
                meta.setDisplayName(Utils.ownerShip(args[0]) + " Head");
                skull.setItemMeta(meta);
                p.getInventory().addItem(skull);
                return true;
            }
            PlayerProfile profile = Bukkit.createProfile(uuid, args[0]);
            profile.setProperty(textures);
            meta.setPlayerProfile(profile);
            skull.setItemMeta(meta);
            p.getInventory().addItem(skull);
            p.sendMessage(var.getMessages() + "You have been given 1 head of " + var.getObj() + args[0]);
        } else {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You do not have an inventory.");
        }
        return true;
    }
}