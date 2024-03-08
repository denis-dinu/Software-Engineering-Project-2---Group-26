import java.util.ArrayList;

public class Cell {

    private boolean atom;
    private double yPosition;   //used for the GUI


    /*
        An array that stores references to the cells which are neighbours of this cell (according to the
        visual representation of the board):

        - index 0 contains the upper-left neighbour
        - index 1 contains the upper-right neighbour
        ...and so on going clockwise
        - index 5 contains the left neighbour

        If the cell does not have a neighbour at a certain position (for example cells at the edge of the board),
        null is stored at that index
     */
    private final Cell[] neighbours;
    private int row, col;       //store the row and column number of this cell on the board

    /*  An ArrayList that stores information about the rays passing through this cell - each ray passing through this cell
        corresponds to a ray segment in this ArrayList - each ray segment gives the entry point of the ray to this cell
        and the exit point from this cell - used for the final display of rays on the board
     */
    private final ArrayList<RaySegment> raySegments;


    public Cell() {
        this.atom = false;
        this.neighbours = new Cell[6];
        this.raySegments = new ArrayList<>();

    }




    public void setNeighbour(int position, Cell newNeighbour) {

        if(position < 0 || position > 5) {
            throw new IllegalArgumentException("Invalid neighbour position");
        }

        if(neighbours[position] == null) {
            neighbours[position] = newNeighbour;
        }
    }

    public boolean hasAtom() {
        return atom;
    }

    public void setAtom() {
        atom = true;
    }

    public Cell[] getNeighbours() {
        return neighbours;
    }

    public double getY() {
        return yPosition;
    }

    public void setY(double yPosition) {
        this.yPosition = yPosition;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public ArrayList<RaySegment> getRaySegments() {
        return raySegments;
    }

    public void addRaySegment(int entryPoint, int exitPoint) {
        raySegments.add(new RaySegment(entryPoint, exitPoint));
    }
}
