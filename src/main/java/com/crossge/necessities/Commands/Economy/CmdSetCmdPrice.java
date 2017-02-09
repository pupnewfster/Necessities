package com.crossge.necessities.Commands.Economy;

import com.crossge.necessities.Economy.CmdPrices;
import com.crossge.necessities.Economy.Economy;
import com.crossge.necessities.Necessities;
import com.crossge.necessities.Utils;
import com.crossge.necessities.Variables;
import org.bukkit.command.CommandSender;

public class CmdSetCmdPrice implements EconomyCmd {
    public boolean commandUse(CommandSender sender, String[] args) {
        Variables var = Necessities.getInstance().getVar();
        if (args.length >= 0) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "This command is currently disabled.");
            return true;
        }
        if (args.length < 3) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Format requires you to enter the command you want to sell the price and the rank required.");
            return true;
        }
        String cmd = Utils.capFirst(args[0]);
        CmdPrices cmdp = Necessities.getInstance().getCommandPrices();
        if (!Utils.legalDouble(args[1])) {
            if (args[1].equalsIgnoreCase("null")) {
                cmdp.removeCommand(cmd);
                sender.sendMessage(var.getMessages() + cmd + " can no longer be bought.");
                return true;
            }
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must enter a valid price.");
            return true;
        }
        String price = Utils.roundTwoDecimals(Double.parseDouble(args[1]));
        String rank = args[2];
        cmdp.addCommand(rank, cmd, price);
        sender.sendMessage(var.getMessages() + "Added " + var.getObj() + cmd + var.getMessages() + " to the commands of rank " + var.getObj() + rank + var.getMessages() + " at the price of " +
                var.getMoney() + Economy.format(Double.parseDouble(price)));
        return true;
    }
}