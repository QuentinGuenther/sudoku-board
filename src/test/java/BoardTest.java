import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class BoardTest {

    private Board board;

    @BeforeEach
    void setUp() {
        board = new Board();
    }

    @Test
    void nodesShouldNotShareHashcode() {
        Board.Node node1 = new Board.Node();
        Board.Node node2 = new Board.Node();

        assertAll(() -> {
            assertNotEquals(node1, node2);
            assertNotEquals(node1.hashCode(), node2.hashCode());
        });

    }

    @Test
    void matrixShouldGenerateWithConnectedNodes() {
        board.generateMatrix();

        assertAll(() -> {
            assertTrue(board.matrix[0][0].getConnections().containsValue(board.matrix[0][1]));
            assertTrue(board.matrix[0][0].getConnections().containsValue(board.matrix[1][0]));
            assertTrue(board.matrix[0][0].getConnections().containsValue(board.matrix[1][0]));
            assertFalse(board.matrix[0][0].getConnections().containsValue(board.matrix[1][4]));
        });
    }

    @Test
    void connectedNotesShouldRecogniseStateChange() {
        board.generateMatrix();
        board.update(0, 1, 1);

        assertEquals(1, board.matrix[0][1].getData());
        assertTrue(board.matrix[0][0].isConnectedToValue(1));
        assertFalse(board.matrix[0][0].isConnectedToValue(2));
    }

    @Test
    void updateShouldThrowExceptionForValuesOutsideOfRange() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
            board.update(-1, 0, 0)
        );

        assertTrue(exception.getMessage()
            .contains("Unable to use arguments: x=-1 (0-8) y=0 (0-8) value=0 (1-9)"),
            exception.getMessage());
    }

    @Test
    void isSolvedShouldReturnFalseWithUnsolvedBoard() {
        board.generateMatrix();
        assertFalse(board.isSolved());
    }

    @Nested
    class IsValidMove {

        @Nested
        class Valid {

            @Test
            void shouldReturnValidIfNotDuplicateValueAndInBounds() {
                board.generateMatrix();
                assertEquals(MoveType.VALID, board.isValid(0, 0, 2));
            }
        }

        @Nested
        class OutOfBounds {

            @Test
            void shouldReturnOutOfBoundsIfXIsLessThanZero () {
                board.generateMatrix();
                assertEquals(MoveType.INVALID_OUT_OF_BOUNDS, board.isValid(-1, 0, 2));
            }

            @Test
            void shouldReturnOutOfBoundsIfXIsGreaterThanLength () {
                board.generateMatrix();
                assertEquals(MoveType.INVALID_OUT_OF_BOUNDS, board.isValid(9, 0, 2));
            }

            @Test
            void shouldReturnOutOfBoundsIfYIsLessThanZero () {
                board.generateMatrix();
                assertEquals(MoveType.INVALID_OUT_OF_BOUNDS, board.isValid(0, 9, 2));
            }

            @Test
            void shouldReturnOutOfBoundsIfYIsGreaterThanLength () {
                board.generateMatrix();
                assertEquals(MoveType.INVALID_OUT_OF_BOUNDS, board.isValid(0, 9, 2));
            }
        }

        @Nested
        class InvalidValue {

            @Test
            void shouldReturnInvalidValueIfValueIsLessThanOne() {
                board.generateMatrix();
                assertEquals(MoveType.INVALID_VALUE, board.isValid(0, 0, 0));
            }

            @Test
            void shouldReturnInvalidValueIfValueIsGreaterThanLength() {
                board.generateMatrix();
                assertEquals(MoveType.INVALID_VALUE, board.isValid(0, 0, 10));
            }
        }

        @Nested
        class DuplicateValue {
            @Test
            void shouldReturnDuplicateIfDuplicateValue() {
                board.generateMatrix();
                board.update(1,0, 2);
                assertEquals(MoveType.DUPLICATE_VALUE_IN_ADJACENT_NODES, board.isValid(0,0,2));
            }
        }
    }
}