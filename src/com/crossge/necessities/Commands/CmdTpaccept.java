package com.crossge.necessities.Commands;

import java.util.UUID;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.crossge.necessities.Teleports;
import com.crossge.necessities.RankManager.User;

public class CmdTpaccept extends Cmd {
	Teleports tps = new Teleports();
	CmdHide hide = new CmdHide();
	
	public boolean commandUse(CommandSender sender, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			UUID uuid = null;
			if(args.length == 0) {
				uuid = tps.lastRequest(p.getUniqueId());
				if(uuid == null) {
					sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You do not have any pending teleport requests.");
					return true;
				}
			}
			if(args.length > 0)
				uuid = get.getID(args[0]);
			if(uuid == null) {
				sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Invalid player.");
				return true;
			}
			Player target = sender.getServer().getPlayer(uuid);
			if(!p.hasPermission("Necessities.seehidden") && hide.isHidden(target)) {
				sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Invalid player.");
				return true;
			}
			if(!tps.hasRequestFrom(p.getUniqueId(), uuid)) {
				sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You do not have any pending teleport request from that player.");
				return true;
			}
			User u = um.getUser(uuid);
			User self = um.getUser(p.getUniqueId());
			if(u.isIgnoring(p.getUniqueId())) {
				sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "That user is ignoring you, so you cannot accept their teleport request.");
				return true;
			}
			if(self.isIgnoring(uuid)) {
				sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You are ignoring that user, so cannot accept their teleport request.");
				return true;
			}
			String type = tps.getRequestType(p.getUniqueId(), uuid);
			tps.removeRequestFrom(p.getUniqueId(), uuid);
			target.sendMessage(var.getObj() + p.getName() + var.getMessages() + " accepted your teleport request");
			p.sendMessage(var.getMessages() + "Teleport request accepted.");
			if(type.equals("toMe"))
				u.teleport(self);
			else if(type.equals("toThem"))
				self.teleport(u);
		} else
			sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You cannot teleport so are unable to get teleport requests.");
		return true;
	}
}