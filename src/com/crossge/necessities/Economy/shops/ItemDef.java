package com.crossge.necessities.Economy.shops;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemDef implements ConfigurationSerializable {
    private ItemStack item;
    private int count;
    private double price;

    public static ItemDef newItem(ItemStack item, double price) {
        ItemMeta meta = item.getItemMeta();
        List<String> lore = meta.getLore();
        if (lore == null)
            lore = new ArrayList<String>();

        lore.add("" + ChatColor.BOLD + ChatColor.DARK_PURPLE + "Price: " + ChatColor.RESET + price);
        meta.setLore(lore);
        item.setItemMeta(meta);

        return new ItemDef(item, price);
    }

    private ItemDef(ItemStack item, double price) {
        this.price = price;
        this.item = item;

        this.count = item.getAmount();
    }

    public ItemDef(Map<String, Object> map) {
        this.count = (Integer)map.get("count");
        this.price = (Double)map.get("price");
        this.item = (ItemStack) map.get("item");
    }

    public ItemStack obtain() {
        if (count <= 0) {
            if (item != null && item.getAmount() != 1) {
                item.setAmount(1);
            }
            return null;
        }

        if (item == null)
            item = getItemStack(); //Force create an itemstack

        ItemStack newItem = new ItemStack(item);
        newItem.setAmount(1);

        ItemMeta meta = newItem.getItemMeta();
        List<String> lore = meta.getLore();
        lore.remove("" + ChatColor.BOLD + ChatColor.DARK_PURPLE + "Price: " + ChatColor.RESET + price);
        lore.remove("" + ChatColor.BOLD + ChatColor.RED + "OUT OF STOCK");
        meta.setLore(lore);
        newItem.setItemMeta(meta);

        sub(1);

        return newItem;
    }

    public boolean hasStock() {
        return count > 0;
    }

    public ItemStack getItemStack() {
        return item;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        double oprice = this.price;
        this.price = price;

        if (item != null) {
            ItemMeta meta = item.getItemMeta();
            List<String> lore = meta.getLore();
            if (lore != null) {
                lore.remove("" + ChatColor.BOLD + ChatColor.DARK_PURPLE + "Price: " + ChatColor.RESET + oprice);
                lore.add("" + ChatColor.BOLD + ChatColor.DARK_PURPLE + "Price: " + ChatColor.RESET + price);
                meta.setLore(lore);
                item.setItemMeta(meta);
            }
        }
    }

    public void add(int count) {
        this.count += count;
        if (item != null) {
            item.setAmount(this.count);

            ItemMeta meta = item.getItemMeta();
            List<String> lore = meta.getLore();
            if (lore != null) {
                lore.remove("" + ChatColor.BOLD + ChatColor.RED + "OUT OF STOCK");
                meta.setLore(lore);
                item.setItemMeta(meta);
            }
        }
    }

    public void sub(int count) {
        this.count -= count;
        if (item != null) {
            if (this.count == 0) {
                ItemMeta meta = item.getItemMeta();
                List<String> lore = meta.getLore();
                if (lore == null) {
                    lore = new ArrayList<String>();
                    lore.add("" + ChatColor.BOLD + ChatColor.DARK_PURPLE + "Price: " + ChatColor.RESET + price);
                }
                lore.add("" + ChatColor.BOLD + ChatColor.RED + "OUT OF STOCK");

                meta.setLore(lore);
                item.setItemMeta(meta);
                item.setAmount(1);
            } else {
                item.setAmount(this.count);
            }
        }
    }

    public Material getType() {
        return item.getType();
    }

    public boolean isSame(ItemStack itemStack) {
        return item.getType().equals(itemStack.getType()) && item.getData().equals(itemStack.getData());
    }

    public Map<String, Object> serialize() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("item", item);
        map.put("count", count);
        map.put("price", price);

        return map;
    }

    public static ItemDef deserialize(Map<String, Object> map) {
        return new ItemDef(map);
    }

    public static ItemDef valueOf(Map<String, Object> map) {
        return new ItemDef(map);
    }
}
