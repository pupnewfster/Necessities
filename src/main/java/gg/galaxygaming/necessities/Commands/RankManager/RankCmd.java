package gg.galaxygaming.necessities.Commands.RankManager;

import gg.galaxygaming.necessities.Commands.Cmd;

interface RankCmd extends Cmd {

    default boolean isPaintballEnabled() {
        return true;
    }
}