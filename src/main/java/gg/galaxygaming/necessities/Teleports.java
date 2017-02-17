package gg.galaxygaming.necessities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class Teleports {
    private final HashMap<UUID, ArrayList<String>> teleportRequests = new HashMap<>();

    void removeRequests(UUID uuid) {
        teleportRequests.remove(uuid);
        for (UUID u : teleportRequests.keySet())
            if (hasRequestFrom(u, uuid))
                removeRequestFrom(u, uuid);
    }

    /**
     * Adds a teleport request.
     * @param to   The uuid of the player to add the request to.
     * @param from The player who sent the request.
     */
    public void addRequest(UUID to, String from) {
        if (teleportRequests.containsKey(to)) {
            if (teleportRequests.get(to).contains(from.split(" ")[0] + " toMe"))
                teleportRequests.get(to).remove(from.split(" ")[0] + " toMe");
            else if (teleportRequests.get(to).contains(from.split(" ")[0] + " toThem"))
                teleportRequests.get(to).remove(from.split(" ")[0] + " toThem");
        } else
            teleportRequests.put(to, new ArrayList<>());
        teleportRequests.get(to).add(from);
    }

    /**
     * Retrieves the uuid of the last person who requested to teleport to or summon the specified player.
     * @param uuid The uuid of the player to check.
     * @return The uuid of the last person who requested to teleport to or summon the specified player.
     */
    public UUID lastRequest(UUID uuid) {
        return !teleportRequests.containsKey(uuid) ? null : UUID.fromString(teleportRequests.get(uuid).get(teleportRequests.get(uuid).size() - 1).split(" ")[0]);
    }

    /**
     * Gets if a teleport request was for teleporting to or for summoning.
     * @param uuid The uuid of the player accepting the request.
     * @param from The uuid of the player who sent the request.
     * @return "toMe" if it is a teleport request, "toThem" if it is a summon request.
     */
    public String getRequestType(UUID uuid, UUID from) {
        if (!teleportRequests.containsKey(uuid))
            return null;
        if (teleportRequests.get(uuid).contains(from.toString() + " toMe"))
            return "toMe";
        else if (teleportRequests.get(uuid).contains(from.toString() + " toThem"))
            return "toThem";
        return null;
    }

    /**
     * Checks if the specified player has a teleport request from the other player.
     * @param uuid The uuid of the player to check if they have a request from the other player.
     * @param from The uuid of the player who sent the request if there is one.
     * @return True if there is a request, false otherwise.
     */
    public boolean hasRequestFrom(UUID uuid, UUID from) {
        return teleportRequests.containsKey(uuid) && (teleportRequests.get(uuid).contains(from.toString() + " toMe") || teleportRequests.get(uuid).contains(from.toString() + " toThem"));
    }

    /**
     * Removes a teleport request paring between two players.
     * @param uuid The uuid of the player who declined the request.
     * @param from The uuid of the player who sent the request.
     */
    public void removeRequestFrom(UUID uuid, UUID from) {
        if (teleportRequests.containsKey(uuid)) {
            teleportRequests.get(uuid).remove(from.toString() + " toMe");
            teleportRequests.get(uuid).remove(from.toString() + " toThem");
            if (teleportRequests.get(uuid).isEmpty())
                teleportRequests.remove(uuid);
        }
    }
}