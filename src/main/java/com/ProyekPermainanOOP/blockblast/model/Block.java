package src.main.java.com.ProyekPermainanOOP.blockblast.model;

public class Block {
    private int x;
    private int y;
    private int type; // Bisa merepresentasikan warna atau ID jenis blok
    private boolean active;

    public Block(int x, int y, int type) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.active = true;
    }

    // Getters and Setters
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    // Metode helper untuk pergerakan blok
    public void move(int deltaX, int deltaY) {
        this.x += deltaX;
        this.y += deltaY;
    }
}