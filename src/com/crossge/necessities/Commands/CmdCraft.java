package com.crossge.necessities.Commands;

import com.crossge.necessities.Economy.Materials;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

import java.util.HashMap;

public class CmdCraft extends Cmd {
    Materials mat = new Materials();

    public boolean commandUse(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (args.length == 0) {
                p.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must enter an item to craft.");
                return true;
            }
            if (Bukkit.getRecipesFor(new ItemStack(Material.matchMaterial(mat.findItem(args[0])))).isEmpty()) {
                p.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "There are no recipes to craft that item.");
                return true;
            }
            HashMap<ItemStack, Integer> items = new HashMap<>();
            Recipe r = Bukkit.getRecipesFor(new ItemStack(Material.matchMaterial(mat.findItem(args[0])))).get(0);
            if (r instanceof ShapedRecipe) {
                ShapedRecipe s = (ShapedRecipe) r;
                for (Character c : s.getIngredientMap().keySet())
                    if (s.getIngredientMap().get(c) != null)
                        items.put(s.getIngredientMap().get(c), items.containsKey(s.getIngredientMap().get(c)) ? items.get(s.getIngredientMap().get(c)) + 1 : 1);
            } else if (r instanceof ShapelessRecipe)
                for (ItemStack i : ((ShapelessRecipe) r).getIngredientList())
                    items.put(i, items.containsKey(i) ? items.get(i) + 1 : 1);
            for (ItemStack i : items.keySet())
                Bukkit.broadcastMessage(items.get(i) + " " + i.getType().toString());
            Bukkit.broadcastMessage("results in " + r.getResult().getAmount() + " " + r.getResult().getType().toString());
        } else
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must be a player to craft items.");
        return true;
    }
}