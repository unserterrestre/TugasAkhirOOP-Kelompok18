package src.main.java.com.ProyekPermainanOOP.blockblast.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Block {
    private static final int[] UNIFONT_CODE_POINTS = {
            0x4E0A,
            0x4E0B,
            0x4E16,
            0x4E2D,
            0x4E4B,
            0x4E66,
            0x4E70,
            0x4E8B,
            0x4E91,
            0x4EAC
    };

    private final int[][] shape;
    private final int rows;
    private final int cols;
    private final int cellCount;
    private final int tileId;
    private final int[][] tileGrid;
    private boolean active;
    private boolean selected;
    private boolean highlighted;

    public Block(int[][] shape) {
        this(shape, 1);
    }

    public Block(int[][] shape, int tileId) {
        this(shape, tileId, buildUniformTileGrid(validateAndNormalizeShape(shape), validateTileId(tileId)));
    }

    public Block(int[][] shape, int[][] customTileGrid) {
        this(shape, resolvePrimaryTileId(validateAndNormalizeShape(shape), customTileGrid), customTileGrid);
    }

    private Block(int[][] shape, int tileId, int[][] customTileGrid) {
        int[][] validShape = validateAndNormalizeShape(shape);
        int validTileId = validateTileId(tileId);
        int[][] validTileGrid = validateTileGrid(validShape, customTileGrid);

        this.rows = validShape.length;
        this.cols = validShape[0].length;
        this.cellCount = countActiveCells(validShape);
        this.tileId = validTileId;
        this.shape = deepCopy(validShape);
        this.tileGrid = deepCopy(validTileGrid);
        this.active = true;
        this.selected = false;
        this.highlighted = false;
    }

    public int[][] getShape() {
        return deepCopy(this.shape);
    }

    public int[][] getTileGrid() {
        return deepCopy(this.tileGrid);
    }

    public int getTileId() {
        return tileId;
    }

    public int getTileIdAt(int row, int col) {
        if (row < 0 || row >= rows || col < 0 || col >= cols) {
            return 0;
        }
        return tileGrid[row][col];
    }

    public int getTileCodePoint() {
        return getCodePointForTileId(this.tileId);
    }

    public String getTileCharacter() {
        return new String(Character.toChars(getTileCodePoint()));
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

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isHighlighted() {
        return highlighted;
    }

    public void setHighlighted(boolean highlighted) {
        this.highlighted = highlighted;
    }

    public boolean isCellOccupied(int row, int col) {
        return row >= 0 && row < rows && col >= 0 && col < cols && shape[row][col] == 1;
    }

    public List<int[]> getOccupiedCoords() {
        List<int[]> coords = new ArrayList<>();
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (shape[r][c] == 1) {
                    coords.add(new int[]{r, c});
                }
            }
        }
        return Collections.unmodifiableList(coords);
    }

    public Block rotatedCW() {
        int[][] newShape = new int[cols][rows];
        int[][] newTileGrid = new int[cols][rows];

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                newShape[c][rows - 1 - r] = shape[r][c];
                newTileGrid[c][rows - 1 - r] = tileGrid[r][c];
            }
        }
        return new Block(newShape, this.tileId, newTileGrid);
    }

    public Block mirroredHorizontal() {
        int[][] newShape = new int[rows][cols];
        int[][] newTileGrid = new int[rows][cols];

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                int targetCol = cols - 1 - c;
                newShape[r][targetCol] = shape[r][c];
                newTileGrid[r][targetCol] = tileGrid[r][c];
            }
        }
        return new Block(newShape, this.tileId, newTileGrid);
    }

    public static int getCodePointForTileId(int id) {
        int safeIndex = Math.max(0, Math.min(id - 1, UNIFONT_CODE_POINTS.length - 1));
        return UNIFONT_CODE_POINTS[safeIndex];
    }

    private static int validateTileId(int tileId) {
        if (tileId <= 0) {
            throw new IllegalArgumentException("Tile ID balok harus bernilai positif.");
        }
        return tileId;
    }

    private static int[][] validateAndNormalizeShape(int[][] shape) {
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

        return deepCopy(shape);
    }

    private static int[][] validateTileGrid(int[][] validShape, int[][] grid) {
        if (grid == null || grid.length != validShape.length) {
            throw new IllegalArgumentException("Tile grid harus memiliki jumlah baris yang sama dengan shape.");
        }

        int cols = validShape[0].length;
        int[][] copy = new int[validShape.length][cols];

        for (int r = 0; r < validShape.length; r++) {
            if (grid[r] == null || grid[r].length != cols) {
                throw new IllegalArgumentException("Tile grid harus memiliki jumlah kolom yang sama dengan shape.");
            }
            for (int c = 0; c < cols; c++) {
                if (validShape[r][c] == 0) {
                    copy[r][c] = 0;
                } else {
                    if (grid[r][c] <= 0) {
                        throw new IllegalArgumentException("Sel aktif pada tile grid harus memiliki tile ID positif.");
                    }
                    copy[r][c] = grid[r][c];
                }
            }
        }
        return copy;
    }

    private static int resolvePrimaryTileId(int[][] validShape, int[][] customTileGrid) {
        if (customTileGrid != null) {
            for (int r = 0; r < validShape.length; r++) {
                if (customTileGrid[r] != null) {
                    for (int c = 0; c < validShape[r].length; c++) {
                        if (validShape[r][c] == 1 && r < customTileGrid.length && c < customTileGrid[r].length && customTileGrid[r][c] > 0) {
                            return customTileGrid[r][c];
                        }
                    }
                }
            }
        }
        return 1;
    }

    private static int[][] buildUniformTileGrid(int[][] validShape, int tileId) {
        int[][] grid = new int[validShape.length][validShape[0].length];
        for (int r = 0; r < validShape.length; r++) {
            for (int c = 0; c < validShape[r].length; c++) {
                grid[r][c] = (validShape[r][c] == 1) ? tileId : 0;
            }
        }
        return grid;
    }

    private static int countActiveCells(int[][] validShape) {
        int count = 0;
        for (int[] row : validShape) {
            for (int val : row) {
                if (val == 1) {
                    count++;
                }
            }
        }
        return count;
    }

    private static int[][] deepCopy(int[][] source) {
        int[][] copy = new int[source.length][];
        for (int i = 0; i < source.length; i++) {
            copy[i] = source[i].clone();
        }
        return copy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Block)) {
            return false;
        }
        Block other = (Block) o;
        return tileId == other.tileId
                && Arrays.deepEquals(shape, other.shape)
                && Arrays.deepEquals(tileGrid, other.tileGrid);
    }

    @Override
    public int hashCode() {
        int result = Integer.hashCode(tileId);
        result = 31 * result + Arrays.deepHashCode(shape);
        result = 31 * result + Arrays.deepHashCode(tileGrid);
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Block{tileId=").append(tileId)
                .append(", char='").append(getTileCharacter()).append("'")
                .append(", size=").append(rows).append("x").append(cols)
                .append(", active=").append(active).append("}\n");
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                sb.append(shape[r][c] == 1 ? '#' : '.');
                if (c < cols - 1) {
                    sb.append(' ');
                }
            }
            if (r < rows - 1) {
                sb.append('\n');
            }
        }
        return sb.toString();
    }
}