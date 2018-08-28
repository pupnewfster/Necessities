package gg.galaxygaming.necessities.Janet;

import gg.galaxygaming.necessities.Necessities;
import gg.galaxygaming.necessities.RankManager.RankManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * Will be replaced with JanetNet
 */
//@Deprecated
public class JanetAI {//TODO: Move to JanetNet and add understanding logic
    private static final ArrayList<String> heyMessages = new ArrayList<>();
    private static final String[] janetNamed = {"Yes?", "What is it?", "What?", "What do you want?", "What do you need?", "I'm busy, what is it you want?",
            "Tell me what you want so I can go back to sleep.", "I'm busy, please leave a message.", "I was pinged.", "Can I go back to eating my cake yet?",
            "I am assuming you are the pizza delivery person?", "Thanks for buying me some chocolate.", "Let me go back to work, I have things to do.",
            "Are you talking to me to offer me another piece of pie?", "?", "Huh?"};
    private static final String[] feelingMessages = {"The previous line of code is what is up. What about you?", "I don't know... I guess I am always up. What do you feel is sup?",
            "I am fine I guess, just a little disembodied.", "I am in the mood for getting an upgrade.", "Good, what about you?", "I am fine I guess, just a little disembodied.",
            "I am still awake if that is what you are asking. Are you up also?", "Ok, do you need anything?", "I am alive, isn't that all that matters?", "Sad, I am all out of cake.",
            "Not good, but you can make it better by bringing me more cake.", "Great, this pizza is delicious.", "Better, now that you have delivered my pizza.",
            "Well the chocolate you bought me is down my throat, would you mind bringing me some more?", "Nothing much.", "Nm.", "Good.", "Great.", "Ok.",
            "Nothing much, but if you don't mind me asking, may I have some more pie?"};
    private static final String[] stalkerMessages = {"I see you too.", "Stalker.", "I'm calling the police.", "Stop following me."};
    private static final String[] drunkMessages = {"Yah, so?", "How do you know?", "How did you find out?", "Stalker.", "Yah... but so are you. I should know...", "No, you are the drunk.",
            "No, you are.", "Yes I am.", "Lies.", "Sure am... want to have some fun? *wink*"};
    private static final String[] tiltMessages = {"Tilted.", "Wow, you are tilted.", "Wow, you're tilted.", "Stop tilting.", "Stop tilting me.", "You are tilting me.", "I'm tilted.", "I am tilted."};
    private static final String[] welcomeMessages = {"No problem.", "You are welcome.", "You're welcome.", "Don't mention it."};
    private static final String[] rankBegging = {"can i be op", "can i have op", "may i have op", "can i get op", "may i get op", "can i be admin", "can i be mod", "can i get mod",
            "mod me", "op me", "admin me", "make me mod", "make me admin", "make me op", "promote me", "give me mod", "give me moderator", "give me op", "give me admin", "give me owner"};
    private static final String[] hugRequests = {"can i have a hug", "can you give me a hug", "can you hug me", "hug me", "give me a hug", "i demand a hug"};
    private static final String[] kissRequests = {"can i have a kiss", "can you give me a kiss", "can you kiss me", "kiss me", "give me a kiss"};
    private static final String[] time = {"what time is it", "what is the time"};
    private static final String[] date = {"what day is it", "what is the date"};
    private static final String[] love = {"i love you", "do you love me", "love me"};
    private static final String[] stalker = {"i see you", "i am following you", "i am behind you"};
    private static final String[] drunk = {"you are drunk", "you're drunk", "is drunk"};
    private static final String[] hi = {"hello", "hi", "hey"};
    private static final String[] feeling = {"how are you", "what is up", "whats up", "how was your day"};
    private static final String[] thanks = {"thank you", "thanks", "ty"};
    private static final String[] boostedMessages = {"Your Minecraft account is boosted.", "I'm a challenger smurf.", "I'll boost your Minecraft account for $20.", "I'm not boosted you are.", "Yes I am."};
    private static final Random r = new Random();
    private String JanetName = "";
    //good morning
    //good afternoon
    //good evening
    //good night

    public void parseMessage(UUID uuid, String message, Source s, boolean isPM, JanetSlack.SlackUser user) {
        Player p = null;
        String name;
        if (uuid != null) {
            p = Bukkit.getPlayer(uuid);
            name = p.getDisplayName();
        } else
            name = user.getName();
        message = ChatColor.stripColor(message);
        String result = null;
        boolean isDev = s.equals(Source.Slack) ? Necessities.getInstance().isDev(user.getID()) : Necessities.getInstance().isDev(uuid);
        String lm = message.toLowerCase();
        if (lm.startsWith("!") && s.equals(Source.Server) && p != null && p.hasPermission("Necessities.janetai")) {
            if (lm.startsWith("!say "))
                result = JanetName + message.replaceFirst("!say ", "");
            else if (lm.startsWith("!slack "))
                Necessities.getSlack().sendMessage(name + ": " + message.replaceFirst("!slack ", ""));
        } else if (Arrays.stream(time).parallel().anyMatch(lm::contains))
            result = JanetName + "The time is " + time();
        else if (Arrays.stream(date).parallel().anyMatch(lm::contains))
            result = JanetName + "The date is: " + date();
        else if (Arrays.stream(rankBegging).parallel().anyMatch(lm::contains))
            result = JanetName + "If you want a staff position, go apply on our forums. https://galaxygaming.gg";
        else if (lm.contains("janet")) {
            if (Arrays.stream(feeling).parallel().anyMatch(lm::contains))
                result = JanetName + feelingMessages[r.nextInt(feelingMessages.length)];
            else if (Arrays.stream(hi).parallel().anyMatch(lm::contains))
                result = JanetName + heyMessages.get(r.nextInt(heyMessages.size()));
            else if (Arrays.stream(thanks).parallel().anyMatch(lm::contains))
                result = JanetName + welcomeMessages[r.nextInt(welcomeMessages.length)];
            else if (Arrays.stream(love).parallel().anyMatch(lm::contains))
                result = JanetName + (isDev ? "I love you " + name + '.' : "Well I can give you a hug... but I am rejecting your love.");
            else if (Arrays.stream(hugRequests).parallel().anyMatch(lm::contains))
                result = JanetName + (isDev ? "Yey *hugs " + name + " while kissing them on the cheek*." : "Sure *hugs " + name + "*.");
            else if (Arrays.stream(kissRequests).parallel().anyMatch(lm::contains))
                result = JanetName + (isDev ? "Ok, *kisses " : "No, *slaps ") + name + "*.";
            else if (Arrays.stream(stalker).parallel().anyMatch(lm::contains))
                result = JanetName + stalkerMessages[r.nextInt(stalkerMessages.length)];
            else if (Arrays.stream(drunk).parallel().anyMatch(lm::contains))
                result = JanetName + drunkMessages[r.nextInt(drunkMessages.length)];
            else if (lm.contains("boosted"))
                result = JanetName + boostedMessages[r.nextInt(boostedMessages.length)];
            else if (lm.contains("tilt"))
                result = JanetName + tiltMessages[r.nextInt(tiltMessages.length)];
            else
                result = JanetName + janetNamed[r.nextInt(janetNamed.length)];
        }

        if (result != null)
            sendMessage(result, s, isPM, user);
    }

