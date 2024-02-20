/*import java.util.*;

public class Board {

    // a 2D array that stores the cells of the board (padded with null references where the row is not full of cells)
    private final Cell[][] cells;
     final int BOARD_SIZE = 9;
    public int cellsNumber = 0;
    private final double ROW_HEIGHT = 50.0;

    // Board constructor creates the board and each of its cells and also sets up the neighbours of each cell
    public Board() {
        cells = new Cell[BOARD_SIZE][];
        for (int i = 0; i < BOARD_SIZE; i++) {
            int rowLength = i < BOARD_SIZE / 2 ? i + 5 : BOARD_SIZE - i + 4;
            cells[i] = new Cell[rowLength];

            for(int j=0; j<rowLength; j++) {
                cells[i][j] = new Cell();
            }
        }
        setupNeighbours();
    }

    // Set up neighbours for each cell
    private void setupNeighbours() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            int rowLength = cells[i].length;
            double yOffset = (BOARD_SIZE - rowLength) * ROW_HEIGHT / 2.0;
            for (int j = 0; j < rowLength; j++) {
                Cell cell = cells[i][j];
                if (cell == null) continue; // Skip null cells

                // Set neighbours
                if (j > 0) cell.setNeighbour(5, cells[i][j - 1]); // Left neighbour

                if (i > 0) {
                    if (i <= BOARD_SIZE / 2) {
                        cell.setNeighbour(0, cells[i - 1][j]); // Upper-left neighbour
                        if (j < cells[i - 1].length) {
                            cell.setNeighbour(1, cells[i - 1][j + 1]); // Upper-right neighbour
                        }
                    } else {
                        cell.setNeighbour(3, cells[i - 1][j]); // Lower-left neighbour
                        if (j > 0) cell.setNeighbour(4, cells[i - 1][j - 1]); // Lower-right neighbour
                    }

                }

            }
        }
    }

    public Cell[][] getCells() {
        return cells;
    }


    // Ive asked Vida to quickly rewrite a generate Atoms method without git branch hussle
    public void generateAtoms(int maxAtoms) {

        Random random = new Random();
        int atomsSet = 0; // Counter for the number of atoms set

        // Calculate the center row index
        int centerRow = BOARD_SIZE / 2;

        for (int i = 0; i < BOARD_SIZE; i++) {
            // Calculate the probability of placing an atom based on the distance from the center row
            double distanceFromCenter = Math.abs(i - centerRow);
            double probability = 0.3 * (1.0 - (distanceFromCenter / centerRow));

            for (int j = 0; j < cells[i].length; j++) {
                if (cells[i][j] == null) ;
                if (atomsSet >= maxAtoms) return; // Exit the method if the limit is reached

                // Set atom with the calculated probability
                if (random.nextDouble() < probability) {
                    cells[i][j].setAtom();
                    atomsSet++; // Increment the counter for the number of atoms set
                }
            }
        }
    }

}*/

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

    final int BOARD_SIZE = 9;
    private final double ROW_HEIGHT = 50.0;

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

    public void generateAtoms(int maxAtoms) {

        Random random = new Random();
        int atomsSet = 0; // Counter for the number of atoms set

        // Calculate the center row index
        int centerRow = BOARD_SIZE / 2;

        for (int i = 0; i < BOARD_SIZE; i++) {
            // Calculate the probability of placing an atom based on the distance from the center row
            double distanceFromCenter = Math.abs(i - centerRow);
            double probability = 0.3 * (1.0 - (distanceFromCenter / centerRow));

            for (int j = 0; j < cells[i].length; j++) {
                if (cells[i][j] == null) ;
                if (atomsSet >= maxAtoms) return; // Exit the method if the limit is reached

                // Set atom with the calculated probability
                if (random.nextDouble() < probability) {
                    cells[i][j].setAtom();
                    atomsSet++; // Increment the counter for the number of atoms set
                }
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
}