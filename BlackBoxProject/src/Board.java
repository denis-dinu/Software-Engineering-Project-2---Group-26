import java.util.*;

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
                // Set vertical position
                double yPosition = yOffset + j * ROW_HEIGHT;
                cell.setY(yPosition);
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
                if (cells[i][j] == null) cells[i][j] = new Cell();
                if (atomsSet >= maxAtoms) return; // Exit the method if the limit is reached

                // Set atom with the calculated probability
                if (random.nextDouble() < probability) {
                    cells[i][j].setAtom();
                    atomsSet++; // Increment the counter for the number of atoms set
                }
            }
        }
    }



}