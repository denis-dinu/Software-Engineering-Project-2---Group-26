import java.io.*;
import java.util.*;

/**
 * Class that models a leaderboard used to keep track of player's obtained scores. Contains functionality
 * for reading and writing the file used to store the leaderboard, checking the format of the leaderboard file,
 * parsing and sorting this file.
 */
public class Leaderboard {

    private final List<String> leaderboardList = new ArrayList<>();

    /**
     * Attempts to save a player's score in the file with the given filename
     * and returns a status code reflecting the success/failure of the operation
     *
     * @param name The name of the player whose score is being saved
     * @param score The score being saved
     * @param filename the file to save the score to
     * @return  0 for success, -1 for error
     */
    public static int saveScore(String name, int score, String filename) {

        Leaderboard leaderboard = new Leaderboard();

        int status = leaderboard.loadLeaderboard(filename);
        if(status != 0) {   // if leaderboard file is not well formatted or another file error occurred
            return -1;
        }

        // status returned by loadLeaderboard = 0, so we know leaderboard is well formatted

        leaderboard.updateLeaderboard(name, score);
        return leaderboard.writeToLeaderboardFile(filename);
    }

    /**
     * Attempts to load a leaderboard from the file with the given filename into this Leaderboard's
     * leaderboardList and returns a status code reflecting the success/failure of the operation.
     * If successful, the leaderboard is loaded in sorted order, according to the scores.
     * @param filename the file to load the leaderboard from, in sorted order
     * @return 0 for success, 1 for leaderboard formatting error, 2 for general file error
     */
    public int loadLeaderboard(String filename) {

        try {
            readLeaderboardFile(filename);
        } catch (IOException e) {
            return 2;
        }

        if(!leaderboardList.isEmpty()) {
            try {
                verifyFormat();
                sortLeaderboard();
            } catch (LeaderboardFormatException e) {
                return 1;
            }
        }

        return 0;
    }

    /**
     * Attempts to read the contents of the file with the given filename, placing the file's contents into
     * this Leaderboard's leaderboardList line by line
     * @param filename the file to read the leaderboard from
     * @throws IOException in the case of a file error
     */
    private void readLeaderboardFile(String filename) throws IOException {
        File f = new File(filename);

        if(!f.exists()) {
            if(!f.createNewFile()) {
                throw new IOException();
            }
        }
        BufferedReader reader = new BufferedReader(new FileReader(f));

        String line;
        while ((line = reader.readLine()) != null) {
            leaderboardList.add(line);
        }
        reader.close();
    }

    /**
     * Verifies the format of the leaderboardList entries and throws a LeaderboardFormatException
     * if an entry does not conform to the following format: each entry must be made up by 2 fields,
     * a name and an integer score, separated by the character ":". This check should always pass as
     * long as the user has not modified leaderboard.txt in a way that violates this format
     */
    private void verifyFormat() {
        for(String entry: leaderboardList) {
            String[] split = entry.split(":");
            try {
                int ignored = Integer.parseInt(split[1].trim());
            } catch (RuntimeException ex) {
                throw new LeaderboardFormatException("Illegal leaderboard format");
            }
            if(split.length != 2) {
                throw new LeaderboardFormatException("Illegal leaderboard format");
            }
        }
    }

    /**
     * Sorts the contents of this Leaderboard's leaderboardList according to the score found by parsing
     * each String element of the leaderboardList. The leaderboardList is expected to be in a certain
     * format, where the name field is separated by the score field by a ":" character and the score
     * is a number
     */
    private void sortLeaderboard() {
        leaderboardList.sort((entry1, entry2) -> {
            int score1 = Integer.parseInt(entry1.split(":")[1].trim());
            int score2 = Integer.parseInt(entry2.split(":")[1].trim());
            return Integer.compare(score1, score2);
        });
    }

    /**
     * Updates this Leaderboard's leaderboardList with a name-score pair: in the case the given
     * name is already present on the leaderboard, its score is simply updated, else the name is added
     * as a new entry together with the given score
     * @param name the name with which to update the leaderboard
     * @param score the score associated with the given name
     */
    private void updateLeaderboard(String name, int score) {
        boolean found = false;

        for (int i = 0; i < leaderboardList.size(); i++) {
            String[] split = leaderboardList.get(i).split(":");
            String leaderboardName = split[0];
            int leaderboardScore = Integer.parseInt(split[1].trim());
            if(name.equals(leaderboardName)) {
                // update this leaderboard entry
                found = true;
                leaderboardList.set(i, leaderboardName + ":" + (leaderboardScore + score));
            }
        }
        if(!found) {
            leaderboardList.add(name + ":" + score);
        }
    }

    /**
     * Attempts to write this Leaderboard's leaderboardList to the file with the given filename
     * @return 0 for success, -1 for error
     */
    private int writeToLeaderboardFile(String filename) {
        try (FileWriter writer = new FileWriter(filename)) {
            for(String entry: leaderboardList) {
                writer.write(entry + "\n");
            }
        } catch (IOException e) {
            return -1;
        }

        return 0;
    }


    public List<String> getLeaderboardList() {
        return leaderboardList;
    }
}

class LeaderboardFormatException extends RuntimeException {
    public LeaderboardFormatException(String message) {
        super(message);
    }
}
