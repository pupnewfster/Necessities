package com.crossge.necessities.Economy;

import com.crossge.necessities.Formatter;
import com.crossge.necessities.Variables;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Sign;

public class Signs {
    Formatter form = new Formatter();
    Materials mat = new Materials();
    Variables var = new Variables();
    Prices pr = new Prices();

    public boolean checkFormat(Sign sign) {
        if (sign.getLines()[0] == null || sign.getLines()[1] == null || sign.getLines()[2] == null)
            return false;
        String operation = getOperation(ChatColor.stripColor(sign.getLine(0).trim()));
        if (operation.equals("buy") || operation.equals("sell")) {
            String itemName = itemName(sign);
            if (itemName == null)
                return false;
            String amount = ChatColor.stripColor(sign.getLine(2).trim());
            return form.isLegal(amount) && pr.getCost(operation, itemName, Integer.parseInt(amount)) != -1 && Integer.parseInt(amount) > 0;
        }
        return false;
    }

    public Sign sign(Location loc) {
        return loc.getBlock().getState() instanceof Sign ? (Sign) loc.getBlock().getState() : null;
    }

    public void setSign(Sign sign) {
        setPrice(sign);
        formatSign(sign);
        sign.update();
    }

    public String itemName(Sign sign) {
    	String itemName = ChatColor.stripColor(sign.getLine(1).trim().replaceAll(" ", "")).replaceAll(":", " ").split(" ")[0];
    	if (form.isLegal(itemName))
            itemName = mat.idToName(Integer.parseInt(itemName));
        return mat.findItem(itemName.trim());
    }
    
    public String itemLine(Sign sign) {
    	String line = ChatColor.stripColor(sign.getLine(1).trim().replaceAll(" ", ""));
    	String itemName = line.replaceAll(":", " ").split(" ")[0];
    	if (form.isLegal(itemName))
            itemName = mat.idToName(Integer.parseInt(itemName));
        return mat.findItem(itemName.trim()) + (line.replaceAll(":", " ").split(" ").length > 1 ? ":" + line.replaceAll(":", " ").split(" ")[1] : "");
    }

    public Integer amount(Sign sign) {
        return Integer.parseInt(ChatColor.stripColor(sign.getLine(2)));
    }

    public String operation(Sign sign) {
        return getOperation(ChatColor.stripColor(sign.getLine(0).trim().toLowerCase()));
    }

    private void setPrice(Sign sign) {
        String operation = getOperation(sign.getLine(0).trim());
        String itemName = sign.getLine(1).trim().replaceAll(" ", "").replaceAll(":", " ").split(" ")[0];
        if (form.isLegal(itemName))
            itemName = mat.idToName(Integer.parseInt(itemName));
        itemName = mat.findItem(itemName);
        int amount = Integer.parseInt(sign.getLine(2).trim());
        double price = pr.getCost(operation, itemName, amount);
        sign.setLine(3, var.getMoney() + "$" + form.addCommas(form.roundTwoDecimals(price)));
    }

    private void formatSign(Sign sign) {
        String operation = sign.getLine(0).trim();
        operation = ChatColor.BLUE + "[" + form.capFirst(getOperation(operation)) + "]";
        sign.setLine(0, operation);
        sign.setLine(1, var.getMessages() + form.capFirst(sign.getLine(1).trim()));
        sign.setLine(2, var.getMessages() + sign.getLine(2).trim());
    }

    private String getOperation(String operation) {
        operation = operation.toLowerCase();
        if (operation.contains("buy"))
            return "buy";
        else if (operation.contains("sell"))
            return "sell";
        return "";//TODO: possibly add more
    }
}