public class Cell {

    private boolean atom;
    private double yPosition;

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

    public Cell() {
        this.atom = false;
        this.neighbours = new Cell[6];
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


}
