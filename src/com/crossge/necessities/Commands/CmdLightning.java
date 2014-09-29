package com.crossge.necessities.Commands;

import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.crossge.necessities.Economy.Materials;
import com.crossge.necessities.RankManager.User;

public class CmdLightning extends Cmd {
	Materials mat = new Materials();
	
	public boolean commandUse(CommandSender sender, String[] args) {
		if(args.length >= 1) {
			UUID uuid = get.getID(args[0]);
			if(uuid == null) {
				sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Invalid player.");
				return true;
			}
			Player target = sender.getServer().getPlayer(uuid);
			target.sendMessage(var.getMessages() + "Thou hast been smitten!");
			sender.sendMessage(var.getMessages() + "Smiting " + var.getObj() + target.getName());
			target.getWorld().strikeLightning(target.getLocation());
			return true;
		}
		if (sender instanceof Player) {
			Player p = (Player) sender;
			User u = um.getUser(p.getUniqueId());
			Location l = u.getLookingAt();
			if(l == null) {
				p.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Block out of range.");
				return true;
			}
			if(l.getBlock().getType().isSolid())
				l.setY(l.getY() + 1);
			p.getWorld().strikeLightning(l);
        } else
			sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You cannot cast out lightning unless you direct it to a player.");
		return true;
	}
}