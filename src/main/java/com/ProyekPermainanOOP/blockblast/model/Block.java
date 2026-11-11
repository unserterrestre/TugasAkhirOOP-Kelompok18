package src.main.java.com.ProyekPermainanOOP.blockblast.model;

public class Block {
    private int[][] shape;
    private boolean active;

    public Block() {
        this.active = true;
    }

    public Block(int[][] shape) {
        this.shape = shape;
        this.active = true;
    }

    public void init(int[][] shape) {
        this.shape = shape;
        this.active = true;
    }

    public int[][] getShape() {
        return shape;
    }

    public void setShape(int[][] shape) {
        this.shape = shape;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}