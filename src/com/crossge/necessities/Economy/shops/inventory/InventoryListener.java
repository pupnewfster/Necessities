package com.crossge.necessities.Economy.shops.inventory;

import com.crossge.necessities.Economy.BalChecks;
import com.crossge.necessities.Economy.shops.ItemDef;
import com.crossge.necessities.Economy.shops.ShopDef;
import com.crossge.necessities.Economy.shops.utils.Sounds;
import com.crossge.necessities.Necessities;
import com.google.common.base.Optional;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryListener implements Listener {
    private BalChecks uwotpup = new BalChecks();

    @EventHandler(priority = EventPriority.LOW)
    public void inventoryClicked(InventoryClickEvent e) {
        final Player p = (Player)e.getWhoClicked();
        int clickedSlot = e.getView().convertSlot(e.getRawSlot());
        boolean clickedOwnInventory = clickedSlot != e.getRawSlot();
        if (clickedOwnInventory) {
            Inventory top = p.getOpenInventory().getTopInventory();
            ShopInventory sinventory = ShopInventory.from(top);
            if (sinventory != null) {
                e.setCancelled(true);

                if (e.getCurrentItem() == null)
                    return;

                if (sinventory.getShop().getOwnerUUID().equals(p.getUniqueId())) {
                    Optional<ItemDef> oitem = sinventory.getItem(e.getCurrentItem());

                    if (oitem.isPresent()) {
                        ItemDef item = oitem.get();
                        item.add(1);
                        ItemStack stack = p.getInventory().getItem(clickedSlot);
                        stack.setAmount(stack.getAmount() - 1);
                        p.getInventory().setItem(clickedSlot, stack.getAmount() <= 0 ? null : stack);
                        sinventory.update();
                    }
                }
                return;
            }
            ShopViewInventory s2 = ShopViewInventory.from(p);
            if (s2 != null) {
                e.setCancelled(true);
                return;
            }
        }

        ShopViewInventory view = ShopViewInventory.from(p);
        if (view != null) {
            e.setCancelled(true);
            if (e.getCurrentItem() == null)
                return;

            ItemStack currentItem = e.getCurrentItem();
            if (view.isNextPageButton(currentItem)) {
                view.nextPage();
                return;
            } else if (view.isPreviousPageButton(currentItem)) {
                view.previousPage();
                return;
            } else {
                final ShopDef def = view.getShop(clickedSlot);
                if (def != null) {
                    p.closeInventory();
                    view.end(p);
                    Bukkit.getScheduler().scheduleSyncDelayedTask(Necessities.getInstance(), new Runnable() {
                        public void run() {
                            ShopInventory inventory = ShopInventory.createInventory(def);
                            inventory.openFor(p);
                        }
                    }, 1);
                }
            }
        }

        ShopInventory inventory;
        if ((inventory = ShopInventory.from(e.getInventory())) != null) {
            e.setCancelled(true);
            if (e.getCurrentItem() == null)
                return;

            Optional<ItemDef> oitem = inventory.getItem(e.getCurrentItem());

            if (oitem.isPresent()) {
                ItemDef item = oitem.get();

                if (p.getInventory().firstEmpty() == -1) {
                    //We need to check to see if we can even fit this item

                    boolean foundRoom = false;
                    for (ItemStack i : p.getInventory().getContents()) {
                        if (item.isSame(i)) {
                            Material type = i.getType();

                            if (i.getAmount() < type.getMaxStackSize()) {
                                foundRoom = true;
                                break;
                            }
                        }
                    }

                    if (!foundRoom) {
                        p.sendMessage("" + ChatColor.BOLD + ChatColor.RED + "You have no room in your inventory!");
                        Sounds.playError(p);
                        return;
                    }
                }

                OfflinePlayer o = Bukkit.getOfflinePlayer(p.getUniqueId());
                double balance = uwotpup.balance(p.getUniqueId());
                if (balance >= item.getPrice()) {
                    ItemStack itemStack = item.obtain();
                    if (itemStack == null) {
                        p.sendMessage("" + ChatColor.BOLD + ChatColor.RED + "That item is out of stock!");
                        Sounds.playError(p);
                        inventory.update();
                        return;
                    }
                    OfflinePlayer owner = inventory.getShop().getOfflineOwner();
                    uwotpup.addMoney(owner.getUniqueId(), item.getPrice());

                    p.getInventory().addItem(itemStack);
                    uwotpup.removeMoney(p.getUniqueId(), item.getPrice());

                    inventory.update(); //Send update

                    p.sendMessage("" + ChatColor.BOLD + ChatColor.GREEN + "+ Purchase successful!");
                    Sounds.playSuccess(p);
                } else {
                    p.sendMessage("" + ChatColor.BOLD + ChatColor.RED + "You don't have enough money for that!");
                    Sounds.playError(p);
                }
            }
        }
    }

    public void disable() {
        InventoryClickEvent.getHandlerList().unregister(this);
    }
}
