package com.crossge.necessities.Hats;

import com.crossge.necessities.Necessities;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;

public abstract class Hat {
    protected ArrayList<ArmorStand> armorStands = new ArrayList<>();
    private HatType type;

    public static Hat fromType(HatType type, Location loc) {
        Hat h = null;
        if (type.equals(HatType.BoxTopHat))
            h = new BoxTopHat(loc);
        else if (type.equals(HatType.TopHat))
            h = new TopHat(loc);
        if (h != null)
            h.setType(type);
        return h;
    }

    public void despawn() {
        for (ArmorStand a : this.armorStands)
            a.remove();
    }

    protected void spawn(int num, Location loc) {
        World w = loc.getWorld();
        for (int i = 0; i < num; i ++) {
            ArmorStand a = (ArmorStand) w.spawnEntity(new Location(w, loc.getX(), loc.getY(), loc.getZ()), EntityType.ARMOR_STAND);
            a.setVisible(false);
            a.setGravity(false);
            this.armorStands.add(a);
        }
    }

    public void move(double x, double y, double z, float yaw, float pitch) {
        for (ArmorStand a : this.armorStands) {
            Location loc = a.getLocation().add(x, y, z);
            loc.setYaw(loc.getYaw() + yaw);
            a.teleport(loc);
            //a.setHeadPose(a.getHeadPose().add(Math.toRadians(pitch), 0, 0));
        }
    }

    public void setType(HatType type) {
        this.type = type;
    }
    public HatType getType() {
        return this.type;
    }
}