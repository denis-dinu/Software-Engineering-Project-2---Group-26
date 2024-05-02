/**
 * Class that contains game-related logic, such as score calculation and performing a "game step" where
 * the experimenter sends a ray into the board and observes the result
 */
public class Game {

    private final Board board;
    private final BoardUI boardUI;


    /**
     * Constructs a new Game, with a new associated Board and BoardUI, generating 6 random atom locations
     */
    public Game() {
        board = new Board();
        board.generateAtoms(6);
        boardUI = new BoardUI(board);
    }


    /**
     * Constructs a new Game to be played with the given Board (useful for testing purposes)
     * @param board Board used to play the Game; the board may or may not already have atoms placed.
     *              This constructor will generate remaining random locations in order to make the
     *              number of atoms 6.
     */
    public Game(Board board) {
        if(board == null) {
            throw new IllegalArgumentException("Illegal board argument to Game constructor");
        }
        this.board = board;
        // if the provided board does not already contain 6 atoms, place remaining required number of atoms
        int atomsPlaced = board.countAtoms();
        board.generateAtoms(atomsPlaced < 6 ? 6-atomsPlaced : 0);
        this.boardUI = new BoardUI(board);
    }

    /**
     * Counts the number of matches between the player's guesses and the true atom locations in this Game
     * @return the number of correctly guessed atom locations
     */
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

    /**
     * Counts the score obtained by the player in this Game, considering the number of missed guesses
     * and the number of ray markers used
     * @return the player's penalty score
     */
    public int countScore()
    {
        int matchPoints = (6-countMatches())*5;
        int rayPoints = boardUI.board.getRayMarkers().size();
        return rayPoints + matchPoints;
    }

    /**
     * Determines whether 2 given sets of coordinates match
     * @param atomCoordinate the x and y coordinates of a true atom position
     * @param playerMarkerCoordinate the x and y coordinates of a player marker
     * @return true if the coordinates match, false otherwise
     */
    private boolean isCoordinateMatch(double[] atomCoordinate, double[] playerMarkerCoordinate) {
        double atomX = atomCoordinate[0];
        double atomY = atomCoordinate[1];
        double playerMarkerX = playerMarkerCoordinate[0];
        double playerMarkerY = playerMarkerCoordinate[1];

        // Add a tolerance value to handle slight variations in coordinates due to rounding errors
        final double TOLERANCE = 1e-5; // Adjust tolerance as needed
        return Math.abs(atomX - playerMarkerX) < TOLERANCE && Math.abs(atomY - playerMarkerY) < TOLERANCE;
    }

    /**
     * Sends a ray into the Game's Board, at an input point passed as argument, updating the Board and
     * BoardUI as a result of this.
     * @param input the input point where the ray should enter the board
     * @return the output point of the ray (or -1 if absorbed)
     */
    public int sendExperimenterRay(String input) {
        int inputCoordinate = validateExperimenterInput(input);
        int outputCoordinate = Ray.process(board, inputCoordinate);
        board.addRayMarker(inputCoordinate, outputCoordinate);

        // Refresh the board to show the updated ray path
        boardUI.drawBoard();

        return outputCoordinate;
    }

    /**
     * Validates input passed to sendExperimenterRay, making sure it is a valid integer input point that
     * has not already been tested
     * @param input the input point to be validated
     * @return the validated input point as an int
     */
    private int validateExperimenterInput(String input) {
        int inputCoordinate = Integer.parseInt(input);
        for(RayMarker rm : board.getRayMarkers()) {
            if(rm.inputPoint() == inputCoordinate || rm.outputPoint() == inputCoordinate) {
                throw new InputPointTestedException("Input point has already been tested, " +
                        "please enter another input point");
            }
        }
        return inputCoordinate;
    }

    public BoardUI getBoardUI() {
        return boardUI;
    }
    public Board getBoard() { return board; }
}

/**
 * A custom exception thrown during experimenter input validation to isolate the case where the experimenter
 * supplies an input point for which the ray path has already been calculated
 */
class InputPointTestedException extends RuntimeException {
    public InputPointTestedException(String message) {
        super(message);
    }
}
