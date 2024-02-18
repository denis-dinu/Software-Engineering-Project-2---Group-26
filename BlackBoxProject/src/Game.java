/*import java.util.Random;

public class Game {
    private Random random;
    private Board board;

    public Game(Board board) {
        this.board = board;
        random = new Random();
    }

    public void generateAtoms() {
        int atomsPlaced = 0; // Counter to keep track of atoms placed
        while (atomsPlaced < 6) {

            // Loop until 6 atoms are placed
            // Generate random coordinates within the board size
            int x = random.nextInt(board.getSize());
            int y = random.nextInt(board.getSize());

            // Check if there is no atom already at the generated position
            if (!board.isAtomPresent(x, y)) {
                // If the cell is empty, set an atom at the generated position
                board.setAtom(x, y);
                atomsPlaced++; // Increment the counter for atoms placed
            }
        }
    }
}*/
