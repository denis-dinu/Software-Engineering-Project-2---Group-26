import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ScoreTest {
    @Test
    void testScore() {
        Board board = new Board();
        Cell[][] cells = board.getCells();

        // set up true atom locations
        Cell[] atoms = new Cell[] {cells[0][4], cells[3][2], cells[4][2], cells[6][1], cells[7][3], cells[7][5]};
        for(Cell cell: atoms) {
            cell.setAtom();
        }

        Game game = new Game(board);

        // set up player atom markers (guesses)
        BoardUI boardUI = game.getBoardUI();
        ArrayList<double[]> atomMarkers = boardUI.getPlayerMarkerCoordinates();
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
}
