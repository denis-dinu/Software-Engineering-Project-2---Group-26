
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BoardTest {

    @Test
    void testNeighbours() {
        Board board = new Board();

        //testing row index 0
        assertNull(board.getCells()[0][1].getNeighbours()[1]); //checking the upper-right neighbour
        assertNull(board.getCells()[0][2].getNeighbours()[0]); //checking the upper-left neighbour
        assertEquals(board.getCells()[0][3], board.getCells()[0][2].getNeighbours()[2]); //checking the right neighbour
        assertEquals(board.getCells()[1][4], board.getCells()[0][4].getNeighbours()[4]); //checking the lower-left neighbour

        //testing row index 2
        assertEquals(board.getCells()[1][1], board.getCells()[2][1].getNeighbours()[1]); //checking the upper-right neighbour
        assertEquals(board.getCells()[2][4], board.getCells()[2][3].getNeighbours()[2]); //checking the right neighbour
        assertNull(board.getCells()[2][0].getNeighbours()[5]); //checking the left neighbour
        assertNull(board.getCells()[2][0].getNeighbours()[0]); //checking the upper-left neighbour
        assertEquals(board.getCells()[3][6], board.getCells()[2][6].getNeighbours()[4]); //checking the lower-left neighbour

        //testing row index 5
        assertNull(board.getCells()[5][7].getNeighbours()[2]); //checking the right neighbour
        assertEquals(board.getCells()[4][4], board.getCells()[5][3].getNeighbours()[1]); //checking the upper-right neighbour
        assertEquals(board.getCells()[4][8], board.getCells()[5][7].getNeighbours()[1]); //checking the upper-right neighbour
        assertEquals(board.getCells()[4][6], board.getCells()[5][6].getNeighbours()[0]); //checking the upper-left neighbour
        assertNull(board.getCells()[5][0].getNeighbours()[4]); //checking the lower-left neighbour
        assertEquals(board.getCells()[6][2], board.getCells()[5][2].getNeighbours()[3]); //checking the lower-right neighbour

        //testing row index 8
        assertNull(board.getCells()[8][1].getNeighbours()[3]); //checking the lower-right neighbour
        assertNull(board.getCells()[8][4].getNeighbours()[4]); //checking the lower-left neighbour
        assertNull(board.getCells()[8][4].getNeighbours()[2]); //checking the right neighbour
        assertEquals(board.getCells()[8][2], board.getCells()[8][3].getNeighbours()[5]); //checking the left neighbour
        assertEquals(board.getCells()[7][2], board.getCells()[8][1].getNeighbours()[1]); //checking the upper-right neighbour
    }

    //tests if the board and cell neighbours are set up correctly
    @Test
    public void testBoard() {
        Board board = new Board();

        // Get the cells array
        Cell[][] cells = board.getCells();

        // Test neighbors for each cell
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[i].length; j++) {
                Cell cell = cells[i][j];

                if (j > 0) assertNotNull(cell.getNeighbours()[5]); // Left neighbor should not be null

                if (i > 0 && (j > 0 || i > board.BOARD_SIZE / 2)) assertNotNull(cell.getNeighbours()[0]); // Upper-left neighbor should not be null
                if (i > 0 && (j < cells[i].length-1 || i > board.BOARD_SIZE / 2)) assertNotNull(cell.getNeighbours()[1]); // Upper-right neighbor should not be null
                if (i < board.BOARD_SIZE-1 && (j < cells[i].length-1 || i < board.BOARD_SIZE / 2)) assertNotNull(cell.getNeighbours()[3]); // Lower-right neighbor should not be null
                if (i < board.BOARD_SIZE-1 && (j > 0 || i < board.BOARD_SIZE / 2)) assertNotNull(cell.getNeighbours()[4]); // Lower-left neighbor should not be null

            }
        }

        assertEquals(5, cells[0].length);
        assertEquals(9, cells[4].length);
        assertEquals(7, cells[6].length);
    }

    @Test
    public void testGenerateAtoms() {
        Board board = new Board();
        board.setSeed(0);
        board.generateAtoms(6);
        Cell[][] cells = board.getCells();

        //seed 0 should set atoms in the following cells
        assertTrue(cells[2][2].hasAtom());
        assertTrue(cells[4][2].hasAtom());
        assertTrue(cells[5][2].hasAtom());
        assertTrue(cells[6][2].hasAtom());
        assertTrue(cells[8][1].hasAtom());
        assertTrue(cells[8][3].hasAtom());
        assertFalse(cells[6][3].hasAtom());
    }

}
