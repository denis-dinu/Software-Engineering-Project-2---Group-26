import java.util.HashMap;

public class Board {

    // a 2D array that stores the cells of the board (padded with null references where the row is not full of cells)
    private final Cell[][] cells;

    /*
        A hashmap that stores the ray markers placed on the board in (key, value) pairs:

        - the key represents the input point chosen by the experimenter to send a ray into the board
        - the value represents the point determined by the algorithm to be the output point of the ray
          (for example key=value for reflected rays, value=null for absorbed rays)

     */
    private final HashMap<Integer, Integer> rayMarkers;

    private final int BOARD_SIZE = 9;

    //Board constructor creates the board and each of its cells and also sets up the neighbours of each cell
    public Board() {
        cells = new Cell[BOARD_SIZE][BOARD_SIZE];
        rayMarkers = new HashMap<>();

        for(int i=0; i<BOARD_SIZE; i++) {

            //calculate the length of the current row
            int rowLength;
            if(i <= 4) {
                rowLength = 5+i;
            } else {
                rowLength = 13-i;
            }

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

            }
            //the rest of the cells on the row remain set to null



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
