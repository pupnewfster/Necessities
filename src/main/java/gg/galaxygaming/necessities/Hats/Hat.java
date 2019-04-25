package gg.galaxygaming.necessities.Hats;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

public abstract class Hat {

    final List<ArmorStand> armorStands = new ArrayList<>();
    Location trueLoc;
    private HatType type;
    //private double x1, y1, z1 = 1, pitch, yaw;

    /**
     * Gets a new instance of a hat at the specified location based on the given type.
     *
     * @param type The type of the hat to create.
     * @param location The location to create the hat at.
     * @return A new instance of a hate of the specified type at the given location.
     */
    public static Hat fromType(HatType type, Location location) {
        Hat h = null;
        location = location.clone().add(0, 0.5, 0);
        if (type.equals(HatType.BoxTopHat)) {
            h = new BoxTopHat(location);
        } else if (type.equals(HatType.TopHat)) {
            h = new TopHat(location);
        } else if (type.equals(HatType.StrawHat)) {
            h = new StrawHat(location);
        } else if (type.equals(HatType.Fedora)) {
            h = new Fedora(location);
        } else if (type.equals(HatType.Pot)) {
            h = new Pot(location);
        } else if (type.equals(HatType.RimmedHat)) {
            h = new RimmedHat(location);
        } else if (type.equals(HatType.Trippy)) {
            h = new Trippy(location);
        } else if (type.equals(HatType.SunHat)) {
            h = new SunHat(location);
        } else if (type.equals(HatType.Design)) {
            h = new Design(location);
        }
        if (h != null) {
            h.setType(type);
        }
        return h;
    }

    /**
     * Despawns the hat.
     */
    public void despawn() {
        this.armorStands.forEach(Entity::remove);
    }

    void spawn(int num, Location loc) {
        World w = loc.getWorld();
        for (int i = 0; i < num; i++) {
            ArmorStand a = (ArmorStand) w
                  .spawnEntity(new Location(w, loc.getX(), loc.getY(), loc.getZ()), EntityType.ARMOR_STAND);
            a.setVisible(false);
            a.setGravity(false);
            a.setMarker(true);
            this.armorStands.add(a);
        }
    }

    void spawnYaw(int num, Location loc) {
        World w = loc.getWorld();
        for (int i = 0; i < num; i++) {
            ArmorStand a = (ArmorStand) w
                  .spawnEntity(new Location(w, loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), 0),
                        EntityType.ARMOR_STAND);
            a.setVisible(false);
            a.setGravity(false);
            a.setMarker(true);
            this.armorStands.add(a);
        }
    }

    /**
     * Moves the hat a specified amount.
     *
     * @param x The amount in the x direction to move the hat.
     * @param y The amount in the y direction to move the hat.
     * @param z The amount in the z direction to move the hat.
     * @param yaw The yaw to set the hat's direction to.
     * @param pitch The pitch to set the hat's direction to.
     */
    public void move(double x, double y, double z, float yaw, float pitch) {
        /*this.pitch = ((90 - pitch) * Math.PI) / 180;
        this.yaw  = ((yaw + 90 + 180) * Math.PI) / 180;
        double x2 = this.x1, y2 = this.y1, z2 = this.z1;
        this.y1 = Math.sin(Math.toRadians(this.pitch)) * Math.sin(Math.toRadians(this.yaw));
        this.x1 = Math.sin(Math.toRadians(this.pitch)) * Math.cos(Math.toRadians(this.yaw));
        this.z1 = Math.cos(Math.toRadians(this.pitch));
        int xChange = dir.getX() >= 0 ? 1 : -1;
        int zChange = dir.getZ() >= 0 ? 1 : -1;*/
        this.trueLoc = this.trueLoc.clone().add(x, y, z);
        //double ang = Math.toRadians(yaw);//Math.atan2(rx, rz));
        for (ArmorStand a : this.armorStands) {
            //Location loc = a.getLocation().add(x + (x2 - this.x1)*xChange, y + y2 - this.y1, z + (z2 - this.z1)*zChange);
            Location loc = a.getLocation().clone().add(x, y, z);
            //loc.setYaw(loc.getYaw() + yaw);
            a.teleport(loc);
            //a.setHeadPose(a.getHeadPose().add(Math.toRadians(pitchChange*xChange*zChange*-1), 0, 0));
        }
    }

    private void setType(HatType type) {
        this.type = type;
    }

    /**
     * Retrieves the type of the hat.
     *
     * @return The type of the current hat.
     */
    public HatType getType() {
        return this.type;
    }
}