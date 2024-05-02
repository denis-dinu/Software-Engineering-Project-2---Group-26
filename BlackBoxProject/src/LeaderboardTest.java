import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class LeaderboardFormatTest {
    // tests whether the leaderboard loader correctly catches formatting errors

    Leaderboard leaderboard = new Leaderboard();

    @Test
    public void testEntryFormat1() {
        try{
            Util.writeTestFile("good:35\nstill_good:10\nbad100");

            // expecting status code 1 for leaderboard formatting error
            assertEquals(1, leaderboard.loadLeaderboard("testfile.txt"));
        } catch (IOException ignored) {
            // don't consider an error in opening or creating the test file a fail
            // as we are only testing whether formatting errors are caught correctly
        }
    }

    @Test
    public void testEntryFormat2() {
        try{
            Util.writeTestFile("good:35\nstill_good:10\ntoo:20:many:fields:20");
            assertEquals(1, leaderboard.loadLeaderboard("testfile.txt"));
        } catch (IOException ignored) {
        }
    }

    @Test
    public void testScoreFormat1() {
        try{
            Util.writeTestFile("good:35\nbad:bad\nstill_good:100");
            assertEquals(1, leaderboard.loadLeaderboard("testfile.txt"));
        } catch (IOException ignored) {
        }
    }

    @Test
    public void testScoreFormat2() {
        try{
            Util.writeTestFile("good:35\nbad::\nstill_good:100");
            assertEquals(1, leaderboard.loadLeaderboard("testfile.txt"));
        } catch (IOException ignored) {
        }
    }
}

class LeaderboardSortTest {
    Leaderboard leaderboard = new Leaderboard();
    @Test
    public void testSorted() {
        try{
            Util.writeTestFile("a:100\nb:110\nc:50\nd:70\ne:50\nf:10\ng:85");
            leaderboard.loadLeaderboard("testfile.txt");
            List<String> list = leaderboard.getLeaderboardList();
            int prev = Integer.parseInt(list.get(0).split(":")[1].trim());
            for(int i = 1; i<list.size(); i++) {
                int curr = Integer.parseInt(list.get(i).split(":")[1].trim());
                if(curr < prev) {
                    fail("Not in sorted order");
                }
            }
        } catch (IOException ignored) {
        }
    }
}

class LeaderboardUpdateTest {
    @Test
    public void testNameNotPresent() {
        try {
            Util.writeTestFile("a:100\nb:110\nc:50\nd:70\ne:50\nf:10\ng:85");
            Leaderboard.saveScore("new", 45, "testfile.txt");
            assertTrue(Util.checkTestFile("new", 45));
        }catch (IOException ignored) {
        }
    }

    @Test
    public void testNameAlreadyPresent() {
        try {
            Util.writeTestFile("a:100\nb:110\nc:50\nd:70\ne:50\nf:10\ng:85");
            Leaderboard.saveScore("b", 30, "testfile.txt");
            assertTrue(Util.checkTestFile("b", 140));
            assertFalse(Util.checkTestFile("b", 110));
        }catch (IOException ignored) {
        }
    }
}

class Util {
    static void writeTestFile(String str) throws IOException {
        FileWriter writer = new FileWriter("testfile.txt");
        writer.write(str);
        writer.close();
    }

    // checks whether the given name-score pair appears in the test file
    static boolean checkTestFile(String name, int score) throws IOException{
        Scanner scanner = new Scanner(new File("testfile.txt"));
        while(scanner.hasNextLine()) {
            String str = scanner.nextLine();
            String[] split = str.split(":");
            if(split[0].equals(name) && Integer.parseInt(split[1]) == score) {
                return true;
            }
        }
        scanner.close();
        return false;
    }
}


