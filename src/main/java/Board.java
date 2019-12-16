import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Board {
    public final int LENGTH = 9;
    public Node[][] matrix = new Node[LENGTH][LENGTH];

    public void update(int x, int y, int value) {
        matrix[x][y].setData(value);
    }

    public void isValid(int x, int y, int value) {

    }

    public boolean isSolved() {
        return false;
    }

    public Map<Node, Collection<Node>> getState() {
        Map<Node, Collection<Node>> matrixList = new HashMap<>();

        for (Node[] nodeArr : matrix){
            for(Node node : nodeArr) {
                matrixList.put(node, node.getConnections().values());
            }
        }

        return Collections.unmodifiableMap(matrixList);
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
            node.addConnection(matrix[index][i]);
        }
    }

    private void appendVertical(Node node, int index) {
        for (Node[] nodes : matrix) {
            node.addConnection(nodes[index]);
        }
    }

    private void appendSquare(Node node, int row, int col) {
        final double SQUARE_SIZE = Math.sqrt(LENGTH);

        int r = row - row % 3;
        int c = col - col % 3;

        for (int i = r; i < r + 3; i++) {
            for (int j = c; j < c + 3; j++) {
                node.addConnection(matrix[i][j]);
            }
        }
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
