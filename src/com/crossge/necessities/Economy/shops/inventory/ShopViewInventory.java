package com.crossge.necessities.Economy.shops.inventory;

import com.crossge.necessities.Economy.shops.ShopDef;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class ShopViewInventory {
    private static HashMap<Player, ShopViewInventory> INSTANCERS = new HashMap<Player, ShopViewInventory>();

    private Inventory inventory;
    private int offset = 0;
    private List<ShopDef> shops;
    private ShopViewInventory() { }

    public static ShopViewInventory create(Player p, List<ShopDef> shops) {
        int slotSize = 9;
        while (shops.size() > slotSize) {
            slotSize += 9;
        }
        slotSize += 9;
        slotSize = Math.min(slotSize, 54);

        Inventory inventory = Bukkit.createInventory(null, slotSize, "Shop Directory");

        for (int i = 0; i < 45; i++) {
            if (i >= shops.size())
                break;
            ItemStack item = shops.get(i).getIcon();

            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(shops.get(i).getName());
            item.setItemMeta(meta);

            inventory.setItem(i, item);
        }

        if (slotSize == 54) {
            ItemStack item = new ItemStack(Material.EMERALD_BLOCK, 1);
            ItemMeta meta = item.getItemMeta();
            meta.setLore(Collections.singletonList(ChatColor.GREEN + "Next Page ->"));
            item.setItemMeta(meta);

            inventory.setItem(53, item);
        }

        ShopViewInventory sinventory = new ShopViewInventory();
        sinventory.inventory = inventory;
        sinventory.shops = shops;
        INSTANCERS.put(p, sinventory);

        return sinventory;
    }

    public static ShopViewInventory from(Player p) {
        if (INSTANCERS.containsKey(p))
            return INSTANCERS.get(p);
        return null;
    }

    public void nextPage() {
        if (offset + 45 >= shops.size())
            return;

        offset += 45;

        updateView();
    }

    public void previousPage() {
        if (offset - 45 < 0)
            return;

        offset -= 45;

        updateView();
    }

    public void updateView() {
        inventory.clear();

        for (int i = offset; i < offset + 45; i++) {
            if (i >= shops.size())
                break;
            ItemStack item = shops.get(i).getIcon();

            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(shops.get(i).getName());
            item.setItemMeta(meta);

            inventory.setItem(i % 45, item);
        }

        if (offset + 45 < shops.size()) {
            ItemStack item = new ItemStack(Material.EMERALD_BLOCK, 1);
            ItemMeta meta = item.getItemMeta();
            meta.setLore(Collections.singletonList(ChatColor.GREEN + "Next Page ->"));
            item.setItemMeta(meta);

            inventory.setItem(53, item);
        }

        ItemStack item = new ItemStack(Material.EMERALD_BLOCK, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setLore(Collections.singletonList(ChatColor.GREEN + "<- Previous Page"));
        item.setItemMeta(meta);

        inventory.setItem(45, item);

        for (HumanEntity p : inventory.getViewers()) {
            if (p instanceof Player) {
                ((Player)p).updateInventory();
            }
        }
    }

    public ShopDef getShop(int slot) {
        int index = offset + slot;

        if (index >= shops.size())
            return null;

        return shops.get(index);
    }

    public void openFor(Player p) {
        p.openInventory(inventory);
    }

    public boolean isNextPageButton(ItemStack stack) {
        if (stack.getItemMeta().getLore() != null) {
            for (String lore : stack.getItemMeta().getLore()) {
                if (lore.equals(ChatColor.GREEN + "Next Page ->"))
                    return true;
            }
        }

        return false;
    }

    public boolean isPreviousPageButton(ItemStack stack) {
        if (stack.getItemMeta().getLore() != null) {
            for (String lore : stack.getItemMeta().getLore()) {
                if (lore.equals(ChatColor.GREEN + "<- Previous Page"))
                    return true;
            }
        }

        return false;
    }

    public void end(Player p) {
        INSTANCERS.remove(p);
        inventory.clear();
        inventory = null;
        shops.clear();
        shops = null;
    }
}