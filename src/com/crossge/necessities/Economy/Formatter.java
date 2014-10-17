package com.crossge.necessities.Economy;

import java.text.DecimalFormat;

public class Formatter {
    public boolean isLegal(String input) {
        try {
            Double.parseDouble(input);
            return true;
        } catch (Exception ignored) { }
        return false;
    }

    public String roundTwoDecimals(double d) {
        return new DecimalFormat("0.00").format(d);
    }

    public String addCommas(String s) {
        return new DecimalFormat("#,##0.00").format(Double.parseDouble(s));
    }

    public String addCommas(int i) {
        return new DecimalFormat("#,###").format(i);
    }

    public String capFirst(String matName) {
        String name = "";
        matName = matName.replaceAll("_", " ").toLowerCase();
        String[] namePieces = matName.split(" ");
        for (String piece : namePieces)
            name += upercaseFirst(piece) + " ";
        return name.trim();
    }

    private String upercaseFirst(String word) {
        String firstCapitalized = "";
        if (word.length() > 0)
            firstCapitalized = word.substring(0, 1).toUpperCase();
        if (word.length() > 1)
            firstCapitalized += word.substring(1);
        return firstCapitalized;
    }
}