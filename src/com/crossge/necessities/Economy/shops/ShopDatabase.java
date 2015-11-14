package com.crossge.necessities.Economy.shops;

import com.google.gson.Gson;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ShopDatabase {
    private static final Gson GSON = new Gson();

    private HashMap<String, ArrayList<ShopDef>> shops = new HashMap<String, ArrayList<ShopDef>>();

    private ShopDatabase() { }

    public void save(JavaPlugin owner) throws IOException {
        List<String> ids = new ArrayList<String>();
        ids.addAll(shops.keySet());
        owner.getConfig().set("shop_owners", ids);
        for (String key : shops.keySet()) {
            List<String> names = new ArrayList<String>();
            for (ShopDef shop : shops.get(key)) {
                names.add(shop.getName());
            }
            owner.getConfig().set(key + ".names", names);
            for (ShopDef shop : shops.get(key)) {
                owner.getConfig().set(key + "." + shop.getName() + ".items", shop.getItems());
                owner.getConfig().set(key + "." + shop.getName() + ".icon", shop.getIcon());
            }
        }

        File file = new File(owner.getDataFolder(), "shops.conf");
        if (!owner.getDataFolder().exists())
            owner.getDataFolder().mkdir();

        owner.getConfig().save(file);
    }

    public static ShopDatabase load(JavaPlugin owner) throws IOException, InvalidConfigurationException {
        File file = new File(owner.getDataFolder(), "shops.conf");
        if (!file.exists())
            return new ShopDatabase();

        ShopDatabase database = new ShopDatabase();
        owner.getConfig().load(file);

        List<String> owners = owner.getConfig().getStringList("shop_owners");

        for (String o : owners) {
            List<String> shopNames = owner.getConfig().getStringList(o + ".names");
            ArrayList<ShopDef> shops = new ArrayList<ShopDef>();
            for (String name : shopNames) {
                List<ItemDef> items = (List<ItemDef>) owner.getConfig().getList(o + "." + name + ".items");
                ItemStack icon = owner.getConfig().getItemStack(o + "." + name + ".icon");

                ShopDef shop = new ShopDef(name, items, icon, o);
                shops.add(shop);
            }

            database.shops.put(o, shops);
        }

        return database;
    }

    public ShopDef createNewShop(Player p, String name) {
        ShopDef shop = new ShopDef(name, p);

        String uuid = p.getUniqueId().toString();

        if (!shops.containsKey(uuid)) {
            ArrayList<ShopDef> temp = new ArrayList<ShopDef>();
            temp.add(shop);
            shops.put(uuid, temp);
        } else {
            shops.get(uuid).add(shop);
        }

        return shop;
    }

    public List<ShopDef> getShops(List<String> uuids) {
        ArrayList<ShopDef> toReturn = new ArrayList<ShopDef>();
        for (String s : uuids) {
            if (shops.containsKey(s))
                toReturn.addAll(shops.get(s));
        }

        return toReturn;
    }

    public List<ShopDef> getShops(Player p) {
        String uuid = p.getUniqueId().toString();

        if (!shops.containsKey(uuid))
            return new ArrayList<ShopDef>();

        return shops.get(uuid);
    }

    public boolean hasShopName(Player p, String name) {
        List<ShopDef> shops = getShops(p);
        for (ShopDef shop : shops) {
            if (shop.getName().equalsIgnoreCase(name))
                return true;
        }

        return false;
    }
}
