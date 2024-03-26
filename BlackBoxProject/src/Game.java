public class Game {

    private final Board board;
    private final BoardUI boardUI;

    public Game() {
        //create a new board associated with this game and generate random atom locations for it
        board = new Board();
        board.generateAtoms(6);

        //create a boardUI associated with this game's board
        boardUI = new BoardUI(board);
    }

    /**
     * This method implements the game phase where the experimenter sends a ray into the board.
     *
     *
     * @param input the input supplied by the experimenter (might not be valid, in which case an IllegalArgumentException will be thrown)
     * @return the resulting output point to be announced to the experimenter
     */
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
