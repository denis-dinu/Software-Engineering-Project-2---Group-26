import javafx.scene.paint.Color;

public class Game {

    private final Board board;
    private final BoardUI boardUI;

    public Game() {
        board = new Board();
        board.generateAtoms(6);
        boardUI = new BoardUI(board);
    }

    public int countMatches() {
        int matchCount = 0;
        for (double[] atomCoordinate : boardUI.getAtomCoordinates()) {
            for (double[] playerMarkerCoordinate : boardUI.getPlayerMarkerCoordinates()) {
                if (isCoordinateMatch(atomCoordinate, playerMarkerCoordinate)) {
                    matchCount++;
                }
            }
        }
        return matchCount;
    }

    private boolean isCoordinateMatch(double[] atomCoordinate, double[] playerMarkerCoordinate) {
        double atomX = atomCoordinate[0];
        double atomY = atomCoordinate[1];
        double playerMarkerX = playerMarkerCoordinate[0];
        double playerMarkerY = playerMarkerCoordinate[1];

        // You might want to add a tolerance value to handle slight variations in coordinates due to rounding errors
        final double TOLERANCE = 1e-5; // Adjust tolerance as needed
        return Math.abs(atomX - playerMarkerX) < TOLERANCE && Math.abs(atomY - playerMarkerY) < TOLERANCE;
    }

    public BoardUI getBoardUI() {
        return boardUI;
    }
}
