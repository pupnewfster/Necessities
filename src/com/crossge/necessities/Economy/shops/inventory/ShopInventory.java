package com.crossge.necessities.Economy.shops.inventory;

import com.crossge.necessities.Economy.shops.ItemDef;
import com.crossge.necessities.Economy.shops.ShopDef;
import com.google.common.base.Optional;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;

public class ShopInventory {
    private static final HashMap<ShopDef, ShopInventory> INSTATNCES = new HashMap<ShopDef, ShopInventory>();
    private static final HashMap<Inventory, ShopInventory> CACHE = new HashMap<Inventory, ShopInventory>();

    private ShopDef shop;
    private Inventory inventory;

    public static ShopInventory createInventory(ShopDef shop) {
        if (INSTATNCES.containsKey(shop))
            return INSTATNCES.get(shop);

        int slotSize = 9;
        while (shop.getItemCount() > slotSize) {
            slotSize += 9;
        }
        slotSize += 9;

        Inventory inventory = Bukkit.createInventory(null, slotSize, shop.getName());

        List<ItemDef> items = shop.getItems();
        for (int i = 0; i < items.size(); i++) {
            inventory.setItem(i, items.get(i).getItemStack());
        }

        ShopInventory shopInventory = new ShopInventory(shop, inventory);
        INSTATNCES.put(shop, shopInventory);
        CACHE.put(inventory, shopInventory);
        shop.attachInventory(shopInventory);

        return shopInventory;
    }

    public static ShopInventory from(Inventory inventory) {
        if (!CACHE.containsKey(inventory))
            return null;
        else
            return CACHE.get(inventory);
    }

    private ShopInventory(ShopDef def, Inventory inventory) {
        this.shop = def;
        this.inventory = inventory;
    }

    public void update() {
        List<ItemDef> items = shop.getItems();
        for (int i = 0; i < inventory.getSize(); i++) {
            if (i < items.size()) {
                ItemStack actual = items.get(i).getItemStack();
                ItemStack old = inventory.getItem(i);

                if (!actual.equals(old)) {
                    inventory.setItem(i, actual);
                }
            } else {
                inventory.clear(i);
            }
        }

        for (HumanEntity p : inventory.getViewers()) {
            if (p instanceof Player) {
                ((Player)p).updateInventory();
            }
        }
    }

    public Optional<ItemDef> getItem(ItemStack item) {
        return shop.getItem(item);
    }

    public ShopDef getShop() {
        return shop;
    }

    public void openFor(Player p) {
        p.openInventory(inventory);
    }
}
