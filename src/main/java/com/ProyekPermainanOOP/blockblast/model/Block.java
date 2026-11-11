package src.main.java.com.ProyekPermainanOOP.blockblast.model;

public class Block {
    private int[][] shape;

    public Block() {
    }

    public Block(int[][] shape) {
        this.shape = shape;
    }

    public void init(int[][] shape) {
        this.shape = shape;
    }

    public int[][] getShape() {
        return shape;
    }
}