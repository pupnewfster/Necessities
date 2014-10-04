package com.crossge.necessities.Commands;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CmdWrench extends Cmd {
	public boolean commandUse(CommandSender sender, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			ItemStack stick = new ItemStack(Material.STICK, 1);
			ItemMeta wrench = stick.getItemMeta();
			wrench.setDisplayName("Wrench");
			ArrayList<String> lore = new ArrayList<String>();
			lore.add("Wrench");
			wrench.setLore(lore);
			stick.setItemMeta(wrench);
			player.getInventory().addItem(stick);
			player.sendMessage(var.getMessages() + "You now have a wrench.");
		} else
			sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You are not allowed wrenches they could be harmfull to you.");
		return true;
	}
}