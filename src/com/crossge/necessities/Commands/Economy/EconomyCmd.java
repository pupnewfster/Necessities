package com.crossge.necessities.Commands.Economy;

import com.crossge.necessities.Commands.Cmd;
import com.crossge.necessities.Economy.*;
import org.bukkit.command.CommandSender;

public class EconomyCmd extends Cmd {
    protected RankPrices rp = new RankPrices();
    protected CmdPrices cmdp = new CmdPrices();
    protected BalChecks balc = new BalChecks();
    protected Materials mat = new Materials();
    protected Prices pr = new Prices();
    protected Trade tr = new Trade();

    public boolean commandUse(CommandSender sender, String[] args) {
        return false;
    }
}