package com.crossge.necessities;

import org.bukkit.ChatColor;

public class Variables {
    private ChatColor messages = ChatColor.GOLD;
    private ChatColor me = ChatColor.DARK_PURPLE;
    private ChatColor money = ChatColor.AQUA;
    private ChatColor error = ChatColor.RED;
    private ChatColor errorMsg = ChatColor.DARK_RED;
    private ChatColor plugincolor = ChatColor.DARK_RED;
    private ChatColor promoteMsg = ChatColor.GREEN;
    private ChatColor catalog = ChatColor.BLUE;
    private ChatColor demoteMsg = ChatColor.RED;
    private ChatColor objectMsg = ChatColor.RED;
    private ChatColor guildMsgs = ChatColor.YELLOW;
    private ChatColor guild = ChatColor.GREEN;
    private ChatColor ally = ChatColor.DARK_PURPLE;
    private ChatColor enemy = ChatColor.RED;
    private ChatColor neutral = ChatColor.WHITE;
    private ChatColor wilderness = ChatColor.DARK_GREEN;

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