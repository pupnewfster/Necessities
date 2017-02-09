package gg.galaxygaming.necessities.Commands.Economy;

import gg.galaxygaming.necessities.Economy.Economy;
import gg.galaxygaming.necessities.Economy.Material;
import gg.galaxygaming.necessities.Necessities;
import gg.galaxygaming.necessities.Utils;
import gg.galaxygaming.necessities.Variables;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdPrice implements EconomyCmd {
    public boolean commandUse(CommandSender sender, String[] args) {
        Variables var = Necessities.getInstance().getVar();
        if (args.length == 0) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Format requires you enter the item you want to know the price of and whether to buy or sell.");
            return true;
        }
        String itemName = args[0];
        short data = 0;
        String oper = args[0];
        if (sender instanceof Player) {
            if (args.length == 1) {
                itemName = ((Player) sender).getInventory().getItemInMainHand().getType().toString();
                data = ((Player) sender).getInventory().getItemInMainHand().getDurability();
            }
        } else if (args.length < 2) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Format requires you enter the item you want to know the price of and whether to buy or sell.");
            return true;
        }
        if (args.length > 1) {
            oper = args[1];
            String[] temp = args[0].split(":");
            if (temp.length == 2 && Utils.legalInt(temp[1])) {
                itemName = temp[0];
                data = Short.parseShort(temp[1]);
            }
        }
        Material mat;
        if (Utils.legalInt(itemName))
            mat = Material.fromID(Integer.parseInt(itemName));
        else
            mat = Material.fromString(itemName);
        if (mat == null) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "That item does not exist");
            return true;
        }
        if (!mat.isTool())
            mat = Material.fromData(data != 0 ? mat.getParent() : mat, data);
        String file;
        if (oper.equalsIgnoreCase("buy") || oper.equalsIgnoreCase("sell"))
            file = oper.toLowerCase();
        else {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Input either buy or sell");
            return true;
        }
        String cost = Necessities.getInstance().getPrices().cost(file, mat.getName());
        if (cost == null || cost.equalsIgnoreCase("null")) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + mat.getFriendlyName(2) + " cannot be " + (oper.equalsIgnoreCase("buy") ? "bought from" : "sold to") + " the server");
            return true;
        }
        sender.sendMessage(var.getObj() + mat.getFriendlyName(2) + var.getMessages() + " can be " + (oper.equalsIgnoreCase("buy") ? "bought" : "sold") + " for " + var.getMoney() +
                Economy.format(Double.parseDouble(cost)));
        return true;
    }
}