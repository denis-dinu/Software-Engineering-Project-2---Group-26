

//import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Board {

    // a 2D array that stores the cells of the board
    private final Cell[][] cells;

    /*
        A hashmap that stores the ray markers placed on the board in (key, value) pairs:

        - the key represents the input point chosen by the experimenter to send a ray into the board
        - the value represents the point determined by the algorithm to be the output point of the ray
          (for example key=value for reflected rays, value=null for absorbed rays)

     */
    private final HashMap<Integer, Integer> rayMarkers;

    final static int BOARD_SIZE = 9;
    private final static double ROW_HEIGHT = 50.0;
    private Random random = new Random();

    //Board constructor creates the board and each of its cells and also sets up the neighbours of each cell
    public Board() {
        cells = new Cell[BOARD_SIZE][];
        rayMarkers = new HashMap<>();

        for(int i=0; i<BOARD_SIZE; i++) {

            //calculate the length of the current row
            int rowLength;
            if(i <= 4) {
                rowLength = 5+i;
            } else {
                rowLength = 13-i;
            }

            double yOffset = (BOARD_SIZE - rowLength) * ROW_HEIGHT / 2.0;

            cells[i] = new Cell[rowLength];

            //create cells on the current row up to the length of the row
            for(int j=0; j<rowLength; j++) {
                cells[i][j] = new Cell();
                cells[i][j].setRow(i);
                cells[i][j].setCol(j);

                //setting up neighbours

                //UPPER-LEFT & LOWER-RIGHT neighbours
                if(i>0 && i<=4 && j>0) {    //upper-half of the board
                    cells[i][j].setNeighbour(0, cells[i-1][j-1]);
                    cells[i-1][j-1].setNeighbour(3, cells[i][j]);
                } else if(i>4) {    //lower-half of the board
                    cells[i][j].setNeighbour(0, cells[i-1][j]);
                    cells[i-1][j].setNeighbour(3, cells[i][j]);
                }
                //in the rest of the cases(left and upper edges of the upper half), the cells don't have upper-left neighbours (they remain set to null)
                //also, the right and lower edges of the lower half will not have lower-right neighbours (they remain set to null)

                //UPPER-RIGHT & LOWER-LEFT neighbours
                if(i>0 && i<=4 && j<rowLength-1) {  //upper-half of the board
                    cells[i][j].setNeighbour(1, cells[i-1][j]);
                    cells[i-1][j].setNeighbour(4, cells[i][j]);
                } else if(i>4) {    //lower-half of the board
                    cells[i][j].setNeighbour(1, cells[i-1][j+1]);
                    cells[i-1][j+1].setNeighbour(4, cells[i][j]);
                }
                //in the rest of the cases(right and upper edges of the upper half), the cells don't have upper-right neighbours (they remain set to null)
                //also, the left and lower edges of the lower half will not have lower-left neighbours (they remain set to null)

                //LEFT & RIGHT neighbours
                if(j>0) {
                    cells[i][j].setNeighbour(5, cells[i][j-1]);
                    cells[i][j-1].setNeighbour(2, cells[i][j]);
                }
                //in the rest of the cases(left edge of the board), the cells don't have left neighbours (they remain set to null)
                //also, the right edge of the board will not have right neighbours (they remain set to null)

                // Set vertical position
                double yPosition = yOffset + j * ROW_HEIGHT;
                cells[i][j].setY(yPosition);
            }


        }
    }

    // Method to get the size of the board
    public int getSize() {
        return BOARD_SIZE;
    }

    // Method to get the size of a specific row
    public int getSize(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < BOARD_SIZE) {
            return cells[rowIndex].length;
        } else {
            return 0;
        }
    }

    /**
     * Method which randomly generates board positions for the number of atoms specified in its parameter.
     * This method modifies the internal state of the board, by setting a boolean flag in the chosen cells.
     *
     * @param maxAtoms the number of atom positions to generate
     */

    public void generateAtoms(int maxAtoms) {
        //generate 6 random atom numbers

            Board board = this;


                int atomsPlaced = 0; // Counter to keep track of atoms placed
                while (atomsPlaced < maxAtoms) {

                    // Loop until 6 atoms are placed
                    // Generate random coordinates within the board size

                    //calculate the length of the current row



                    int y = random.nextInt(BOARD_SIZE);
                    int rowLength;

                    if(y <= 4) {
                        rowLength = 5+y;
                    } else {
                        rowLength = 13-y;
                    }
                    int x = random.nextInt(rowLength);

                    // Check if there is no atom already at the generated position
                    if (!board.getCells()[y][x].hasAtom()) {
                        // If the cell is empty, set an atom at the generated position
                        board.getCells()[y][x].setAtom();
                        atomsPlaced++; // Increment the counter for atoms placed
                    }
                }
            }



    public Cell[][] getCells() {
        return cells;
    }

    public HashMap<Integer, Integer> getRayMarkers() {
        return rayMarkers;
    }

    public void addRayMarker(Integer inputPoint, Integer outputPoint) {
        rayMarkers.put(inputPoint, outputPoint);
    }

    //useful for testing the generateAtoms methods
    public void setSeed(int seed) {
        random = new Random(seed);
    }
}