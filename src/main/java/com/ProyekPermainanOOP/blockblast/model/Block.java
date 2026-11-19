package src.main.java.com.ProyekPermainanOOP.blockblast.model;

/**
 - Merepresentasikan bentuk balok puzzle 2D dengan matriks 0 dan 1.
 - Bentuk bersifat immutable (tidak dapat diputar maupun dimodifikasi dari luar).
 */
public class Block {
    private final int[][] shape;
    private final int rows;
    private final int cols;
    private final int cellCount;
    private final int tileId;
    private final int[][] tileGrid;
    private boolean active;

    public Block(int[][] shape) {
        this(shape, 1);
    }

    public Block(int[][] shape, int tileId) {
        if (shape == null || shape.length == 0) {
            throw new IllegalArgumentException("Shape balok tidak boleh null atau kosong.");
        }
        if (tileId <= 0) {
            throw new IllegalArgumentException("Tile ID balok harus bernilai positif.");
        }

        int expectedCols = -1;
        int activeCells = 0;

        for (int r = 0; r < shape.length; r++) {
            if (shape[r] == null || shape[r].length == 0) {
                throw new IllegalArgumentException("Baris pada shape balok tidak boleh null atau kosong.");
            }
            if (expectedCols == -1) {
                expectedCols = shape[r].length;
            } else if (shape[r].length != expectedCols) {
                throw new IllegalArgumentException("Shape balok harus berbentuk matriks persegi panjang.");
            }

            for (int c = 0; c < shape[r].length; c++) {
                int val = shape[r][c];
                if (val != 0 && val != 1) {
                    throw new IllegalArgumentException("Nilai sel balok hanya boleh 0 atau 1.");
                }
                if (val == 1) {
                    activeCells++;
                }
            }
        }

        if (activeCells == 0) {
            throw new IllegalArgumentException("Balok harus memiliki minimal satu sel aktif (bernilai 1).");
        }

        this.rows = shape.length;
        this.cols = expectedCols;
        this.cellCount = activeCells;
        this.tileId = tileId;
        this.shape = deepCopy(shape);
        this.tileGrid = buildTileGrid(this.shape, tileId);
        this.active = true;
    }

    /**
     - Mengembalikan salinan mendalam (deep copy) dari matriks bentuk balok
     - agar pemanggil tidak dapat memodifikasi state internal objek.
     */
    public int[][] getShape() {
        return deepCopy(this.shape);
    }

    public int[][] getTileGrid() {
        return deepCopy(this.tileGrid);
    }

    public int getTileId() {
        return tileId;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public int getCellCount() {
        return cellCount;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    private static int[][] buildTileGrid(int[][] shape, int tileId) {
        int[][] grid = new int[shape.length][shape[0].length];
        for (int r = 0; r < shape.length; r++) {
            for (int c = 0; c < shape[r].length; c++) {
                grid[r][c] = (shape[r][c] == 1) ? tileId : 0;
            }
        }
        return grid;
    }

    private static int[][] deepCopy(int[][] source) {
        int[][] copy = new int[source.length][];
        for (int i = 0; i < source.length; i++) {
            copy[i] = source[i].clone();
        }
        return copy;
    }
}