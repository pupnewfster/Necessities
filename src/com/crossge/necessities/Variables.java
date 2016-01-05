package com.crossge.necessities;

import org.bukkit.ChatColor;

public class Variables {
    private ChatColor messages = ChatColor.GOLD, me = ChatColor.DARK_PURPLE, money = ChatColor.AQUA, error = ChatColor.RED, errorMsg = ChatColor.DARK_RED, plugincolor = ChatColor.DARK_RED,
            promoteMsg = ChatColor.GREEN, catalog = ChatColor.BLUE, demoteMsg = ChatColor.RED, objectMsg = ChatColor.RED, guildMsgs = ChatColor.YELLOW, guild = ChatColor.GREEN, ally = ChatColor.DARK_PURPLE,
            enemy = ChatColor.RED, neutral = ChatColor.WHITE, wilderness = ChatColor.DARK_GREEN;

    public ChatColor getWild() {
        return wilderness;
    }

    public ChatColor getGuildMsgs() {
        return guildMsgs;
    }

    public ChatColor getAlly() {
        return ally;
    }

    public ChatColor getEnemy() {
        return enemy;
    }

    public ChatColor getNeutral() {
        return neutral;
    }

    public ChatColor getGuildCol() {
        return guild;
    }

    public ChatColor getMe() {
        return me;
    }

    public ChatColor getObj() {
        return objectMsg;
    }

    public ChatColor getPlugCol() {
        return plugincolor;
    }

    public ChatColor getCatalog() {
        return catalog;
    }

    public ChatColor getDemote() {
        return demoteMsg;
    }

    public ChatColor getPromote() {
        return promoteMsg;
    }

    public ChatColor getMoney() {
        return money;
    }

    public ChatColor getMessages() {
        return messages;
    }

    public ChatColor getEr() {
        return error;
    }

    public ChatColor getErMsg() {
        return errorMsg;
    }
}