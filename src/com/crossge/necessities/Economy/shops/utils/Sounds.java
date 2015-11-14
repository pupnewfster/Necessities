package com.crossge.necessities.Economy.shops.utils;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class Sounds {

    public static void playSuccess(Player target) {
        target.playSound(target.getLocation(), Sound.ARROW_HIT, 10, 1);
    }

    public static void playError(Player target) {
        target.playSound(target.getLocation(), Sound.CHICKEN_HURT, 10, 1);
    }
}
