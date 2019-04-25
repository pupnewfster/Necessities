package gg.galaxygaming.necessities.Commands.WorldManager;

import gg.galaxygaming.necessities.Commands.Cmd;

public interface WorldCmd extends Cmd {

    default boolean isPaintballEnabled() {
        return true;
    }
}