import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
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
}