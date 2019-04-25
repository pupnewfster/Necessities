package gg.galaxygaming.necessities.Material;

import java.lang.reflect.Field;
import net.minecraft.server.v1_14_R1.Item;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_14_R1.inventory.CraftItemStack;

/**
 * A class to help quantify different Materials
 */
public class MaterialHelper {

    /**
     * Checks if a given material is a wooden door.
     *
     * @param type The material to check.
     * @return True if type is a wooden door, false otherwise.
     */
    public static boolean isWoodDoor(Material type) {
        return type.equals(Material.OAK_DOOR) || type.equals(Material.DARK_OAK_DOOR) || type
              .equals(Material.ACACIA_DOOR) || type.equals(Material.BIRCH_DOOR) ||
              type.equals(Material.JUNGLE_DOOR) || type.equals(Material.SPRUCE_DOOR);
    }

    /**
     * Checks if a given material is a wooden trapdoor.
     *
     * @param type The material to check.
     * @return True if type is a wooden trapdoor, false otherwise.
     */
    public static boolean isWoodTrapdoor(Material type) {
        return type.equals(Material.ACACIA_TRAPDOOR) || type.equals(Material.BIRCH_TRAPDOOR) ||
              type.equals(Material.DARK_OAK_TRAPDOOR) || type.equals(Material.JUNGLE_TRAPDOOR) ||
              type.equals(Material.OAK_TRAPDOOR) || type.equals(Material.SPRUCE_TRAPDOOR);
    }

    /**
     * Checks if a given material is a sign.
     *
     * @param type The material to check.
     * @return True if type is a sign, false otherwise.
     */
    public static boolean isSign(Material type) {
        return type.equals(Material.ACACIA_SIGN) || type.equals(Material.ACACIA_WALL_SIGN) ||
              type.equals(Material.BIRCH_SIGN) || type.equals(Material.BIRCH_WALL_SIGN) ||
              type.equals(Material.DARK_OAK_SIGN) || type.equals(Material.DARK_OAK_WALL_SIGN) ||
              type.equals(Material.JUNGLE_SIGN) || type.equals(Material.JUNGLE_WALL_SIGN) ||
              type.equals(Material.OAK_SIGN) || type.equals(Material.OAK_WALL_SIGN) ||
              type.equals(Material.SPRUCE_SIGN) || type.equals(Material.SPRUCE_WALL_SIGN);
    }

    /**
     * Checks if a given material is a container.
     *
     * @param type The material to check.
     * @return True if type is a container, false otherwise.
     */
    public static boolean isContainer(Material type) {
        return type.equals(Material.CHEST) || type.equals(Material.TRAPPED_CHEST) || type.equals(Material.FURNACE) ||
              type.equals(Material.HOPPER) || type.equals(Material.DISPENSER) || type.equals(Material.DROPPER) ||
              type.equals(Material.BREWING_STAND) || type.equals(Material.BARREL) || type.equals(Material.SMOKER) ||
              type.equals(Material.BLAST_FURNACE);
    }

    /**
     * Checks if a given material is a fence gate.
     *
     * @param type The material to check.
     * @return True if type is a fence gate, false otherwise.
     */
    public static boolean isFenceGate(Material type) {
        return type.equals(Material.ACACIA_FENCE_GATE) || type.equals(Material.BIRCH_FENCE_GATE) ||
              type.equals(Material.DARK_OAK_FENCE_GATE) || type.equals(Material.OAK_FENCE_GATE) ||
              type.equals(Material.JUNGLE_FENCE_GATE) || type.equals(Material.SPRUCE_FENCE_GATE);
    }

    /**
     * Checks if a given material is a button.
     *
     * @param type The material to check.
     * @return True if type is a button, false otherwise.
     */
    public static boolean isButton(Material type) {
        return type.equals(Material.BIRCH_BUTTON) || type.equals(Material.ACACIA_BUTTON) ||
              type.equals(Material.DARK_OAK_BUTTON) || type.equals(Material.JUNGLE_BUTTON) ||
              type.equals(Material.OAK_BUTTON) || type.equals(Material.SPRUCE_BUTTON) ||
              type.equals(Material.STONE_BUTTON);
    }

