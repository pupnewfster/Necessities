package gg.galaxygaming.necessities;

import java.util.HashMap;
import org.bukkit.entity.Player;

class AntiCombatLog {

    private final HashMap<Player, Long> inCombat = new HashMap<>();

    void addToCombat(Player p, Player other) {
        if (p.hasPermission("Necessities.canCombatLog") || other.hasPermission("Necessities.canCombatLog")) {
            return;
        }
        long time = System.currentTimeMillis();
        Variables var = Necessities.getVar();
        if (!this.inCombat.containsKey(p)) {
            p.sendMessage(var.getMessages() + "You are now in combat.");
        }
        if (!this.inCombat.containsKey(other)) {
            other.sendMessage(var.getMessages() + "You are now in combat.");
        }
        this.inCombat.put(p, time);
        this.inCombat.put(other, time);
    }

    boolean contains(Player p) {
        return this.inCombat.containsKey(p);
    }

    void removeFromCombat(Player p) {
        long time = System.currentTimeMillis();
        if (this.inCombat.containsKey(p) && (time - this.inCombat.get(p)) / 1000.0 >= 9) {
            this.inCombat.remove(p);
            p.sendMessage(Necessities.getVar().getMessages() + "You are no longer in combat.");
        }
    }
}