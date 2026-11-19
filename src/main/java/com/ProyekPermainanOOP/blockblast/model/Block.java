package src.main.java.com.ProyekPermainanOOP.blockblast.model;

/**
 * Merepresentasikan bentuk balok puzzle 2D dengan matriks 0 dan 1.
 * Bersifat immutable dan tidak dapat diputar sesuai aturan BAB II proposal.
 */
public class Block {
    private final int[][] shape;
    private final int rows;
    private final int cols;
    private final int cellCount;

    public Block(int[][] shape) {
        if (shape == null || shape.length == 0) {
            throw new IllegalArgumentException("Shape balok tidak boleh null atau kosong.");
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
        this.shape = deepCopy(shape);
    }

    public int[][] getShape() {
        return deepCopy(this.shape);
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

    private static int[][] deepCopy(int[][] source) {
        int[][] copy = new int[source.length][];
        for (int i = 0; i < source.length; i++) {
            copy[i] = source[i].clone();
        }
        return copy;
    }
}
