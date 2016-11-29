package com.crossge.necessities;

import com.crossge.necessities.RankManager.Rank;
import com.crossge.necessities.RankManager.RankManager;
import com.crossge.necessities.RankManager.User;
import org.bukkit.Bukkit;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

public class ScoreBoards {
    private final String[] ALPHABET = new String[]{"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
    private ScoreboardManager man;
    private Scoreboard b;

    void createScoreboard() {
        man = Bukkit.getScoreboardManager();
        b = man.getMainScoreboard();//Use main scoreboard instead of a new one for better comparability with other plugins
        RankManager rm = Necessities.getInstance().getRM();
        for (Rank r : rm.getOrder()) {
            Team t = b.getTeam(fromInt(rm.getOrder().size() - rm.getOrder().indexOf(r)));
            if (t == null)
                t = b.registerNewTeam(fromInt(rm.getOrder().size() - rm.getOrder().indexOf(r)));
            t.setPrefix(r.getColor());
        }
    }

    public void addPlayer(User u) {
        if (u.getRank() == null || u.getPlayer() == null)
            return;
        RankManager rm = Necessities.getInstance().getRM();
        Team t = b.getTeam(fromInt(rm.getOrder().size() - rm.getOrder().indexOf(u.getRank())));
        if (t == null)
            return;
        for (Team team : b.getTeams())
            if (team.hasEntry(u.getPlayer().getName()))
                team.removeEntry(u.getPlayer().getName());
        t.addEntry(u.getPlayer().getName());
        u.getPlayer().setScoreboard(b);
    }

    public void delPlayer(User u) {
        if (u.getRank() == null || u.getPlayer() == null)
            return;
        Team t = b.getTeam(u.getPlayer().getName());
        RankManager rm = Necessities.getInstance().getRM();
        if (t == null)
            t = b.getTeam(fromInt(rm.getOrder().size() - rm.getOrder().indexOf(u.getRank())));
        if (t == null)
            return;
        t.removeEntry(u.getPlayer().getName());
        u.getPlayer().setScoreboard(man.getNewScoreboard());
    }

    private String fromInt(int toTranslate) {
        toTranslate -= 1;
        return (toTranslate < 0 || ALPHABET.length <= toTranslate) ? "ERROR" : ALPHABET[toTranslate];
    }
}