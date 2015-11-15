package com.crossge.necessities.Economy.shops.utils;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class ShopPlayer {
    private Player onlinePlayer;
    private OfflinePlayer offlinePlayer;

    public ShopPlayer(Player player) {
        this.onlinePlayer = player;
    }

    public ShopPlayer(OfflinePlayer offlinePlayer) {
        this.offlinePlayer = offlinePlayer;
    }

    public boolean isOnline() {
        return onlinePlayer != null;
    }

    public boolean isOffline() {
        return offlinePlayer != null;
    }

    public Player getOnlinePlayer() {
        return onlinePlayer;
    }

    public OfflinePlayer getOfflinePlayer() {
        return offlinePlayer;
    }
}