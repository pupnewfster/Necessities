package com.crossge.necessities;

import org.bukkit.entity.Player;

import java.util.HashMap;

public class AntiCombatLog {
    public static HashMap<Player, Long> inCombat = new HashMap<Player, Long>();
    Variables var = new Variables();

    public void addToCombat(Player p, Player other) {
        if (p.hasPermission("Necessities.canCombatLog") || other.hasPermission("Necessities.canCombatLog"))
            return;
        long time = System.currentTimeMillis();
        if (!inCombat.containsKey(p))
            p.sendMessage(var.getMessages() + "You are now in combat.");
        if (!inCombat.containsKey(other))
            other.sendMessage(var.getMessages() + "You are now in combat.");
        inCombat.put(p, time);
        inCombat.put(other, time);
    }

    public boolean contains(Player p) {
        return inCombat.containsKey(p);
    }

    public void removeFromCombat(Player p) {
        long time = System.currentTimeMillis();
        if (inCombat.containsKey(p) && (time - inCombat.get(p)) / 1000.0 >= 9) {
            inCombat.remove(p);
            p.sendMessage(var.getMessages() + "You are no longer in combat.");
        }
    }
}