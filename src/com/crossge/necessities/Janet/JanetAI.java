package com.crossge.necessities.Janet;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.crossge.necessities.RankManager.RankManager;

public class JanetAI {//TODO: Upgrade
	private static ArrayList<String> heyMessages = new ArrayList<String>();
	private static String[] janetNamed = new String[16];
	private static String[] feelingMessages = new String[20];
	private static String[] stalkerMessages = new String[4];
	private static String[] drunkMessages = new String[10];
	private static String JanetName = "";
	JanetRandom r = new JanetRandom();
	
	public void parseMessage(UUID uuid, String message) {
		Player p = Bukkit.getPlayer(uuid);
		message = ChatColor.stripColor(message);
		if(message.toLowerCase().contains("what time is it") || message.toLowerCase().contains("what is the time"))
			Bukkit.broadcastMessage(JanetName + "The time is " + time());
		else if(message.toLowerCase().contains("what day is it") || message.toLowerCase().contains("what is the date") ||
				message.toLowerCase().contains("whats the date") || message.toLowerCase().contains("what's the date"))
			Bukkit.broadcastMessage(JanetName + "The date is: " + date());
		else if(message.toLowerCase().contains("can i be op") || message.toLowerCase().contains("may i be op") ||
				message.toLowerCase().contains("can i have op") || message.toLowerCase().contains("may i have op") ||
				message.toLowerCase().contains("can i get op") || message.toLowerCase().contains("may i get op") ||
				message.toLowerCase().contains("can i be admin") || message.toLowerCase().contains("may i be admin") ||
				message.toLowerCase().contains("can i get admin") || message.toLowerCase().contains("may i get admin") ||
				message.toLowerCase().contains("can i have admin") || message.toLowerCase().contains("may i have admin") ||
				message.toLowerCase().contains("can i be mod") || message.toLowerCase().contains("may i be mod") ||
				message.toLowerCase().contains("can i get mod") || message.toLowerCase().contains("may i get mod") ||
				message.toLowerCase().contains("mod me") || message.toLowerCase().contains("op me") ||
				message.toLowerCase().contains("admin me") || message.toLowerCase().contains("make me mod") ||
				message.toLowerCase().contains("make me admin") || message.toLowerCase().contains("make me op") ||
				message.toLowerCase().contains("promote me"))
			Bukkit.broadcastMessage(JanetName + "You may only earn the rank, no free promotions");
		else if(message.toLowerCase().contains("janet")) {
			if(message.toLowerCase().contains("how are you") || message.toLowerCase().contains("what is up") || 
					message.toLowerCase().contains("sup") || message.toLowerCase().contains("whats up") ||
					message.toLowerCase().contains("how was your day"))
				Bukkit.broadcastMessage(JanetName + feelingMessages[r.rInt(feelingMessages.length)]);
			else if(message.toLowerCase().startsWith("hello") || message.toLowerCase().startsWith("hey") ||
					message.toLowerCase().startsWith("hi") || message.toLowerCase().startsWith("hai"))
				Bukkit.broadcastMessage(JanetName + heyMessages.get(r.rInt(heyMessages.size())));
			else if(message.toLowerCase().contains("i love you") || message.toLowerCase().contains("do you love me") ||
					message.toLowerCase().contains("i wub you") || message.toLowerCase().contains("do you wub me") ||
					message.toLowerCase().contains("love me")) {
				if(p.hasPermission("Necessities.janetai"))
					Bukkit.broadcastMessage(JanetName + "I love you " + p.getName() + ".");
				else
					Bukkit.broadcastMessage(JanetName + "Well I can give you a hug... but I am rejecting your love.");
			}
			else if(message.toLowerCase().contains("can i have a hug") || message.toLowerCase().contains("can you give me a hug") ||
					message.toLowerCase().contains("can you hug me") || message.toLowerCase().contains("hug me") ||
					message.toLowerCase().contains("give me a hug") || message.toLowerCase().contains("gimme a hug") ||
					message.toLowerCase().contains("hug me") || message.toLowerCase().contains("i demand a hug") ||
					message.toLowerCase().contains("can you gimme a hug")) {
				if(p.hasPermission("Necessities.janetai"))
					Bukkit.broadcastMessage(JanetName + "Yey *hugs " + p.getName() + " while kissing them on the cheek*.");
				else
					Bukkit.broadcastMessage(JanetName + "Sure *hugs " + p.getName() + "*.");
			}
			else if(message.toLowerCase().contains("can i have a kiss") || message.toLowerCase().contains("can you give me a kiss") ||
					message.toLowerCase().contains("can you kiss me") || message.toLowerCase().contains("kiss me") ||
					message.toLowerCase().contains("give me a kiss") || message.toLowerCase().contains("gimme a kiss") ||
					message.toLowerCase().contains("can you gimme a kiss")) {
				if(p.hasPermission("Necessities.janetai"))
					Bukkit.broadcastMessage(JanetName + "Ok, *kisses " + p.getName() + "*.");
				else
					Bukkit.broadcastMessage(JanetName + "No, *slaps " + p.getName() + "*.");
			}
			else if(message.toLowerCase().contains("i see you") || message.toLowerCase().contains("i am following you"))
				Bukkit.broadcastMessage(JanetName + stalkerMessages[r.rInt(stalkerMessages.length)]);
			else if(message.toLowerCase().contains("your drunk") || message.toLowerCase().contains("you are drunk") ||
					message.toLowerCase().contains("you're drunk") || message.toLowerCase().contains("is drunk"))
				Bukkit.broadcastMessage(JanetName + drunkMessages[r.rInt(drunkMessages.length)]);
			else
				Bukkit.broadcastMessage(JanetName + janetNamed[r.rInt(janetNamed.length)]);
		}		
	}
	
