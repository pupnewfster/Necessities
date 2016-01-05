package com.crossge.necessities.Economy;

import java.util.HashMap;

public class Trade {
    private static HashMap<String, String> trades = new HashMap<>();

    public boolean hasTrade(String pname, String offerpname) {
        return trades.containsKey(pname + " " + offerpname);
    }

    public void createTrade(String inf) {
        trades.put(inf.split(" ")[0] + " " + inf.split(" ")[1], inf.split(" ")[2] + " " + inf.split(" ")[3] + " " + inf.split(" ")[4] + " " + inf.split(" ")[5]);
    }

    public String acceptTrade(String pname, String offerpname) {
        String info = trades.get(pname + " " + offerpname);
        removeTrade(pname, offerpname);
        return info;
    }

    public void denyTrade(String pname, String offerpname) {
        removeTrade(pname, offerpname);
    }

    public void removeTrade(String pname, String offerpname) {
        trades.remove(pname + " " + offerpname);
    }
}