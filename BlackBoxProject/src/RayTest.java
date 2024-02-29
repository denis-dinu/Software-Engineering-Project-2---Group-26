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