	public void initiate() {
		RankManager rm = new RankManager();
		String temp = Bukkit.getServerName();
		String rank = "";
		if(temp.contains(" "))
			temp = "world";
		if(!rm.getOrder().isEmpty())
			rank = ChatColor.translateAlternateColorCodes('&', rm.getRank(rm.getOrder().size() - 1).getTitle() + " ");
		JanetName = ChatColor.WHITE + temp + " " + rank + "Janet" + ChatColor.DARK_RED + ": " + ChatColor.WHITE;
		
		String[] foods = new String[6];
		String[] drinks = new String[8];
		String[] start = new String[7];
		String[] end = new String[6];
		foods[0] = "pizza";
		foods[1] = "chocolate";
		foods[2] = "cake";
		foods[3] = "pie";
		foods[4] = "icecream";
		foods[5] = "cookie";
		
		drinks[0] = "soda";
		drinks[1] = "orange juice";
		drinks[2] = "juice";
		drinks[3] = "wine";
		drinks[4] = "beer";
		drinks[5] = "apple juice";
		drinks[6] = "cranberry juice";
		drinks[7] = "water";
		
		start[0] = "Hello";
		start[1] = "Hey";
		start[2] = "Hi";
		start[3] = "Hai";
		start[4] = "Ohey";
		start[5] = "Ohai";
		start[6] = "Ohi";
		
		end[0] = "what's up";
		end[1] = "what is up";
		end[2] = "sup";
		end[3] = "how are you";
		end[4] = "what are you up to";
		end[5] = "what's up";
		
		for(int i = 0; i < start.length; i++) {
			heyMessages.add(start[i]);
			for(int j = 0; j < end.length; j++)//add normal endings
				heyMessages.add(start[i] + ", " + end[j] + "?");
			for(int j = 0; j < foods.length; j++) {//add foods
				heyMessages.add(start[i] + ", do you mind buying me some " + foods[j] + "?");
				heyMessages.add(start[i] + ", can you buy me some " + foods[j] + "?");
				heyMessages.add(start[i] + ", if your not giving me some " + foods[j] + " leave me alone.");
				heyMessages.add(start[i] + ", can I join you in eating that " + foods[j] + "?");
				heyMessages.add(start[i] + ", may I join you in eating that " + foods[j] + "?");
				heyMessages.add(start[i] + ", may I have a " + foods[j] + " as well?");
				heyMessages.add(start[i] + ", may I have a " + foods[j] + " too?");
			}
			for(int j = 0; j < drinks.length; j++) {//add drinks
				heyMessages.add(start[i] + ", can I have a sip of that " + drinks[j] + "?");
				heyMessages.add(start[i] + ", may I have a sip of that " + drinks[j] + "?");
				heyMessages.add(start[i] + ", can I have a glass of " + drinks[j] + " as well?");
				heyMessages.add(start[i] + ", may I have a glass of " + drinks[j] + " as well?");
				heyMessages.add(start[i] + ", may I have a glass of " + drinks[j] + " too?");
				heyMessages.add(start[i] + ", can I have a glass of " + drinks[j] + " too?");
			}
		}
		
		janetNamed[0] = "Yes?";
		janetNamed[1] = "What is it?";
		janetNamed[2] = "What?";
		janetNamed[3] = "What do you want?";
		janetNamed[4] = "What do you need?";
		janetNamed[5] = "I'm busy, what is it you want?";
		janetNamed[6] = "Tell me what you want so I can go back to sleep.";
		janetNamed[7] = "I'm busy, please leave a message.";
		janetNamed[8] = "I was pinged.";
		janetNamed[9] = "Can I go back to eating my cake yet?";
		janetNamed[10] = "I am assuming you are the pizza delivery person?";
		janetNamed[11] = "Thanks for buying me some chocolate.";
		janetNamed[12] = "Let me go back to work, I have things to do.";
		janetNamed[13] = "Are you talking to me to offering me another piece of pie?";
		janetNamed[14] = "?";
		janetNamed[15] = "Huh?";
		
		feelingMessages[0] = "The previous line of code is what is up. What about you?";
		feelingMessages[1] = "I don't know... I guess I am always up. What do you feel is sup?";
		feelingMessages[2] = "I am fine I guess, just a little disembodied.";
		feelingMessages[3] = "I am in the mood for getting an upgrade.";
		feelingMessages[4] = "Good, what about you?";
		feelingMessages[5] = "I am fine I guess, just a little disembodied.";
		feelingMessages[6] = "I am still awake if that is what you are asking. Are you up also?";
		feelingMessages[7] = "Ok, do you need anything?";
		feelingMessages[8] = "I am alive, isn't that all that matters?";
		feelingMessages[9] = "Sad, I am all out of cake.";
		feelingMessages[10] = "Not good, but you can make it better by bringing me more cake.";
		feelingMessages[11] = "Great, this pizza is delicious.";
		feelingMessages[12] = "Better, now that you have delivered my pizza.";
		feelingMessages[13] = "Well the chocolate you bought me is down my throat, would you mind bringing me some more?";
		feelingMessages[14] = "Nothing much.";
		feelingMessages[15] = "Nm.";
		feelingMessages[16] = "Good.";
		feelingMessages[17] = "Great.";
		feelingMessages[18] = "Ok.";
		feelingMessages[19] = "Nothing much, but if you don't mind me asking, may I have some more pie?";
		
		stalkerMessages[0] = "I see you too.";
		stalkerMessages[1] = "Stalker.";
		stalkerMessages[2] = "I'm calling the police.";
		stalkerMessages[3] = "Stop following me.";
		
		drunkMessages[0] = "Yah, so?";
		drunkMessages[1] = "How do you know?";
		drunkMessages[2] = "How did you find out?";
		drunkMessages[3] = "Stalker.";
		drunkMessages[4] = "Yah... but so are you. I should know...";
		drunkMessages[5] = "No, you are the drunk.";
		drunkMessages[6] = "No, you are.";
		drunkMessages[7] = "Yes I am.";
		drunkMessages[8] = "Lies.";
		drunkMessages[9] = "Sure am... want to have some fun? *wink*";
	}
	
