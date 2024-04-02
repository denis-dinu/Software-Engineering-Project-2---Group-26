import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class RayTest {

    @Test
    void noAtomsOutputPoint() {
        Board board = new Board();
        board.setSeed(1);
        board.generateAtoms(6);

        assertEquals(39, Ray.process(board, 8));
        assertEquals(54, Ray.process(board, 11));
        assertEquals(11, Ray.process(board, 54));
    }

    @Test
    void absorbedOutputPoint() {
        Board board = new Board();
        board.setSeed(1);
        board.generateAtoms(6);

        assertEquals(-1, Ray.process(board, 7));
        assertEquals(-1, Ray.process(board, 46));
        assertEquals(-1, Ray.process(board, 38));
    }

    @Test
    void reflectionOutputPoint() {
        Board board = new Board();
        board.setSeed(10);
        board.generateAtoms(6);

        assertEquals(5, Ray.process(board, 5));
        assertEquals(24, Ray.process(board, 24));
    }

    @Test
    void deviation60OutputPoint() {
        Board board = new Board();
        board.setSeed(7);
        board.generateAtoms(6);

        assertEquals(10, Ray.process(board, 34));
        assertEquals(51, Ray.process(board, 12));
        assertEquals(7, Ray.process(board, 17));
    }

    @Test
    void deviation120OutputPoint() {
        Board board = new Board();
        board.setSeed(5);
        board.generateAtoms(6);

        assertEquals(5, Ray.process(board, 44));
        assertEquals(1, Ray.process(board, 10));
    }

    @Test
    void edgeOfTheBoard() {
        Board board = new Board();
        board.setSeed(7);
        board.generateAtoms(6);

        assertEquals(48, Ray.process(board, 48));
        assertEquals(42, Ray.process(board, 42));
        assertEquals(41, Ray.process(board, 41));
        assertEquals(39, Ray.process(board, 39));
    }

    @Test
    void testRaySegments() {
        Board board = new Board();
        board.setSeed(1);
        board.generateAtoms(6);

        //send 3 rays into the board
        Ray.process(board, 8);
        Ray.process(board, 54);
        Ray.process(board, 7);

        Cell testCell = board.getCells()[3][0];
        ArrayList<Integer> entryPoints = new ArrayList<>();
        ArrayList<Integer> exitPoints = new ArrayList<>();
        for(RaySegment rs : testCell.getRaySegments()) {
            entryPoints.add(rs.entryPoint());
            exitPoints.add(rs.exitPoint());
        }

        assertEquals(3, entryPoints.size());
        assertTrue(entryPoints.contains(0));
        assertTrue(entryPoints.contains(1));
        assertTrue(entryPoints.contains(5));

        assertEquals(3, exitPoints.size());
        assertTrue(exitPoints.contains(2));
        assertTrue(exitPoints.contains(3));
        assertTrue(exitPoints.contains(4));

    }



}
