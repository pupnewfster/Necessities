package com.crossge.necessities.Commands;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CmdMachineGun extends Cmd {
	public boolean commandUse(CommandSender sender, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			ItemStack hand = player.getItemInHand();
			if(hand == null || !hand.getType().equals(Material.BOW)) {
				player.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You are not holding an bow.");
				return true;
			}
			ItemMeta handMeta = hand.getItemMeta();
			ArrayList<String> lore = new ArrayList<String>();
			if(handMeta.hasLore())
				lore = (ArrayList<String>) handMeta.getLore();
			if(lore.contains("Machine gun")) {
				lore.remove("Machine gun");
				player.sendMessage(var.getMessages() + "Successfully turned your machine gun back into a bow.");
			} else {
				lore.add("Machine gun");
				player.sendMessage(var.getMessages() + "Successfully turned your bow into a machine gun.");
			}
			handMeta.setLore(lore);
			hand.setItemMeta(handMeta);
		} else
			sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You can not rename your items because you do not have any.");
		return true;
	}
}