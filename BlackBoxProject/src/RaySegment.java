public record RaySegment(int entryPoint, int exitPoint) {
    /*  entryPoint stores the entry position of the ray in this cell:
        - 0 for entering through the upper-left side
        - 1 for entering through the upper-right side
        ...and so on going clockwise
        - 5 for entering through the left side
        (This encoding is the same as the one used for storing the neighbours of the cells)

        exitPoint stores the exit position of the ray from this cell, with an identical encoding, except it
        is -1 if the ray is absorbed (and doesn't exit the cell)

     */
}
