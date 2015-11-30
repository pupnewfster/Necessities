package com.crossge.necessities.Hats;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.EulerAngle;

public class BoxTopHat extends Hat {
    public BoxTopHat(Location loc) {
        loc.setYaw(0);
        loc = loc.add(0, 0.5, 0);
        spawn(5, loc);
        this.armorStands.get(0).setHelmet(new ItemStack(Material.CARPET, 1, (short) 15));
        this.armorStands.get(1).setHelmet(new ItemStack(Material.CARPET, 1, (short) 15));
        this.armorStands.get(1).setSmall(true);
        this.armorStands.get(1).setHeadPose(new EulerAngle(Math.toRadians(90), 0, 0));
    }
}