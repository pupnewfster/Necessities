package com.crossge.necessities.Commands;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.crossge.necessities.Economy.Materials;

public class CmdTop extends Cmd {
	Materials mat = new Materials();
	
	public boolean commandUse(CommandSender sender, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			Location top = p.getLocation();
			double topy = 0;
			for(int i = 0; i < top.getWorld().getMaxHeight(); i++)
				if((new Location(top.getWorld(), top.getX(), i, top.getZ())).getBlock().getType().isSolid()) {
					if(mat.isSlab((new Location(top.getWorld(), top.getX(), i, top.getZ())).getBlock()))
						topy = i + .5;
					else
						topy = i + 1;
				}
			p.teleport(new Location(top.getWorld(), top.getX(), topy, top.getZ(), top.getYaw(), top.getPitch()));
			p.sendMessage(var.getMessages() + "Teleporting to top.");
		} else
			sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "The console cannot use /top soz.");
		return true;
	}
}