	private String corTime(String time) {
		if(time.length() == 1)
			return "0" + time;
		return time;
	}
	
	private String dayOfWeek(int day) {
		if(day == Calendar.MONDAY)
			return "Monday";
		else if(day == Calendar.TUESDAY)
			return "Tuesday";
		else if(day == Calendar.WEDNESDAY)
			return "Wensday";
		else if(day == Calendar.THURSDAY)
			return "Thursday";
		else if(day == Calendar.FRIDAY)
			return "Friday";
		else if(day == Calendar.SATURDAY)
			return "Saturday";
		return "Sunday";//else if(day == Calendar.SUNDAY) other days have been checked already
	}
	
	private String time() {
		Calendar c = Calendar.getInstance();
		String minute = corTime(Integer.toString(c.get(Calendar.MINUTE)));
		String hour = Integer.toString(c.get(Calendar.HOUR_OF_DAY));
		String time = "AM";
		if(Integer.parseInt(hour) > 12) {
			time = "PM";
			hour = Integer.toString(Integer.parseInt(hour) - 12);
		}
		return hour + ":" + minute + " " + time;
	}
	
	private String date() {
		Calendar c = Calendar.getInstance();
		String day = Integer.toString(c.get(Calendar.DATE));
		String month = Integer.toString(c.get(Calendar.MONTH) + 1);
		String year = Integer.toString(c.get(Calendar.YEAR));
		return dayOfWeek(c.get(Calendar.DAY_OF_WEEK)) + " " + month + "/" + day + "/" + year;
	}
}