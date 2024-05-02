import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ScoreTest {

    Game game;
    Board board;
    Cell[][] cells;
    Cell[] atoms;

    @BeforeEach
    void setUpGame() {
        board = new Board();
        cells = board.getCells();

        // set up true atom locations
        atoms = new Cell[]{cells[0][4], cells[3][2], cells[4][2], cells[6][1], cells[7][3], cells[7][5]};
        for (Cell cell : atoms) {
            cell.setAtom();
        }

        game = new Game(board);
    }

    @Test
    void testScore() {
        // set up player atom markers (guesses)
        ArrayList<double[]> atomMarkers = game.getBoardUI().getPlayerMarkerCoordinates();
        Cell[] guesses = new Cell[] {cells[1][1], cells[1][2], cells[3][3], cells[3][6], cells[7][0], cells[7][5]};
        for(Cell cell: guesses) {
            atomMarkers.add(new double[] {cell.getCenterX(), cell.getCenterY()});
        }

        // add some ray markers
        board.addRayMarker(3, 3);
        board.addRayMarker(7, 37);
        board.addRayMarker(10, 20);

        // score should be 5 mismatched atoms * 5 + 3 ray markers
        assertEquals(28, game.countScore());
    }

    @Test
    void testPerfectScore() {
        // set up player atom markers (guesses)
        ArrayList<double[]> atomMarkers = game.getBoardUI().getPlayerMarkerCoordinates();
        for(Cell cell: atoms) {
            atomMarkers.add(new double[] {cell.getCenterX(), cell.getCenterY()});
        }

        // score should be 0
        assertEquals(0, game.countScore());
    }
}



