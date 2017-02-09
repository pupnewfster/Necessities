package gg.galaxygaming.necessities;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.UUID;

public class Console {
    private boolean toggleChat = false;
    private UUID lastContact = null;

    public String getName() {
        Variables var = Necessities.getVar();
        YamlConfiguration config = Necessities.getInstance().getConfig();
        return var.getMessages() + "Console [" + ChatColor.GREEN + (config.contains("Console.AliveStatus") ? ChatColor.translateAlternateColorCodes('&', config.getString("Console.AliveStatus")) : "Alive") +
                var.getMessages() + "]:" + ChatColor.RESET;
    }

    public void chatToggle() {
        toggleChat = !toggleChat;
    }

    public boolean chatToggled() {
        return toggleChat;
    }

    public UUID getLastContact() {
        return lastContact;
    }

    public void setLastContact(UUID uuid) {
        lastContact = uuid;
    }
}