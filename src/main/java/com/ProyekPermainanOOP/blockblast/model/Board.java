package com.ProyekPermainanOOP.blockblast.model;

import com.ProyekPermainanOOP.blockblast.synergy.HeavenEarthSynergy;
import com.ProyekPermainanOOP.blockblast.synergy.ScholarCloudSynergy;
import com.ProyekPermainanOOP.blockblast.synergy.SynergyHandler;
import com.ProyekPermainanOOP.blockblast.synergy.SynergyResult;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.Set;

public class Board {
    public static final int SIZE = 8;
    private static final int MIN_LINE_LENGTH = 4;
    private static final int MIN_CLUSTER_SIZE = 5;

    private final int[][] grid;
    private final int[][] placementGrid;
    private final CellSealState[][] sealGrid;
    private final Map<SpecialTileType, SpecialTileStrategy> strategies;
    private final SynergyHandler synergyChain;
    private final Random random;
    private int nextPlacementId;

    public Board() {
        this.grid = new int[SIZE][SIZE];
        this.placementGrid = new int[SIZE][SIZE];
        this.sealGrid = new CellSealState[SIZE][SIZE];
        this.random = new Random();
        this.nextPlacementId = 1;

        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                this.sealGrid[r][c] = CellSealState.NORMAL;
            }
        }

        this.strategies = new EnumMap<>(SpecialTileType.class);
        this.strategies.put(SpecialTileType.DRAGON_VEIN, new DragonVeinStrategy());
        this.strategies.put(SpecialTileType.TALISMAN_BOMB, new TalismanBombStrategy());

        SynergyHandler heavenEarth = new HeavenEarthSynergy();
        heavenEarth.setNext(new ScholarCloudSynergy());
        this.synergyChain = heavenEarth;
    }

    public int getSize() {
        return SIZE;
    }

    public int[][] getGrid() {
        return getGridSnapshot();
    }

    public int[][] getGridSnapshot() {
        int[][] snapshot = new int[SIZE][SIZE];
        for (int r = 0; r < SIZE; r++) {
            snapshot[r] = grid[r].clone();
        }
        return snapshot;
    }

    public boolean canPlaceBlock(Block block, int col, int row) {
        return canPlace(block, row, col);
    }

    public boolean canPlace(Block block, int row, int col) {
        if (block == null) {
            return false;
        }

        int[][] shape = block.getShape();
        int blockRows = block.getRows();
        int blockCols = block.getCols();

        for (int r = 0; r < blockRows; r++) {
            for (int c = 0; c < blockCols; c++) {
                if (shape[r][c] == 1) {
                    int targetRow = row + r;
                    int targetCol = col + c;

                    if (targetRow < 0 || targetRow >= SIZE || targetCol < 0 || targetCol >= SIZE) {
                        return false;
                    }
                    if (grid[targetRow][targetCol] != 0) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public boolean placeBlock(Block block, int row, int col) {
        if (!canPlace(block, row, col)) {
            return false;
        }

        int[][] shape = block.getShape();
        int[][] tileGrid = block.getTileGrid();
        int blockRows = block.getRows();
        int blockCols = block.getCols();
        int currentPlacementId = nextPlacementId++;

        for (int r = 0; r < blockRows; r++) {
            for (int c = 0; c < blockCols; c++) {
                if (shape[r][c] == 1) {
                    int tileValue = (tileGrid[r][c] > 0) ? tileGrid[r][c] : block.getTileId();
                    grid[row + r][col + c] = tileValue;
                    placementGrid[row + r][col + c] = currentPlacementId;
                    sealGrid[row + r][col + c] = CellSealState.NORMAL;
                }
            }
        }
        return true;
    }

    private boolean isWildcard(int encodedVal) {
        return SpecialTileType.fromEncoded(encodedVal) == SpecialTileType.JADE_WILDCARD;
    }

    private boolean tilesMatch(int encodedVal, int targetBaseId) {
        if (encodedVal <= 0) {
            return false;
        }
        if (isWildcard(encodedVal)) {
            return true;
        }
        return SpecialTileType.extractBaseTileId(encodedVal) == targetBaseId;
    }

    public ClearResult clearLines() {
        boolean[][] toClear = new boolean[SIZE][SIZE];
        List<Integer> matchedRows = new ArrayList<>();
        List<Integer> matchedCols = new ArrayList<>();
        int totalMatches = 0;

        for (int targetBase = 1; targetBase <= 10; targetBase++) {
            for (int r = 0; r < SIZE; r++) {
                int c = 0;
                while (c < SIZE) {
                    if (!tilesMatch(grid[r][c], targetBase)) {
                        c++;
                        continue;
                    }
                    int end = c + 1;
                    while (end < SIZE && tilesMatch(grid[r][end], targetBase)) {
                        end++;
                    }
                    if (end - c >= MIN_LINE_LENGTH) {
                        Set<Integer> distinctBlocks = new HashSet<>();
                        for (int k = c; k < end; k++) {
                            distinctBlocks.add(placementGrid[r][k]);
                        }
                        if (distinctBlocks.size() >= 2) {
                            totalMatches++;
                            if (!matchedRows.contains(r)) {
                                matchedRows.add(r);
                            }
                            for (int k = c; k < end; k++) {
                                toClear[r][k] = true;
                            }
                        }
                    }
                    c = end;
                }
            }

            for (int c = 0; c < SIZE; c++) {
                int r = 0;
                while (r < SIZE) {
                    if (!tilesMatch(grid[r][c], targetBase)) {
                        r++;
                        continue;
                    }
                    int end = r + 1;
                    while (end < SIZE && tilesMatch(grid[end][c], targetBase)) {
                        end++;
                    }
                    if (end - r >= MIN_LINE_LENGTH) {
                        Set<Integer> distinctBlocks = new HashSet<>();
                        for (int k = r; k < end; k++) {
                            distinctBlocks.add(placementGrid[k][c]);
                        }
                        if (distinctBlocks.size() >= 2) {
                            totalMatches++;
                            if (!matchedCols.contains(c)) {
                                matchedCols.add(c);
                            }
                            for (int k = r; k < end; k++) {
                                toClear[k][c] = true;
                            }
                        }
                    }
                    r = end;
                }
            }

            boolean[][] visited = new boolean[SIZE][SIZE];
            int[] dr = {-1, 1, 0, 0};
            int[] dc = {0, 0, -1, 1};

            for (int r = 0; r < SIZE; r++) {
                for (int c = 0; c < SIZE; c++) {
                    if (visited[r][c] || !tilesMatch(grid[r][c], targetBase)) {
                        continue;
                    }

                    List<int[]> group = new ArrayList<>();
                    Set<Integer> distinctBlocks = new HashSet<>();
                    Queue<int[]> queue = new ArrayDeque<>();
                    visited[r][c] = true;
                    queue.add(new int[]{r, c});

                    while (!queue.isEmpty()) {
                        int[] curr = queue.poll();
                        group.add(curr);
                        distinctBlocks.add(placementGrid[curr[0]][curr[1]]);
                        for (int d = 0; d < 4; d++) {
                            int nr = curr[0] + dr[d];
                            int nc = curr[1] + dc[d];
                            if (nr >= 0 && nr < SIZE && nc >= 0 && nc < SIZE && !visited[nr][nc] && tilesMatch(grid[nr][nc], targetBase)) {
                                visited[nr][nc] = true;
                                queue.add(new int[]{nr, nc});
                            }
                        }
                    }

                    if (group.size() >= MIN_CLUSTER_SIZE && distinctBlocks.size() >= 2) {
                        totalMatches++;
                        for (int[] cell : group) {
                            toClear[cell[0]][cell[1]] = true;
                        }
                    }
                }
            }
        }

        if (totalMatches == 0) {
            return ClearResult.empty();
        }

        Set<Integer> matchedBaseIds = new HashSet<>();
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                if (toClear[r][c] && grid[r][c] > 0) {
                    matchedBaseIds.add(SpecialTileType.extractBaseTileId(grid[r][c]));
                }
            }
        }

        SynergyResult synergyResult = new SynergyResult();
        synergyChain.evaluate(matchedBaseIds, toClear, this, synergyResult);

        boolean specialTriggered = synergyResult.isTriggered();
        boolean[][] snapshotToClear = new boolean[SIZE][SIZE];
        for (int r = 0; r < SIZE; r++) {
            snapshotToClear[r] = toClear[r].clone();
        }

        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                if (snapshotToClear[r][c]) {
                    SpecialTileType type = SpecialTileType.fromEncoded(grid[r][c]);
                    SpecialTileStrategy strategy = strategies.get(type);
                    if (strategy != null) {
                        specialTriggered = true;
                        strategy.applyEffect(r, c, SIZE, toClear);
                    }
                }
            }
        }

        int[][] clearedGrid = new int[SIZE][SIZE];
        List<Integer> clearedTileValues = new ArrayList<>();
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                if (toClear[r][c] && grid[r][c] != 0) {
                    CellSealState currentSeal = sealGrid[r][c];
                    if (currentSeal.isDestroyedOnHit()) {
                        int encodedVal = grid[r][c];
                        clearedTileValues.add(encodedVal);
                        clearedGrid[r][c] = encodedVal;
                        grid[r][c] = 0;
                        placementGrid[r][c] = 0;
                        sealGrid[r][c] = CellSealState.NORMAL;
                    } else {
                        sealGrid[r][c] = currentSeal.onClearHit();
                    }
                }
            }
        }

        int[] clearedRowsArr = matchedRows.stream().mapToInt(Integer::intValue).toArray();
        int[] clearedColsArr = matchedCols.stream().mapToInt(Integer::intValue).toArray();
        int[] clearedValuesArr = clearedTileValues.stream().mapToInt(Integer::intValue).toArray();

        return new ClearResult(
                totalMatches,
                clearedRowsArr,
                clearedColsArr,
                clearedGrid,
                clearedValuesArr,
                specialTriggered,
                synergyResult.getRecipeName(),
                synergyResult.getBonusScore(),
                synergyResult.getBonusLives()
        );
    }

    public boolean sealCell(int row, int col) {
        if (row < 0 || row >= SIZE || col < 0 || col >= SIZE || grid[row][col] == 0) {
            return false;
        }
        sealGrid[row][col] = CellSealState.SEALED;
        return true;
    }

    public boolean sealRandomOccupiedTile() {
        List<int[]> candidates = new ArrayList<>();
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                if (grid[r][c] > 0 && sealGrid[r][c] == CellSealState.NORMAL) {
                    candidates.add(new int[]{r, c});
                }
            }
        }
        if (candidates.isEmpty()) {
            return false;
        }
        int[] chosen = candidates.get(random.nextInt(candidates.size()));
        sealGrid[chosen[0]][chosen[1]] = CellSealState.SEALED;
        return true;
    }

    public CellSealState[][] getSealSnapshot() {
        CellSealState[][] snapshot = new CellSealState[SIZE][SIZE];
        for (int r = 0; r < SIZE; r++) {
            snapshot[r] = sealGrid[r].clone();
        }
        return snapshot;
    }

    public void restoreSealSnapshot(CellSealState[][] snapshot) {
        if (snapshot == null || snapshot.length != SIZE) {
            return;
        }
        for (int r = 0; r < SIZE; r++) {
            if (snapshot[r] != null && snapshot[r].length == SIZE) {
                sealGrid[r] = snapshot[r].clone();
            }
        }
    }

    public void restoreSnapshot(int[][] snapshot) {
        if (snapshot == null || snapshot.length != SIZE) {
            throw new IllegalArgumentException("Snapshot papan harus berukuran 8x8.");
        }
        for (int r = 0; r < SIZE; r++) {
            if (snapshot[r] == null || snapshot[r].length != SIZE) {
                throw new IllegalArgumentException("Snapshot papan harus berukuran 8x8.");
            }
        }
        for (int r = 0; r < SIZE; r++) {
            grid[r] = snapshot[r].clone();
            for (int c = 0; c < SIZE; c++) {
                placementGrid[r][c] = (snapshot[r][c] > 0) ? nextPlacementId : 0;
                if (snapshot[r][c] == 0) {
                    sealGrid[r][c] = CellSealState.NORMAL;
                }
            }
        }
        nextPlacementId++;
    }

    public boolean hasValidMove(Block block) {
        if (block == null) {
            return false;
        }
        int maxStartRow = SIZE - block.getRows();
        int maxStartCol = SIZE - block.getCols();
        if (maxStartRow < 0 || maxStartCol < 0) {
            return false;
        }
        for (int r = 0; r <= maxStartRow; r++) {
            for (int c = 0; c <= maxStartCol; c++) {
                if (canPlace(block, r, c)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void reset() {
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                grid[r][c] = 0;
                placementGrid[r][c] = 0;
                sealGrid[r][c] = CellSealState.NORMAL;
            }
        }
        nextPlacementId = 1;
    }
}