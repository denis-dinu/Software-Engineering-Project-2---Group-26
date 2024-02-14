Import java.util.Random;

public class Game {

    public static void generateAtoms(Board board) {

        /*
            generate 6 random numbers/atom positions and then update the corresponding cells
            of the board passed as argument by calling the setAtom() method on the right cells
         */
        
     // Method to generate 6 random atom positions and update the board
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
    }

}
