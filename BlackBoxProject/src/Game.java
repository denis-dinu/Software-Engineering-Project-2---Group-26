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
        int inputCoordinate = Integer.parseInt(input);
        int outputCoordinate = Ray.process(board, inputCoordinate);
        board.addRayMarker(inputCoordinate, outputCoordinate);

        // Refresh the board to show the updated ray path
        boardUI.weirdBoard = true;
        boardUI.drawBoard();

        return outputCoordinate;
    }

    public Board getBoard() {
        return board;
    }

    public BoardUI getBoardUI() {
        return boardUI;
    }
}
