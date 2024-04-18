import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Leaderboard {


    /**
     * Attempts to save a player's score in the file leaderboard.txt in the same folder (leaderboard.txt
     * is created if it does not exist) and returns a status code reflecting the success/failure of the operation
     *
     * @param name The name of the player whose score is being saved
     * @param score The score being saved
     * @return  0 for success, -1 for error
     */
    public static int saveScore(String name, int score) {

        List<String> leaderboard = new ArrayList<>();

        int status = loadLeaderboard(leaderboard);
        if(status != 0) {   // if leaderboard file is not well formatted or another file error occurred
            return -1;
        }

        // status returned by loadLeaderboard = 0, so we know file is well formatted

        return addToLeaderboard(name, score, leaderboard);

    }

    // returns 0 for success, -1 for error
    private static int addToLeaderboard(String name, int score, List<String> leaderboard) {
        boolean found = false;
        ArrayList<String> names = new ArrayList<>();
        ArrayList<Integer> scores = new ArrayList<>();

        for (String s : leaderboard) {
            String[] split = s.split(":");
            String leaderboardName = split[0];
            int leaderboardScore = Integer.parseInt(split[1].trim());
            if(name.equals(leaderboardName)) {
                found = true;
                leaderboardScore += score;
            }
            names.add(leaderboardName);
            scores.add(leaderboardScore);
        }
        if(!found) {
            names.add(name);
            scores.add(score);
        }

        return writeToLeaderboardFile(names, scores);
    }

    // returns 0 for success, -1 for error
    private static int writeToLeaderboardFile(ArrayList<String> names, ArrayList<Integer> scores) {
        try (FileWriter writer = new FileWriter("leaderboard.txt")) {
            for(int i = 0; i < names.size(); i++) {
                writer.write(names.get(i) + ":" + scores.get(i) + "\n");
            }
        } catch (IOException e) {
            return -1;
        }

        return 0;
    }


    /**
     * Attempts to load a leaderboard from file leaderboard.txt in the same folder (leaderboard.txt
     * is created if it does not exist) into the list passed as argument and returns a status code
     * reflecting the success/failure of the operation.
     * If successful, the leaderboard is loaded in sorted order, according to the scores.
     *
     * @param leaderboard The list into which to load the leaderboard entries, as Strings, in sorted order according to the scores
     * @return 0 for success, 1 for leaderboard formatting error, 2 for general file error
     */
    public static int loadLeaderboard(List<String> leaderboard) {

        try {
            readLeaderboardFile(leaderboard);
        } catch (IOException e) {
            return 2;
        }

        if(!leaderboard.isEmpty()) {
            try {
                sortLeaderboard(leaderboard);
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                return 1;
            }
        }

        return 0;
    }

    private static void readLeaderboardFile(List<String> leaderboard) throws IOException {
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
            leaderboard.add(line);
        }
        reader.close();
    }

    private static void sortLeaderboard(List<String> leaderboard) throws NumberFormatException, ArrayIndexOutOfBoundsException {
        // check format of first entry
        int ignored = Integer.parseInt(leaderboard.get(0).split(":")[1].trim());

        leaderboard.sort((entry1, entry2) -> {
            int score1 = Integer.parseInt(entry1.split(":")[1].trim());
            int score2 = Integer.parseInt(entry2.split(":")[1].trim());
            return Integer.compare(score1, score2);
        });
    }
}
