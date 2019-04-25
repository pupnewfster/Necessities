package gg.galaxygaming.necessities.Hats;

import java.util.Collection;
import java.util.HashMap;

public enum HatType {
    Design("DESIGN"),
    BoxTopHat("BOX_TOP_HAT"),
    RimmedHat("RIMMED_HAT"),
    StrawHat("STRAW_HAT"),
    TopHat("TOP_HAT"),
    SunHat("SUN_HAT"),
    Fedora("FEDORA"),
    Trippy("TRIPPY"),
    Pot("POT");

    private static final HashMap<String, HatType> nameMap = new HashMap<>();
    private final String name;

    HatType(String name) {
        this.name = name;
    }

    /**
     * @param name The name of the hat to search for.
     * @return The HatType corresponding to the given name.
     */
    public static HatType fromString(String name) {
        return nameMap.get(name.toUpperCase().replaceAll("_", ""));
    }

    /**
     * Gets the name of a hat.
     *
     * @return The string representation of the hat.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Maps the valid types of hats
     */
    public static void mapHats() {
        for (HatType h : values()) {
            nameMap.put(h.getName().replaceAll("_", ""), h);
        }
    }

    /**
     * Gets the valid types of hats.
     *
     * @return A collection of the valid types of hat.
     */
    public static Collection<String> getTypes() {
        return nameMap.keySet();
    }
}