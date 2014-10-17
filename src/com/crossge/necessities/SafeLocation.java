package com.crossge.necessities;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

public class SafeLocation {
    public boolean wouldFall(Location l) {
        double x = l.getX();
        double y = l.getY();
        double z = l.getZ();
        l.setY(l.getY() - 1);
        boolean temp = false;
        if (l.getBlock().getType().equals(Material.AIR)) {
            temp = true;
            if (l.getX() - l.getBlockX() > 0.5)
                l.setX(l.getBlockX() + 1);
            else if (l.getX() - l.getBlockX() < 0.5)
                l.setX(l.getBlockX() - 1);
            if (!l.getBlock().getType().equals(Material.AIR))
                temp = false;
            if (l.getZ() - l.getBlockZ() > 0.5)
                l.setZ(l.getBlockZ() + 1);
            else if (l.getZ() - l.getBlockZ() < 0.5)
                l.setZ(l.getBlockZ() - 1);
            if (!l.getBlock().getType().equals(Material.AIR))
                temp = false;
            l.setX(x);
            if (!l.getBlock().getType().equals(Material.AIR))
                temp = false;
        }
        l.setX(x);
        l.setY(y);
        l.setZ(z);
        return temp;
    }

    public Location getSafe(Location l) {//TODO: Make it check for air pockets nearby instead of just up
        int maxHeight = l.getWorld().getMaxHeight();
        if (l.getWorld().getEnvironment().equals(World.Environment.NETHER))
            maxHeight = 126;
        boolean overLava = false;
        for (int i = l.getBlockY(); i < maxHeight; i++) {
            Block b = (new Location(l.getWorld(), l.getX(), i, l.getZ())).getBlock();
            if (b.getType().equals(Material.LAVA) || b.getType().equals(Material.STATIONARY_LAVA))
                overLava = true;
            else if ((b.getType().isSolid() || b.getType().equals(Material.LEAVES) || b.getType().equals(Material.LEAVES_2)) && overLava)
                overLava = false;
            if (!b.getType().isSolid() && !b.getType().equals(Material.LEAVES) && !b.getType().equals(Material.LEAVES_2) && !overLava) {
                l = new Location(l.getWorld(), l.getX(), i, l.getZ(), l.getYaw(), l.getPitch());
                break;
            }
        }
        if (wouldFall(l))
            for (int i = l.getBlockY(); i > 0; i--)
                if ((new Location(l.getWorld(), l.getX(), i, l.getZ())).getBlock().getType().isSolid()) {
                    l = new Location(l.getWorld(), l.getX(), i + 1, l.getZ(), l.getYaw(), l.getPitch());
                    if (l.getBlock().getType().equals(Material.LAVA) || l.getBlock().getType().equals(Material.STATIONARY_LAVA))
                        l = getSafe(l);
                    break;
                }
        return l;
    }
}