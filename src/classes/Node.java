package classes;

import java.util.ArrayList;

public class Node {
    private Node leftNode;
    private Node rightNode;
    private Node parentNode;

    boolean isRoot = false;
    boolean isVisited = false;

    ArrayList<Boolean> states;
    ArrayList<Character> terms;

    public Node getLeftNode() {
        return leftNode;
    }

    public void setLeftNode(Node leftNode) {
        this.leftNode = leftNode;
    }

    public Node getRightNode() {
        return rightNode;
    }

    public void setRightNode(Node rightNode) {
        this.rightNode = rightNode;
    }

    public Node getParentNode() {
        return parentNode;
    }

    public void setParentNode(Node parentNode) {
        this.parentNode = parentNode;
    }

}
