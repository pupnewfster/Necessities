package com.crossge.necessities.Economy.shops;

import com.crossge.necessities.Economy.shops.inventory.ShopInventory;
import com.crossge.necessities.Economy.shops.utils.ShopPlayer;
import com.google.common.base.Optional;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class ShopDef {
    private String ownerUUID;
    private List<ItemDef> itemsForSale = new ArrayList<ItemDef>();
    private String name;
    private Material icon;
    private ShopInventory shopInventory;

    ShopDef(String name, Player owner) {
        this.name = name;
        this.ownerUUID = owner.getUniqueId().toString();
        this.icon = Material.DIAMOND_ORE;
    }

    ShopDef(String name, List<ItemDef> items, ItemStack icon, String uuid) {
        this.name = name;
        this.itemsForSale = items;
        this.icon = icon.getType();
        this.ownerUUID = uuid;
        this.icon = icon.getType();
    }

    public String getName() {
        return name;
    }

    public List<ItemDef> getItems() {
        return Collections.unmodifiableList(itemsForSale);
    }

    public int getItemCount() {
        return itemsForSale.size();
    }

    public Optional<ShopPlayer> getOwner() {
        Player p = Bukkit.getPlayer(UUID.fromString(ownerUUID));
        if (p == null) {
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(UUID.fromString(ownerUUID));
            if (offlinePlayer == null) {
                return Optional.absent();
            }

            return Optional.of(new ShopPlayer(offlinePlayer));
        }

        return Optional.of(new ShopPlayer(p));
    }

    public Optional<ItemDef> getItem(ItemStack item) {
        for (ItemDef i : itemsForSale) {
            if (i.isSame(item))
                return Optional.of(i);
        }

        return Optional.absent();
    }

    public OfflinePlayer getOfflineOwner() {
        return Bukkit.getOfflinePlayer(UUID.fromString(ownerUUID));
    }

    public boolean isSelling(ItemStack item) {
        for (ItemDef def : itemsForSale) {
            if (def.isSame(item))
                return true;
        }

        return false;
    }

    public void changePrice(ItemStack item, double price) {
        for (ItemDef def : itemsForSale) {
            if (def.isSame(item)) {
                def.setPrice(price);
            }
        }

        if (shopInventory != null)
            shopInventory.update();
    }

    public void sell(ItemStack i, double price) {
        ItemStack item = i.clone();
        item.setAmount(1);
        ItemDef def = ItemDef.newItem(item, price);
        itemsForSale.add(def);

        if (shopInventory != null)
            shopInventory.update();
    }

    public void setIcon(Material icon) {
        this.icon = icon;
    }

    public UUID getOwnerUUID() {
        return UUID.fromString(ownerUUID);
    }

    public ItemStack getIcon() {
        return new ItemStack(icon, 1);
    }

    public void attachInventory(ShopInventory shopInventory) {
        this.shopInventory = shopInventory;
    }
}
