package com.crossge.necessities.Commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.crossge.necessities.Economy.Formatter;

public class CmdSpeed extends Cmd {
	Formatter form = new Formatter();
	
	public boolean commandUse(CommandSender sender, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			String type = "walking";
			if(args.length == 0) {
				if(p.isFlying()) {
					p.setFlySpeed((float) 0.1);
					type = "flying";
				} else
					p.setWalkSpeed((float) 0.2);
				p.sendMessage(var.getMessages() + "Set " + var.getObj() + type + var.getMessages() + " speed to " + var.getObj() + "1" + var.getMessages() + ".");
			}
			else if(args.length == 1) {
				if(!form.isLegal(args[0])) {
					p.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Invalid speed.");
					return true;
				}
				double speed = Double.parseDouble(args[0]);
				if(speed > 5)
					speed = 5;
				if(p.isFlying()) {
					p.setFlySpeed((float) (speed * .1));
					type = "flying";
				} else
					p.setWalkSpeed((float) (speed * .2));
				p.sendMessage(var.getMessages() + "Set " + var.getObj() + type + var.getMessages() + " speed to " + var.getObj() + speed + var.getMessages() + ".");
			} else {
				if(!form.isLegal(args[1])) {
					p.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Invalid speed.");
					return true;
				}
				double speed = Double.parseDouble(args[1]);
				if(speed > 5)
					speed = 5;
				if(args[0].equalsIgnoreCase("flying") || args[0].equalsIgnoreCase("fly")) {
					p.setFlySpeed((float) (speed * .1));
					type = "flying";
				} else
					p.setWalkSpeed((float) (speed * .2));
				p.sendMessage(var.getMessages() + "Set " + var.getObj() + type + var.getMessages() + " speed to " + var.getObj() + speed + var.getMessages() + ".");
			}
		} else
			sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "The console cannot change its speed.");
		return true;
	}
}