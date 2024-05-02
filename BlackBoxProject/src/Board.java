
import java.util.ArrayList;
import java.util.Random;
import javafx.scene.paint.Color;

/**
 * Class that models a BlackBox board and its associated information and state (its cells, ray markers),
 * and implements operations on the board (such as generating random atom locations or counting atoms placed)
 */
public class Board {

    /**
     *  A 2D array that stores the cells of the board
     */
    private final Cell[][] cells;

    /**
     * An ArrayList that stores the ray markers placed on the board (see class RayMarker)
     */
    private final ArrayList<RayMarker> rayMarkers;

    final static int BOARD_SIZE = 9;


    /**
     * Constructor that creates the board structure as a 2D array of cells. Creates each of the cells
     * in the 2D array and also sets up the neighbours of each cell
     */
    public Board() {
        cells = new Cell[BOARD_SIZE][];
        rayMarkers = new ArrayList<>();

        for(int i=0; i<BOARD_SIZE; i++) {

            //calculate the length of the current row
            int rowLength = computeRowLength(i);

            cells[i] = new Cell[rowLength];

            //create cells on the current row up to the length of the row
            for(int j=0; j<rowLength; j++) {
                cells[i][j] = new Cell();
                cells[i][j].setRow(i);
                cells[i][j].setCol(j);

                setUpNeighbours(i, j);
            }
        }
    }

    private int computeRowLength(int i) {
        if(i <= 4) {
            return 5+i;
        } else {
            return 13-i;
        }
    }

    /**
     * Sets up the neighbours of the cell at a certain row and column in the 2D array of cells
     * @param i the row of the cell
     * @param j the column of the cell
     */
    private void setUpNeighbours(int i, int j) {
        //UPPER-LEFT & LOWER-RIGHT neighbours
        if(i>0 && i<=4 && j>0) {    //upper-half of the board
            cells[i][j].setNeighbour(0, cells[i-1][j-1]);
            cells[i-1][j-1].setNeighbour(3, cells[i][j]);
        } else if(i>4) {    //lower-half of the board
            cells[i][j].setNeighbour(0, cells[i-1][j]);
            cells[i-1][j].setNeighbour(3, cells[i][j]);
        }
        // In the rest of the cases (left and upper edges of the upper half), the cells don't have upper-left
        // neighbours (they remain set to null). Also, the right and lower edges of the lower half will not
        // have lower-right neighbours (they remain set to null)

        //UPPER-RIGHT & LOWER-LEFT neighbours
        if(i>0 && i<=4 && j<computeRowLength(i)-1) {  //upper-half of the board
            cells[i][j].setNeighbour(1, cells[i-1][j]);
            cells[i-1][j].setNeighbour(4, cells[i][j]);
        } else if(i>4) {    //lower-half of the board
            cells[i][j].setNeighbour(1, cells[i-1][j+1]);
            cells[i-1][j+1].setNeighbour(4, cells[i][j]);
        }
        // In the rest of the cases (right and upper edges of the upper half), the cells don't have upper-right
        // neighbours (they remain set to null). Also, the left and lower edges of the lower half will not have
        // lower-left neighbours (they remain set to null)

        //LEFT & RIGHT neighbours
        if(j>0) {
            cells[i][j].setNeighbour(5, cells[i][j-1]);
            cells[i][j-1].setNeighbour(2, cells[i][j]);
        }
        // In the rest of the cases(left edge of the board), the cells don't have left neighbours (they remain set
        // to null). Also, the right edge of the board will not have right neighbours (they remain set to null)
    }

    /**
     * Method which randomly generates board positions for the number of atoms specified in its parameter.
     * This method modifies the internal state of the board, by setting a boolean flag in the chosen cells.
     *
     * @param maxAtoms the number of atom positions to generate
     */

    public void generateAtoms(int maxAtoms) {
        //generate 6 random atom numbers

            Random random = new Random();

                int atomsPlaced = 0; // Counter to keep track of atoms placed
                while (atomsPlaced < maxAtoms) {

                    int y = random.nextInt(BOARD_SIZE);
                    int rowLength;

                    if(y <= 4) {
                        rowLength = 5+y;
                    } else {
                        rowLength = 13-y;
                    }
                    int x = random.nextInt(rowLength);

                    // Check if there is no atom already at the generated position
                    if (!this.getCells()[y][x].hasAtom()) {
                        // If the cell is empty, set an atom at the generated position
                        this.getCells()[y][x].setAtom();
                        atomsPlaced++;
                    }
                }
            }



    public Cell[][] getCells() {
        return cells;
    }

    public ArrayList<RayMarker> getRayMarkers() {
        return rayMarkers;
    }

    /**
     * Adds a RayMarker at the specified points to the Board's array of rayMarkers, choosing an appropriate
     * color for the ray marker
     * @param inputPoint the input point to be marked
     * @param outputPoint the output point to be marked
     */
    public void addRayMarker(Integer inputPoint, Integer outputPoint) {
        Color color;
        if(outputPoint == -1) {     // ray is absorbed
            color = Color.BLACK;
        } else if(inputPoint.equals(outputPoint)) {     // ray is reflected
            color = Color.WHITE;
        } else {
            // ensure chosen color is not too hard to distinguish from black or white for other rays
            do {
                color = Color.color(Math.random(), Math.random(), Math.random());
            } while(color.getBrightness() < 0.2 || color.getBrightness() > 0.8);
        }

        rayMarkers.add(new RayMarker(inputPoint, outputPoint, color));
    }

    public int countAtoms() {
        int numAtoms = 0;
        for(Cell[] row: cells) {
            for(Cell cell: row) {
                if(cell.hasAtom()) {
                    numAtoms++;
                }
            }
        }
        return numAtoms;
    }
}