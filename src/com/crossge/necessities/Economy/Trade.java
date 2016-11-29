package com.crossge.necessities.Economy;

import java.util.HashMap;

public class Trade {
    private final HashMap<String, String> trades = new HashMap<>();

    public boolean hasTrade(String pname, String offerPname) {
        return trades.containsKey(pname + " " + offerPname);
    }

    public void createTrade(String inf) {
        trades.put(inf.split(" ")[0] + " " + inf.split(" ")[1], inf.split(" ")[2] + " " + inf.split(" ")[3] + " " + inf.split(" ")[4] + " " + inf.split(" ")[5]);
    }

    public String acceptTrade(String pname, String offerPname) {
        String info = trades.get(pname + " " + offerPname);
        removeTrade(pname, offerPname);
        return info;
    }

    public void denyTrade(String pname, String offerPname) {
        removeTrade(pname, offerPname);
    }

    private void removeTrade(String pname, String offerPname) {
        trades.remove(pname + " " + offerPname);
    }
}