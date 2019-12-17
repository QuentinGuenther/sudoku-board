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
    void isSolvedShouldReturnFalseWithUnsolvedBoard() {
        board.generateMatrix();
        assertFalse(board.isSolved());
    }

    @Nested
    class MakeMove {

        @Test
        void makingValidMoveReturnsValid() {
            board.generateMatrix();

            assertEquals(MoveType.VALID,
                            board.update(1, 1, 1));
        }

        @Test
        void makingMoveOfDuplicateValueReturnsDuplicateMoveType() {
            board.generateMatrix();
            board.update(0, 1, 1);

            assertEquals(MoveType.DUPLICATE_VALUE_IN_ADJACENT_NODES,
                            board.update(1, 1, 1));
        }

        @Test
        void connectedNodesShouldRecogniseStateChange() {
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
    }

    @Nested
    class IsValidMove {

        @Nested
        class Valid {

            @Test
            void shouldReturnValidIfNotDuplicateValueAndInBounds() {
                board.generateMatrix();
                for(int i = 0; i < board.LENGTH; i++) {
                    for(int j = 0; j < board.LENGTH; j++) {
                        for(int k = 1; k <= board.LENGTH; k++) {
                            assertEquals(MoveType.VALID, board.isValid(i, j, k));
                        }
                    }
                }
            }
        }

        @Nested
        class OutOfBounds {

            @Test
            void shouldReturnOutOfBoundsIfXIsLessThanZero () {
                assertEquals(MoveType.INVALID_OUT_OF_BOUNDS, board.isValid(-1, 0, 2));
            }

            @Test
            void shouldReturnOutOfBoundsIfXIsGreaterThanLength () {
                assertEquals(MoveType.INVALID_OUT_OF_BOUNDS, board.isValid(9, 0, 2));
            }

            @Test
            void shouldReturnOutOfBoundsIfYIsLessThanZero () {
                assertEquals(MoveType.INVALID_OUT_OF_BOUNDS, board.isValid(0, 9, 2));
            }

            @Test
            void shouldReturnOutOfBoundsIfYIsGreaterThanLength () {
                assertEquals(MoveType.INVALID_OUT_OF_BOUNDS, board.isValid(0, 9, 2));
            }
        }

        @Nested
        class InvalidValue {

            @Test
            void shouldReturnInvalidValueIfValueIsLessThanOne() {
                assertEquals(MoveType.INVALID_VALUE, board.isValid(0, 0, 0));
            }

            @Test
            void shouldReturnInvalidValueIfValueIsGreaterThanLength() {
                assertEquals(MoveType.INVALID_VALUE, board.isValid(0, 0, 10));
            }
        }

        @Nested
        class DuplicateValue {

            @BeforeEach
            void setUp() {
                board.generateMatrix();
            }

            @Test
            void shouldReturnDuplicateIfDuplicateValue() {
                board.update(1,0, 2);
                assertEquals(MoveType.DUPLICATE_VALUE_IN_ADJACENT_NODES, board.isValid(0,0,2));
            }

            @Test
            void shouldNotReturnDuplicateIfValueIsNotAdjacent() {
                board.update(3,1, 2);
                assertNotEquals(MoveType.DUPLICATE_VALUE_IN_ADJACENT_NODES, board.isValid(0,0,2));
            }
        }
    }
}