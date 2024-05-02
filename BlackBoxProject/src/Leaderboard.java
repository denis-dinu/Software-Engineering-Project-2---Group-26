import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Leaderboard {

    private final List<String> leaderboardList = new ArrayList<>();

    /**
     * Attempts to save a player's score in the file leaderboard.txt in the same folder (leaderboard.txt
     * is created if it does not exist) and returns a status code reflecting the success/failure of the operation
     *
     * @param name The name of the player whose score is being saved
     * @param score The score being saved
     * @return  0 for success, -1 for error
     */
    public static int saveScore(String name, int score) {

        Leaderboard leaderboard = new Leaderboard();

        int status = leaderboard.loadLeaderboard();
        if(status != 0) {   // if leaderboard file is not well formatted or another file error occurred
            return -1;
        }

        // status returned by loadLeaderboard = 0, so we know file is well formatted

        leaderboard.updateLeaderboard(name, score);
        return leaderboard.writeToLeaderboardFile();
    }

    /**
     * Attempts to load a leaderboard from file leaderboard.txt in the same folder where the application
     * is running (leaderboard.txt is created if it does not exist) into this Leaderboard's
     * leaderboardList and returns a status code reflecting the success/failure of the operation.
     * If successful, the leaderboard is loaded in sorted order, according to the scores.
     *
     * @return 0 for success, 1 for leaderboard formatting error, 2 for general file error
     */
    public int loadLeaderboard() {

        try {
            readLeaderboardFile();
        } catch (IOException e) {
            return 2;
        }

        if(!leaderboardList.isEmpty()) {
            try {
                sortLeaderboard();
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                return 1;
            }
        }

        return 0;
    }

    /**
     * Attempts to read the contents of file leaderboard.txt in the same folder where the application
     * is running (leaderboard.txt is created if it does not exist) placing the file's contents into
     * this Leaderboard's leaderboardList line by line
     * @throws IOException in the case of a file error
     */
    private void readLeaderboardFile() throws IOException {
        File f = new File("leaderboard.txt");
        // made the leaderboard system a little more robust by creating a leaderboard file
        // if there is not already one in the current folder instead of immediately throwing exception

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
     * Sorts the contents of this Leaderboard's leaderboardList according to the score found by parsing
     * each String element of the leaderboardList. The leaderboardList is expected to be in a certain
     * format and if the user has modified the file leaderboard.txt in a way that violates this format,
     * or leaderboardList doesn't otherwise respect this format, an exception will be thrown to the caller
     *
     * @throws NumberFormatException if the score field in a leaderboard entry is not a number
     * @throws ArrayIndexOutOfBoundsException if one of the leaderboard entries does not contain the
     * established separator between the name and score fields
     */
    private void sortLeaderboard() throws NumberFormatException, ArrayIndexOutOfBoundsException {
        // check format of first entry
        int ignored = Integer.parseInt(leaderboardList.get(0).split(":")[1].trim());

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
     * Attempts to write this Leaderboard's leaderboardList to the file leaderboard.txt
     * in the same folder where the application is running (leaderboard.txt is created if it does not exist)
     * @return 0 for success, -1 for error
     */
    private int writeToLeaderboardFile() {
        try (FileWriter writer = new FileWriter("leaderboard.txt")) {
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
