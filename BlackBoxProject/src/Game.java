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

    public int sendExperimenterRay(String input) {
        int inputCoordinate = validateExperimenterInput(input);
        int outputCoordinate = Ray.process(board, inputCoordinate);
        board.addRayMarker(inputCoordinate, outputCoordinate);

        // Refresh the board to show the updated ray path
        boardUI.drawBoard();

        return outputCoordinate;
    }

    private int validateExperimenterInput(String input) {
        int inputCoordinate = Integer.parseInt(input);
        for(RayMarker rm : board.getRayMarkers()) {
            if(rm.inputPoint() == inputCoordinate || rm.outputPoint() == inputCoordinate) {
                throw new InputPointTestedException("Input point has already been tested, please enter another input point");
            }
        }
        return inputCoordinate;
    }

    public BoardUI getBoardUI() {
        return boardUI;
    }
}

//a custom exception thrown during experimenter input validation to isolate the case where the experimenter
//supplies an input point for which the ray path has already been calculated
class InputPointTestedException extends RuntimeException {
    public InputPointTestedException(String message) {
        super(message);
    }
}
