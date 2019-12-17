import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Board {
    public final int LENGTH = 9;
    public Node[][] matrix = new Node[LENGTH][LENGTH];

    public MoveType update(int x, int y, int value) {
        if(x < 0 || x >= LENGTH ||
                y < 0 || y >= LENGTH ||
                value < 1 || value > LENGTH) {
            throw new IllegalArgumentException("Unable to use arguments: " +
                                                "x=" + x + " (0-" + (LENGTH - 1) + ") " +
                                                "y=" + y + " (0-" + (LENGTH - 1) + ") " +
                                                "value=" + value + " (1-" + LENGTH + ")");
        }

        matrix[x][y].setData(value);

        if(matrix[x][y].isConnectedToValue(value)) {
            return MoveType.DUPLICATE_VALUE_IN_ADJACENT_NODES;
        } else {
            return MoveType.VALID;
        }
    }

    public MoveType isValid(int x, int y, int value) {
        if(x < 0 || x >= LENGTH || y < 0 || y >= LENGTH) {
            return MoveType.INVALID_OUT_OF_BOUNDS;
        }

        if(value < 1 || value > LENGTH) {
            return MoveType.INVALID_VALUE;
        }

        if(matrix[x][y].isConnectedToValue(value)) {
            return MoveType.DUPLICATE_VALUE_IN_ADJACENT_NODES;
        }

        return MoveType.VALID;
    }

    public boolean isSolved() {
        for(Node[] nodes: matrix) {
            for(Node node: nodes) {
                if(node.getData() < 1 || node.getData() > LENGTH || node.isConnectedToValue(node.data)) {
                    return false;
                }
            }
        }

        return true;
    }

    public void generateMatrix() {
        for (int i = 0; i < matrix.length; i++){
            for(int j = 0; j < matrix[i].length; j++) {
                matrix[i][j] = new Node();
            }
        }

        for(int i = 0; i < matrix.length; i++) {
            for(int j = 0; j < matrix[i].length; j++) {
                appendHorizontal(matrix[i][j], i);
                appendVertical(matrix[i][j], j);
                appendSquare(matrix[i][j], i, j);
            }
        }
    }

    private void appendHorizontal(Node node, int index) {
        for(int i = 0; i < matrix[index].length; i++) {
            if(!node.equals(matrix[index][i])) {
                node.addConnection(matrix[index][i]);
            }
        }
    }

    private void appendVertical(Node node, int index) {
        for (Node[] nodes : matrix) {
            if(!node.equals(nodes[index])) {
                node.addConnection(nodes[index]);
            }
        }
    }

    private void appendSquare(Node node, int row, int col) {
        final double SQUARE_SIZE = Math.sqrt(LENGTH);

        int r = (int)(row - row % SQUARE_SIZE);
        int c = (int)(col - col % SQUARE_SIZE);

        for (int i = r; i < r + SQUARE_SIZE; i++) {
            for (int j = c; j < c + SQUARE_SIZE; j++) {
                if(!node.equals(matrix[i][j])) {
                    node.addConnection(matrix[i][j]);
                }
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        for(int i = 0; i < LENGTH; i ++) {
            for(int j = 0; j < LENGTH; j++) {
                stringBuilder.append(matrix[i][j].getData());
                stringBuilder.append('|');
            }
            stringBuilder.append('\n');
        }
        return stringBuilder.toString();
    }

    public static class Node {
        private int data;
        private Map<Integer, Node> connections;

        public Node() {
            connections = new HashMap<>();
        }

        public int getData() {
            return data;
        }

        public void setData(int data) {
            this.data = data;
        }

        public boolean isConnectedToValue(int value) {
            for(Node node : connections.values()) {
                if(node.getData() == value) {
                    return true;
                }
            }

            return false;
        }

        public Map<Integer, Node> getConnections() {
            return this.connections;
        }

        public void addConnection(Node node) {
            this.connections.put(node.hashCode(), node);
        }
    }
}
