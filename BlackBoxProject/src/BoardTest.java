import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NeighbourTest {

    Board board = new Board();

    // methods that test cell neighbours are set up correctly
    @Test
    void testUpperRight() {
        assertNull(board.getCells()[0][1].getNeighbours()[1]);
        assertEquals(board.getCells()[1][1], board.getCells()[2][1].getNeighbours()[1]);
    }

    @Test
    void testUpperLeft() {
        assertNull(board.getCells()[2][0].getNeighbours()[0]);
        assertEquals(board.getCells()[4][5], board.getCells()[5][5].getNeighbours()[0]);
    }

    @Test
    void testLeft() {
        assertNull(board.getCells()[2][0].getNeighbours()[5]);
        assertEquals(board.getCells()[8][2], board.getCells()[8][3].getNeighbours()[5]);
    }

    @Test
    void testLowerLeft() {
        assertNull(board.getCells()[5][0].getNeighbours()[4]);
        assertEquals(board.getCells()[3][6], board.getCells()[2][6].getNeighbours()[4]);
    }

    @Test
    void testLowerRight() {
        assertNull(board.getCells()[8][1].getNeighbours()[3]);
        assertEquals(board.getCells()[6][2], board.getCells()[5][2].getNeighbours()[3]);
    }

    @Test
    void testRight() {
        assertNull(board.getCells()[8][4].getNeighbours()[2]);
        assertEquals(board.getCells()[0][3], board.getCells()[0][2].getNeighbours()[2]);
    }

}

class AtomGenerationTest {
    @Test
    public void testGenerateAtoms() {
        Board board = new Board();
        board.generateAtoms(6);
        int count = 0;

        for(Cell[] row: board.getCells()) {
            for(Cell cell: row) {
                if(cell.hasAtom()) {
                    count++;
                }
            }
        }

        assertEquals(6, count);
    }

}