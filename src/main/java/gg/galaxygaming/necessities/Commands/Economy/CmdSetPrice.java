package gg.galaxygaming.necessities.Commands.Economy;

import gg.galaxygaming.necessities.Economy.Economy;
import gg.galaxygaming.necessities.Economy.Material;
import gg.galaxygaming.necessities.Economy.Prices;
import gg.galaxygaming.necessities.Necessities;
import gg.galaxygaming.necessities.Utils;
import gg.galaxygaming.necessities.Variables;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdSetPrice implements EconomyCmd {
    public boolean commandUse(CommandSender sender, String[] args) {
        Variables var = Necessities.getVar();
        if (args.length < 2) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Format requires you enter the item and price for it and whether buying or selling price.");
            return true;
        }
        Material mat;
        Prices pr = Necessities.getPrices();
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 2) {
                if (!Utils.legalDouble(args[0]) && !args[0].equalsIgnoreCase("null")) {
                    sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must enter a the price you want to set the item at.");
                    return true;
                }
                mat = Material.fromString(player.getInventory().getItemInMainHand().getType().toString());
                if (mat == null) {
                    player.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "That item does not exist");
                    return true;
                }
                if (!mat.isTool()) {
                    short data = player.getInventory().getItemInMainHand().getDurability();
                    mat = Material.fromData(data != 0 ? mat.getParent() : mat, data);
                }
                String file;
                if (args[1].equalsIgnoreCase("buy") || args[1].equalsIgnoreCase("sell"))
                    file = args[1].toLowerCase();
                else {
                    player.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Input either sell or buy");
                    return true;
                }
                if (args[0].equalsIgnoreCase("null")) {
                    pr.setPrice(file, mat.getName(), args[0]);
                    player.sendMessage(var.getObj() + mat.getFriendlyName(2) + var.getMessages() + " can no longer be " + (args[1].equalsIgnoreCase("buy") ? "bought" : "sold"));
                } else {
                    pr.setPrice(file, mat.getName(), Utils.roundTwoDecimals(Double.parseDouble(args[0])));
                    player.sendMessage(var.getObj() + Utils.ownerShip(mat.getFriendlyName(2)) + var.getMessages() + " " + (args[1].equalsIgnoreCase("buy") ? "buy" : "sell") +
                            " price was set to " + var.getMoney() + Economy.format(Double.parseDouble(args[0])));
                }
                return true;
            }
        }
        if (args.length < 3) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Format requires you enter the item and price for it and whether buying or selling price.");
            return true;
        }
        String[] temp = args[0].split(":");
        String itemName = args[0];
        short data = 0;
        if (temp.length == 2 && Utils.legalInt(temp[1])) {
            itemName = temp[0];
            data = Short.parseShort(temp[1]);
        }
        if (Utils.legalInt(itemName))
            mat = Material.fromID(Integer.parseInt(itemName));
        else
            mat = Material.fromString(itemName);
        if (!Utils.legalDouble(args[1]) && !args[1].equalsIgnoreCase("null")) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must enter a the price you want to set the item at.");
            return true;
        }
        if (mat == null) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "That item does not exist");
            return true;
        }
        if (!mat.isTool())
            mat = Material.fromData(data != 0 ? mat.getParent() : mat, data);
        String file;
        if (args[2].equalsIgnoreCase("buy") || args[2].equalsIgnoreCase("sell"))
            file = args[2].toLowerCase();
        else {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Input either sell or buy");
            return true;
        }
        if (args[1].equalsIgnoreCase("null")) {
            pr.setPrice(file, mat.getName(), args[1]);
            sender.sendMessage(var.getObj() + mat.getFriendlyName(2) + var.getMessages() + " can no longer be " + (args[2].equalsIgnoreCase("buy") ? "bought" : "sold"));
        } else {
            pr.setPrice(file, mat.getName(), Utils.roundTwoDecimals(Double.parseDouble(args[1])));
            sender.sendMessage(var.getObj() + Utils.ownerShip(mat.getFriendlyName(2)) + var.getMessages() + " " + (args[2].equalsIgnoreCase("buy") ? "buy" : "sell") +
                    " price was set to " + var.getMoney() + Economy.format(Double.parseDouble(args[1])));
        }
        return true;
    }
}