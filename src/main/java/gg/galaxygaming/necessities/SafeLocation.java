package gg.galaxygaming.necessities;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.World;

public class SafeLocation {
    boolean wouldFall(Location l) {
        double x = l.getX(), y = l.getY(), z = l.getZ();
        l.setY(l.getY() - 1);
        boolean temp = false;
        if (l.getBlock().getType().equals(Material.AIR)) {
            temp = true;
            double xDif = l.getX() - l.getBlockX();
            if (xDif > 0.5)
                l.setX(l.getBlockX() + 1);
            else if (xDif < 0.5)
                l.setX(l.getBlockX() - 1);
            if (!l.getBlock().getType().equals(Material.AIR))
                temp = false;
            double zDif = l.getZ() - l.getBlockZ();
            if (zDif > 0.5)
                l.setZ(l.getBlockZ() + 1);
            else if (zDif < 0.5)
                l.setZ(l.getBlockZ() - 1);
            if (!l.getBlock().getType().equals(Material.AIR))
                temp = false;
            l.setX(x);
            if (!l.getBlock().getType().equals(Material.AIR))
                temp = false;
        }
        //Set values back because it is original object, which means that getSafe needs it unmodified
        //l.setX(x); //Already the same
        l.setY(y);
        l.setZ(z);
        return temp;
    }

    /**
     * Returns a safer location than the given one.
     * @param l The location to check.
     * @return A safer location than the initial one.
     */
    public Location getSafe(Location l) {//TODO: Make it check for air pockets nearby instead of just up
        int maxHeight = l.getWorld().getMaxHeight();
        if (l.getWorld().getEnvironment().equals(World.Environment.NETHER))
            maxHeight = 126;
        //TODO go out ~5 blocks from close to far as it goes up, and then create a private method of this??? because checking about falling does it need the aoe? probably
        //CANNOT just add extra for statements as the overLava would stop working properly
        //TODO: Not check to see if type changed? and just check if it is on a solid block???
        //Compare with https://github.com/drtshock/Essentials/blob/2.x/Essentials/src/com/earth2me/essentials/utils/LocationUtil.java to see about rough logic
        for (int i = l.getBlockY(); i < maxHeight; i++) {
            Material type = new Location(l.getWorld(), l.getX(), i, l.getZ()).getBlock().getType();
            if (!type.isSolid() && !isLeaves(type) && Tag.VALID_SPAWN.isTagged(type)) {
                l = new Location(l.getWorld(), l.getX(), i, l.getZ(), l.getYaw(), l.getPitch());
                break;
            }
        }
        if (wouldFall(l))
            for (int i = l.getBlockY(); i > 0; i--)
                if (new Location(l.getWorld(), l.getX(), i, l.getZ()).getBlock().getType().isSolid()) {
                    l = new Location(l.getWorld(), l.getX(), i + 1, l.getZ(), l.getYaw(), l.getPitch());
                    if (!Tag.VALID_SPAWN.isTagged(l.getBlock().getType()))
                        l = getSafe(l);
                    break;
                }
        return l;
    }

    private boolean isLeaves(Material type) {
        return type.equals(Material.ACACIA_LEAVES) || type.equals(Material.BIRCH_LEAVES) || type.equals(Material.DARK_OAK_LEAVES) || type.equals(Material.JUNGLE_LEAVES) ||
                type.equals(Material.OAK_LEAVES) || type.equals(Material.SPRUCE_LEAVES);
    }
}