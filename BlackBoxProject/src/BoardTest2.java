import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BoardTest2 {

    @Test
    public void testNeighbours() {
        Board board = new Board();

        // Get the cells array
        Cell[][] cells = board.getCells();

        // Test neighbors for each cell
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[i].length; j++) {
                Cell cell = cells[i][j];

                if (cell != null) {
                    if (j > 0) assertNotNull(cell.getNeighbours()[5]); // Left neighbor should not be null

                    if (i > 0 && (i <= board.BOARD_SIZE / 2) && j < cells[i - 1].length) assertNotNull(cell.getNeighbours()[0]); // Upper-left neighbor should not be null
                    if (i > 0 && (i <= board.BOARD_SIZE / 2)) assertNotNull(cell.getNeighbours()[1]); // Upper-right neighbor should not be null
                    if (i > board.BOARD_SIZE / 2 && j > 0) assertNotNull(cell.getNeighbours()[3]); // Lower-left neighbor should not be null
                    if (i > board.BOARD_SIZE / 2 && j < cells[i - 1].length) assertNotNull(cell.getNeighbours()[4]);

                }
            }
        }
    }
}
