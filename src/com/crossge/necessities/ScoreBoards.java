package com.crossge.necessities;

import org.bukkit.Bukkit;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import com.crossge.necessities.RankManager.Rank;
import com.crossge.necessities.RankManager.RankManager;
import com.crossge.necessities.RankManager.User;

public class ScoreBoards {
	private static ScoreboardManager man;
	private static Scoreboard b;
	RankManager rm = new RankManager();
	
	public void createScoreboard() {
		man = Bukkit.getScoreboardManager();
		b = man.getMainScoreboard();//Use main scoreboard instead of a new one for better compatability with other plugins
		for(Rank r : rm.getOrder())
			if(b.getTeam(r.getName()) == null) {
				Team t = b.registerNewTeam(r.getName());
				t.setPrefix(r.getColor());
			}
	}
	
	public void addPlayer(User u) {
		if(u.getRank() == null || u.getPlayer() == null)
			return;
		Team t = b.getTeam(u.getRank().getName());
		if(t == null)
			return;
		for(Team team : b.getTeams())
			if(team.hasPlayer(u.getPlayer()))
				team.removePlayer(u.getPlayer());
		t.addPlayer(u.getPlayer());
		u.getPlayer().setScoreboard(b);
	}
	
	public void delPlayer(User u) {
		if(u.getRank() == null || u.getPlayer() == null)
			return;
		Team t = b.getTeam(u.getPlayer().getName());
		if(t == null)
			t = b.getTeam(u.getRank().getName());
		if(t == null)
			return;
		t.removePlayer(u.getPlayer());
		u.getPlayer().setScoreboard(man.getNewScoreboard());
	}
}