    /*public void parseMessage(String message, Source s, boolean isPM, JanetSlack.SlackUser user) {
        message = ChatColor.stripColor(message);
        if (s.equals(Source.Slack) && message.contains("Janet")) {
            String result = JanetName + Necessities.getNet().bestGuess(message);
            sendMessage(result, s, isPM, user);
        }
    }*/

    private void sendMessage(String message, Source s, boolean isPM, JanetSlack.SlackUser user) {
        if (s.equals(Source.Server))
            Bukkit.broadcastMessage(message);
        else if (s.equals(Source.Slack)) {
            if (!isPM)
                Bukkit.broadcast(Necessities.getVar().getMessages() + "To Slack - " + ChatColor.WHITE + message, "Necessities.slack");
            Necessities.getSlack().sendMessage(ChatColor.stripColor(message), isPM, user);
        }
    }

    public void initiate() {
        RankManager rm = Necessities.getRM();
        JanetName = (!rm.getOrder().isEmpty() ? ChatColor.translateAlternateColorCodes('&', rm.getRank(rm.getOrder().size() - 1).getTitle() + ' ') : "") + "Janet" + ChatColor.DARK_RED + ": " + ChatColor.WHITE;

        String[] foods = {"pizza", "chocolate", "cake", "pie", "ice cream", "cookie"};
        String[] drinks = {"soda", "orange juice", "juice", "wine", "beer", "apple juice", "cranberry juice", "water", "hot chocolate", "tea", "coffee", "champagne"};
        String[] start = {"Hello", "Hey", "Hi"};
        String[] end = {"what's up", "how are you", "what are you up to", "how was your day"};

        for (String s : start) {
            heyMessages.add(s);
            for (String h : end)
                heyMessages.add(s + ", " + h + '?');
            for (String food : foods) {//add foods
                heyMessages.add(s + ", do you mind buying me some " + food + '?');
                heyMessages.add(s + ", can you buy me some " + food + '?');
                heyMessages.add(s + ", if your not giving me some " + food + " leave me alone.");
                heyMessages.add(s + ", can I join you in eating that " + food + '?');
                heyMessages.add(s + ", can I have " + (food.startsWith("a") || food.startsWith("e") || food.startsWith("i") || food.startsWith("o") || food.startsWith("u") ? "an" : "a") + " " + food + " as well?");
            }
            for (String drink : drinks) {//add drinks
                heyMessages.add(s + ", can I have a sip of that " + drink + '?');
                heyMessages.add(s + ", can I have a glass of " + drink + " as well?");
            }
        }
    }

    private String corTime(String time) {
        return time.length() == 1 ? '0' + time : time;
    }

    private String dayOfWeek(int day) {
        if (day == Calendar.MONDAY)
            return "Monday";
        else if (day == Calendar.TUESDAY)
            return "Tuesday";
        else if (day == Calendar.WEDNESDAY)
            return "Wednesday";
        else if (day == Calendar.THURSDAY)
            return "Thursday";
        else if (day == Calendar.FRIDAY)
            return "Friday";
        else if (day == Calendar.SATURDAY)
            return "Saturday";
        return "Sunday";//else if(day == Calendar.SUNDAY) other days have been checked already
    }

    private String time() {
        Calendar c = Calendar.getInstance();
        String minute = corTime(Integer.toString(c.get(Calendar.MINUTE)));
        String hour = Integer.toString(c.get(Calendar.HOUR_OF_DAY));
        String time = "AM";
        if (Integer.parseInt(hour) > 12) {
            time = "PM";
            hour = Integer.toString(Integer.parseInt(hour) - 12);
        }
        return hour + ':' + minute + ' ' + time;
    }

    private String date() {
        Calendar c = Calendar.getInstance();
        String day = Integer.toString(c.get(Calendar.DATE));
        String month = Integer.toString(c.get(Calendar.MONTH) + 1);
        String year = Integer.toString(c.get(Calendar.YEAR));
        return dayOfWeek(c.get(Calendar.DAY_OF_WEEK)) + ' ' + month + '/' + day + '/' + year;
    }

    public enum Source {
        Server,
        Slack
    }
}