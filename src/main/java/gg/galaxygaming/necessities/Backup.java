package gg.galaxygaming.necessities;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Calendar;

class Backup {
    static void tryBackup() {
        Calendar c = Calendar.getInstance();
        String date = Integer.toString(c.get(Calendar.MONTH) + 1) + '-' + Integer.toString(c.get(Calendar.DATE)) + '-' + Integer.toString(c.get(Calendar.YEAR));
        File current = new File("plugins/Necessities/Backup/" + date + "-users.yml");
        if (!current.exists()) {
            try {
                Files.copy(new File("plugins/Necessities/RankManager/users.yml").toPath(), current.toPath());
                //TODO add any other files we want backed up if we have enough instead of using a file name change... we should use a folder per day system
                //If we do it that way, then we could add an api to add file paths to be backed up
            } catch (IOException ignored) {
            }
        }
    }
}