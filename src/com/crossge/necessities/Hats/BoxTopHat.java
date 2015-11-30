package com.crossge.necessities.Hats;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.EulerAngle;

public class BoxTopHat extends Hat {
    public BoxTopHat(Location loc) {
        this.trueLoc = loc;
        spawn(1, loc);
        this.armorStands.get(0).setHelmet(new ItemStack(Material.CARPET, 1, (short) 15));
        spawn(1, loc.clone().add(0, 0.85, 0.24));
        this.armorStands.get(1).setHelmet(new ItemStack(Material.CARPET, 1, (short) 15));
        this.armorStands.get(1).setSmall(true);
        this.armorStands.get(1).setHeadPose(new EulerAngle(Math.toRadians(90), 0, Math.toRadians(90)));
        spawn(1, loc.clone().add(0.19, 0.85, 0));
        this.armorStands.get(2).setHelmet(new ItemStack(Material.CARPET, 1, (short) 15));
        this.armorStands.get(2).setSmall(true);
        this.armorStands.get(2).setHeadPose(new EulerAngle(Math.toRadians(90), Math.toRadians(90), 0));
        spawn(1, loc.clone().add(0, 0.85, -0.2));
        this.armorStands.get(3).setHelmet(new ItemStack(Material.CARPET, 1, (short) 15));
        this.armorStands.get(3).setSmall(true);
        this.armorStands.get(3).setHeadPose(new EulerAngle(Math.toRadians(90), 0, Math.toRadians(90)));
        spawn(1, loc.clone().add(-0.22, 0.85, 0));
        this.armorStands.get(4).setHelmet(new ItemStack(Material.CARPET, 1, (short) 15));
        this.armorStands.get(4).setSmall(true);
        this.armorStands.get(4).setHeadPose(new EulerAngle(Math.toRadians(90), Math.toRadians(90), 0));
        spawn(1, loc.clone().add(0, 1.1, 0));
        this.armorStands.get(5).setHelmet(new ItemStack(Material.CARPET, 1, (short) 15));
        this.armorStands.get(5).setSmall(true);
    }
}