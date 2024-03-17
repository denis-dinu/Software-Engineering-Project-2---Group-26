public class Ray {      //contains only static methods for ray processing

    /**
     *  Static method which processes a ray, calculating its output point from the board based on its input point
     *  and the atoms encountered. This method modifies the internal state of the board, as it saves information about
     *  the ray trajectory inside the board's cells.
     *
     * @param board the Board containing the 2D array of cells
     * @param inputPoint the input point chosen by the player for this ray
     * @return the number of the output point for rays that exit the board or -1 for absorbed rays
     */
    public static int process(Board board, int inputPoint) {

        if(inputPoint < 1 || inputPoint > 54) {
            throw new IllegalArgumentException("Invalid input point");
        }

        Cell[][] cells = board.getCells();

        Cell cell = inputPointToCell(cells, inputPoint);            //get the first cell based on the input point
        int entryPoint = inputPointToEntryPoint(inputPoint);      //get the entry point to first cell based on the input point
        int exitPoint;

        /*
        The algorithm for ray processing progressively advances the ray through the board by taking into
        account, upon entering each cell, only the 3 positions opposite to the entry point to that cell.
        Except for the edge case where there is an atom at the edge of the board and the ray enters the board
        directly into its circle of influence, the other 3 neighbour positions of the current cell can't
        contain atoms, or else the ray wouldn't have entered this cell.
         */

        /*
                HANDLE EDGE OF THE BOARD EDGE CASE HERE (later)
         */

        while(true) {

            exitPoint = computeExitPoint(cell, entryPoint);

            cell.addRaySegment(entryPoint, exitPoint);      //add information about the ray traversing this cell to this cell's raySegments ArrayList to display the ray later

            if(exitPoint == -1) {      //exit loop and return if the ray is absorbed
                return -1;
            }
            Cell next = cell.getNeighbours()[exitPoint];

            if(next == null) {       //exit loop if we exit the board
                break;
            } else {
                cell = next;     //move cell to the next cell on the ray path
            }
            entryPoint = (exitPoint + 3) % 6;     //calculate entry point to the next cell based on exit point from current cell

        }

        return cellToOutputPoint(cell, exitPoint);
    }

    // Computes the exit point of a ray from a cell based on the entry point to that cell and whether the
    // neighbouring cells have atoms or not; returns the exit point (or -1 for absorbed rays)
    private static int computeExitPoint(Cell cell, int entryPoint) {
        //get the 3 neighbours opposite to the entry point in clockwise direction
        Cell neighbour1 = cell.getNeighbours()[(entryPoint + 2) % 6];
        Cell neighbour2 = cell.getNeighbours()[(entryPoint + 3) % 6];
        Cell neighbour3 = cell.getNeighbours()[(entryPoint + 4) % 6];

        boolean atomAtNeighbour1 =  neighbour1 != null && neighbour1.hasAtom();
        boolean atomAtNeighbour2 =  neighbour2 != null && neighbour2.hasAtom();
        boolean atomAtNeighbour3 =  neighbour3 != null && neighbour3.hasAtom();

        //ray is absorbed
        if(cell.hasAtom()) {
            return -1;
        }

        //no atom encountered case or ray is absorbed on the next iteration
        else if( !atomAtNeighbour1 && !atomAtNeighbour3) {
            return (entryPoint + 3) % 6;
        }

        //ray is reflected
        else if( atomAtNeighbour1 && atomAtNeighbour3) {
            return entryPoint;
        }

        //60 degrees and 120 degrees deflection cases
        //deflection to the left
        else if( atomAtNeighbour3 ) {
            if(atomAtNeighbour2) {  //120 degrees
                return (entryPoint + 1) % 6;
            } else {                //60 degrees
                return (entryPoint + 2) % 6;
            }
        }

        //deflection to the right ( atomAtNeighbour1 )
        else {
            if(atomAtNeighbour2) {  //120 degrees
                return (entryPoint + 5) % 6;
            } else {                //60 degrees
                return (entryPoint + 4) % 6;
            }
        }
    }

    //Figures out the starting cell associated with the input point number
    private static Cell inputPointToCell(Cell[][] cells, int inputPoint) {

        if(inputPoint <= 10) {              //upper-left
            return cells[(inputPoint-1) / 2][0];
        } else if(inputPoint <= 19) {       //lower-left
            return cells[(inputPoint-2) / 2][0];
        } else if(inputPoint <= 28) {       //lower
            return cells[Board.BOARD_SIZE-1][(inputPoint-19) / 2];
        } else if(inputPoint <= 37) {       //lower-right
            int row = (45 - inputPoint) / 2;
            return cells[row][12-row];
        } else if(inputPoint <= 46){        //upper-right
            int row = (46 - inputPoint) / 2;
            return cells[row][row+4];
        } else {                            //upper
            return cells[0][(55-inputPoint) / 2];
        }

    }

    //Figures out the entry point to the starting cell based on the input point number
    private static int inputPointToEntryPoint(int inputPoint) {

        if((inputPoint <= 9 || inputPoint >= 47) && inputPoint % 2 == 1 ) {
            return 0;   //upper-left entry point
        }
        if(inputPoint >= 2 && inputPoint <= 18 && inputPoint % 2 == 0 ) {
            return 5;   //left entry point
        }
        if(inputPoint >= 11 && inputPoint <= 27 && inputPoint % 2 == 1 ) {
            return 4;   //lower-left entry point
        }
        if(inputPoint >= 20 && inputPoint <= 36 && inputPoint % 2 == 0 ) {
            return 3;   //lower-right entry point
        }
        if(inputPoint >= 29 && inputPoint <= 45 && inputPoint % 2 == 1 ) {
            return 2;   //right entry point
        }
        return 1;       //upper-right entry point
    }

    //Figures out the output point number associated with the final cell
    private static int cellToOutputPoint(Cell last, int exitPoint) {

        if(exitPoint == 5) {     //exits the board through the left
            return last.getRow() * 2 + 2;
        }
        if(exitPoint == 4) {     //exits the board through the lower-left
            return (last.getRow() + last.getCol()) * 2 + 3;
        }
        if(exitPoint == 3) {     //exits the board through the lower-right
            return last.getCol() * 2 + 20;
        }
        if(exitPoint == 2) {     //exits the board through the right
            return 45 - last.getRow() * 2;
        }
        if(exitPoint == 1) {     //exits the board through the upper-right
            return 54 - last.getCol() * 2;
        }

        //exits the board through the upper-left
        if(last.getNeighbours()[5] == null) {    //left side ( + corner)
            return last.getRow() * 2 + 1;
        } else {                                        //upper side
            return 55 - last.getCol() * 2;
        }
    }
}



