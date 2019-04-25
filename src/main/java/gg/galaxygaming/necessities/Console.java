package gg.galaxygaming.necessities;

import java.util.UUID;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

public class Console {

    private boolean toggleChat;
    private UUID lastContact;

    /**
     * Retrieves the name of the console.
     *
     * @return The name of the console.
     */
    public String getName() {
        Variables var = Necessities.getVar();
        YamlConfiguration config = Necessities.getInstance().getConfig();
        return var.getMessages() + "Console [" + ChatColor.GREEN + (config.contains("Console.AliveStatus") ? ChatColor
              .translateAlternateColorCodes('&', config.getString("Console.AliveStatus")) : "Alive") +
              var.getMessages() + "]:" + ChatColor.RESET;
    }

    /**
     * Toggles the say command.
     */
    public void chatToggle() {
        this.toggleChat = !this.toggleChat;
    }

    /**
     * Checks if chat is toggled.
     *
     * @return True if chat is toggled, false otherwise.
     */
    public boolean chatToggled() {
        return this.toggleChat;
    }

    /**
     * Retrieves the player who the console last messaged, or was messaged by.
     *
     * @return The uuid of the player who the console last messaged, or was messaged by.
     */
    public UUID getLastContact() {
        return this.lastContact;
    }

    /**
     * Sets the player the console last had contact with.
     *
     * @param uuid The uuid of the player to set the last contact as.
     */
    public void setLastContact(UUID uuid) {
        this.lastContact = uuid;
    }
}