    /**
     * Checks if a given material is a pressure plate.
     *
     * @param type The material to check.
     * @return True if type is a pressure plate, false otherwise.
     */
    public static boolean isPressurePlate(Material type) {
        return type.equals(Material.ACACIA_PRESSURE_PLATE) || type.equals(Material.BIRCH_PRESSURE_PLATE) ||
              type.equals(Material.DARK_OAK_PRESSURE_PLATE) || type.equals(Material.JUNGLE_PRESSURE_PLATE) ||
              type.equals(Material.OAK_PRESSURE_PLATE) || type.equals(Material.STONE_PRESSURE_PLATE) ||
              type.equals(Material.LIGHT_WEIGHTED_PRESSURE_PLATE) || type.equals(Material.HEAVY_WEIGHTED_PRESSURE_PLATE)
              || type.equals(Material.SPRUCE_PRESSURE_PLATE);
    }

    /**
     * Checks if a given material is a fence gate.
     *
     * @param type The material to check.
     * @return True if type is a fence gate, false otherwise.
     */
    public static boolean isBed(Material type) {
        return type.equals(Material.BLACK_BED) || type.equals(Material.BLUE_BED) || type.equals(Material.BROWN_BED) ||
              type.equals(Material.CYAN_BED) || type.equals(Material.GRAY_BED) || type.equals(Material.GREEN_BED) ||
              type.equals(Material.LIGHT_BLUE_BED) || type.equals(Material.LIGHT_GRAY_BED) ||
              type.equals(Material.LIME_BED) || type.equals(Material.MAGENTA_BED) || type.equals(Material.ORANGE_BED) ||
              type.equals(Material.PINK_BED) || type.equals(Material.PURPLE_BED) || type.equals(Material.RED_BED) ||
              type.equals(Material.WHITE_BED) || type.equals(Material.YELLOW_BED);
    }

    /**
     * Checks if a given material is a shulker box.
     *
     * @param type The material to check.
     * @return True if type is a shulker box, false otherwise.
     */
    public static boolean isShulker(Material type) {
        return type.equals(Material.WHITE_SHULKER_BOX) || type.equals(Material.ORANGE_SHULKER_BOX) ||
              type.equals(Material.MAGENTA_SHULKER_BOX) || type.equals(Material.LIGHT_BLUE_SHULKER_BOX) ||
              type.equals(Material.YELLOW_SHULKER_BOX) || type.equals(Material.LIME_SHULKER_BOX) ||
              type.equals(Material.PINK_SHULKER_BOX) || type.equals(Material.GRAY_SHULKER_BOX) ||
              type.equals(Material.LIGHT_GRAY_SHULKER_BOX) || type.equals(Material.CYAN_SHULKER_BOX) ||
              type.equals(Material.PURPLE_SHULKER_BOX) || type.equals(Material.BLUE_SHULKER_BOX) ||
              type.equals(Material.BROWN_SHULKER_BOX) || type.equals(Material.GREEN_SHULKER_BOX) ||
              type.equals(Material.RED_SHULKER_BOX) || type.equals(Material.BLACK_SHULKER_BOX);
    }

    /**
     * Changes the max stack size the given material.
     *
     * @param material The material to change the stack size of.
     * @param size The new max stack size for the material.
     */
    public static void setStackSize(gg.galaxygaming.necessities.Material.Material material, int size) {
        Item item = CraftItemStack.asNMSCopy(material.toItemStack(1)).getItem();
        try {
            Field maxStackSize = Item.class.getDeclaredField("maxStackSize");
            maxStackSize.setAccessible(true);
            maxStackSize.setInt(item, size);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}