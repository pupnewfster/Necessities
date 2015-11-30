package com.crossge.necessities.Hats;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.EulerAngle;

public class Design extends Hat {
    public Design(Location loc) {
        this.trueLoc = loc;
        int turn = 5, turnV = 5;
        /*Location temp = loc.clone().add(0, 0.5, 0);
        for (int i = 0; i < 360 / turn; i++) {
            temp.setYaw(temp.getYaw() + turn);
            spawn(1, temp);
        }
        for (int i = 0; i < 360 / turn; i++) {
            this.armorStands.get(i).setItemInHand(new ItemStack(Material.COAL_BLOCK, 1));
            this.armorStands.get(i).setRightArmPose(new EulerAngle(Math.toRadians(135), Math.toRadians(90), 0));
            this.armorStands.get(i).setSmall(true);
        }*/
        /*spawn(360/turnV, loc);
        for (int i = 0; i < 360 / turnV; i++) {
            this.armorStands.get(360/turn + i).setHelmet(new ItemStack(Material.CARPET, 1, (short) 15));
            this.armorStands.get(360/turn + i).setHeadPose(new EulerAngle(0, Math.toRadians(i * turnV), 0));
        }*/
        /*spawn(360/turnV, loc.clone().add(0, 0.7, 0));
        for (int i = 0; i < 360 / turnV; i++) {
            this.armorStands.get(360/turn + i).setHelmet(new ItemStack(Material.COAL_BLOCK, 1));
            this.armorStands.get(360/turn + i).setSmall(true);
            this.armorStands.get(360/turn + i).setHeadPose(new EulerAngle(Math.toRadians(35), Math.toRadians(i * turnV), 0));
        }*/
        /*double a, r = 0.5;
        for (int i = 0; i < 360 / turn; i++) {
            a = Math.toRadians(i * turn);
            spawn(1, loc.clone().add(r*Math.cos(a), 0.85, r*Math.sin(a)));
        }
        for (int i = 0; i < 360 / turn; i++) {
            this.armorStands.get(i).setHelmet(new ItemStack(Material.HAY_BLOCK, 1));
            this.armorStands.get(i).setSmall(true);
            this.armorStands.get(i).setHeadPose(new EulerAngle(Math.toRadians(90), Math.toRadians(i * turn), 0));
        }*/
    }
}