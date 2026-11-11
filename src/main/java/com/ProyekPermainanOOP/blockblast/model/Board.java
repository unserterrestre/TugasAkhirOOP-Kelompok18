package src.main.java.com.ProyekPermainanOOP.blockblast.model;

public class Board {
    private final int width;
    private final int height;
    private final Block[][] grid;

    public Board(int width, int height) {
        this.width = width;
        this.height = height;
        this.grid = new Block[width][height];
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    // Mengambil blok pada koordinat tertentu
    public Block getBlock(int x, int y) {
        if (isValidPosition(x, y)) {
            return grid[x][y];
        }
        return null;
    }

    // Menempatkan blok ke koordinat grid
    public void setBlock(int x, int y, Block block) {
        if (isValidPosition(x, y)) {
            grid[x][y] = block;
            if (block != null) {
                block.setX(x);
                block.setY(y);
            }
        }
    }

    // Validasi apakah koordinat berada di dalam area board
    public boolean isValidPosition(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    // Mengecek apakah suatu sel di grid masih kosong
    public boolean isCellEmpty(int x, int y) {
        return isValidPosition(x, y) && grid[x][y] == null;
    }

    // Membersihkan seluruh isi papan permainan
    public void clearBoard() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                grid[x][y] = null;
            }
        }
    }
}