import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class NoAtomsTest {
    Board board;

    // test fixture
    @BeforeEach
    void constructBoard() {
        board = new Board();
        Cell[][] cells = board.getCells();
        cells[2][0].setAtom();
        cells[4][4].setAtom();
    }

    @Test
    void testNoAtoms() {
        assertEquals(50, Ray.process(board, 15));
        assertEquals(33, Ray.process(board, 14));
    }
}

class AbsorbedTest {
    Board board;

    // test fixture
    @BeforeEach
    void constructBoard() {
        board = new Board();
        Cell[][] cells = board.getCells();
        cells[2][0].setAtom();
        cells[4][4].setAtom();
    }

    @Test
    void testAbsorbed() {
        assertEquals(-1, Ray.process(board, 11));
        assertEquals(-1, Ray.process(board, 1));
    }
}

class ReflectionTest {
    Board board;

    // test fixture
    @BeforeEach
    void constructBoard() {
        board = new Board();
        Cell[][] cells = board.getCells();
        cells[3][0].setAtom();
        cells[5][0].setAtom();
        cells[3][3].setAtom();
        cells[4][4].setAtom();
        cells[4][5].setAtom();
    }

    @Test
    void testReflected() {
        assertEquals(10, Ray.process(board, 10));
        assertEquals(46, Ray.process(board, 46));
    }
}

class Deviation60Test {
    Board board;

    // test fixture
    @BeforeEach
    void constructBoard() {
        board = new Board();
        Cell[][] cells = board.getCells();
        cells[6][1].setAtom();
        cells[1][1].setAtom();
        cells[5][5].setAtom();
        cells[7][3].setAtom();
    }

    @Test
    void testSingleDeflection() {
        assertEquals(32, Ray.process(board, 40));
        assertEquals(18, Ray.process(board, 24));
    }

    @Test
    void testMultipleDeflections() {
        assertEquals(12, Ray.process(board, 41));
    }
}

class Deviation120Test {
    Board board;

    // test fixture
    @BeforeEach
    void constructBoard() {
        board = new Board();
        Cell[][] cells = board.getCells();
        cells[3][1].setAtom();
        cells[3][5].setAtom();
        cells[3][6].setAtom();
        cells[3][0].setAtom();
        cells[6][3].setAtom();
        cells[7][3].setAtom();
    }

    @Test
    void testSingleDeflection() {
        assertEquals(54, Ray.process(board, 5));
    }

    @Test
    void testMultipleDeflections() {
        assertEquals(32, Ray.process(board, 33));
    }
}

class EdgeTest {
    Board board;

    // test fixture
    @BeforeEach
    void constructBoard() {
        board = new Board();
        Cell[][] cells = board.getCells();
        cells[0][4].setAtom();
        cells[4][0].setAtom();
        cells[5][0].setAtom();
    }

    @Test
    void testEdge() {
        assertEquals(12, Ray.process(board, 12));
        assertEquals(44, Ray.process(board, 44));
    }
}

class ComplexTest {
    Board board;

    // test fixture
    @BeforeEach
    void constructBoard() {
        board = new Board();
        Cell[][] cells = board.getCells();
        cells[0][0].setAtom();
        cells[1][2].setAtom();
        cells[4][0].setAtom();
        cells[7][1].setAtom();
        cells[6][2].setAtom();
    }

    @Test
    void testComplex() {
        assertEquals(14, Ray.process(board, 14));
    }
}

class SymmetryTest {
    Board board;

    // test fixture
    @BeforeEach
    void constructBoard() {
        board = new Board();
        Cell[][] cells = board.getCells();
        cells[1][2].setAtom();
        cells[4][0].setAtom();
        cells[7][1].setAtom();
        cells[6][2].setAtom();
    }

    @Test
    void testSymmetric() {
        assertEquals(14, Ray.process(board, 1));
        assertEquals(1, Ray.process(board, 14));
    }
}

class RaySegmentTest {
    Board board;

    // test fixture
    @BeforeEach
    void sendRays() {
        board = new Board();
        Ray.process(board, 52);
        Ray.process(board, 6);
    }

    @Test
    void testRaySegments() {

        Cell testCell = board.getCells()[2][1];
        ArrayList<RaySegment> raySegments = testCell.getRaySegments();

        assertTrue(raySegments.contains(new RaySegment(1, 4)));
        assertTrue(raySegments.contains(new RaySegment(5, 2)));

    }

}
