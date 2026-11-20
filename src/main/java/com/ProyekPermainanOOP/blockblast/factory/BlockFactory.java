package src.main.java.com.ProyekPermainanOOP.blockblast.factory;

import src.main.java.com.ProyekPermainanOOP.blockblast.model.Block;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BlockFactory {
    private static final Random random = new Random();
    private static final int TOTAL_MAHJONG_TILES = 10;
    private static final int POOL_CAPACITY = 30;
    private static final List<Block> blockPool = new ArrayList<>();
    private static final List<BlockGenerationListener> listeners = new ArrayList<>();

    public interface BlockGenerationListener {
        void onBlockGenerated(Block block, int patternIndex, int tileIndex);
    }

    private static final int[][][] PATTERNS = {
            {{1}},
            {{1, 1}},
            {{1}, {1}},
            {{1, 1, 1}},
            {{1}, {1}, {1}},
            {{1, 1, 1, 1}},
            {{1}, {1}, {1}, {1}},
            {{1, 1}, {1, 1}},
            {{1, 0}, {1, 1}},
            {{0, 1}, {1, 1}},
            {{1, 1}, {1, 0}},
            {{1, 1}, {0, 1}},
            {{1, 1, 1}, {0, 1, 0}},
            {{1, 1, 1}, {1, 0, 0}},
            {{1, 1, 1}, {0, 0, 1}},
            {{1, 1, 0}, {0, 1, 1}},
            {{0, 1, 1}, {1, 1, 0}},
            {{0, 1, 0}, {1, 1, 1}, {0, 1, 0}}
    };

    private static final int[] PATTERN_WEIGHTS = {
            16,
            14,
            14,
            10,
            10,
            6,
            6,
            9,
            8,
            8,
            8,
            8,
            6,
            6,
            6,
            5,
            5,
            3
    };

    private static final int TOTAL_WEIGHT;

    static {
        int sum = 0;
        for (int weight : PATTERN_WEIGHTS) {
            sum += weight;
        }
        TOTAL_WEIGHT = sum;
    }

    public static int getPatternCount() {
        return PATTERNS.length;
    }

    public static int getTotalMahjongTiles() {
        return TOTAL_MAHJONG_TILES;
    }

    public static int getPoolSize() {
        return blockPool.size();
    }

    public static void addListener(BlockGenerationListener listener) {
        if (listener != null && !listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public static void removeListener(BlockGenerationListener listener) {
        listeners.remove(listener);
    }

    public static void clearListeners() {
        listeners.clear();
    }

    public static void clearPool() {
        blockPool.clear();
    }

    public static void releaseBlock(Block block) {
        if (block == null) {
            return;
        }
        block.setActive(false);
        block.setSelected(false);
        block.setHighlighted(false);
        if (blockPool.size() < POOL_CAPACITY && !blockPool.contains(block)) {
            blockPool.add(block);
        }
    }

    public static void releaseBlocks(List<Block> blocks) {
        if (blocks == null) {
            return;
        }
        for (Block block : blocks) {
            releaseBlock(block);
        }
    }

    private static Block prepareBlockState(Block block) {
        block.setActive(true);
        block.setSelected(false);
        block.setHighlighted(false);
        return block;
    }

    private static Block acquireBlock(int[][] pattern, int tileId) {
        int[][] safePattern = deepCopy(pattern);
        if (!blockPool.isEmpty()) {
            Block pooled = blockPool.remove(blockPool.size() - 1);
            if (pooled != null
                    && pooled.getRows() == safePattern.length
                    && pooled.getCols() == safePattern[0].length
                    && pooled.getTileId() == tileId
                    && java.util.Arrays.deepEquals(pooled.getShape(), safePattern)) {
                return prepareBlockState(pooled);
            }
        }
        return prepareBlockState(new Block(safePattern, tileId));
    }

    private static Block acquireBlockWithTileGrid(int[][] pattern, int[][] tileGrid) {
        int[][] safePattern = deepCopy(pattern);
        int[][] safeTileGrid = deepCopy(tileGrid);
        if (!blockPool.isEmpty()) {
            Block pooled = blockPool.remove(blockPool.size() - 1);
            if (pooled != null
                    && pooled.getRows() == safePattern.length
                    && pooled.getCols() == safePattern[0].length
                    && java.util.Arrays.deepEquals(pooled.getShape(), safePattern)
                    && java.util.Arrays.deepEquals(pooled.getTileGrid(), safeTileGrid)) {
                return prepareBlockState(pooled);
            }
        }
        return prepareBlockState(new Block(safePattern, safeTileGrid));
    }

    private static int selectWeightedPatternIndex() {
        int roll = random.nextInt(TOTAL_WEIGHT);
        int cumulative = 0;
        for (int i = 0; i < PATTERN_WEIGHTS.length; i++) {
            cumulative += PATTERN_WEIGHTS[i];
            if (roll < cumulative) {
                return i;
            }
        }
        return PATTERNS.length - 1;
    }

    public static int[][] buildTileMapping(int[][] pattern, int tileId) {
        int safeTileId = Math.max(1, Math.min(tileId, TOTAL_MAHJONG_TILES));
        int[][] tileMap = new int[pattern.length][pattern[0].length];
        for (int r = 0; r < pattern.length; r++) {
            for (int c = 0; c < pattern[r].length; c++) {
                tileMap[r][c] = (pattern[r][c] != 0) ? safeTileId : 0;
            }
        }
        return tileMap;
    }

    public static int[][] buildMixedTileMapping(int[][] pattern) {
        int[][] tileMap = new int[pattern.length][pattern[0].length];
        for (int r = 0; r < pattern.length; r++) {
            for (int c = 0; c < pattern[r].length; c++) {
                tileMap[r][c] = (pattern[r][c] != 0) ? (random.nextInt(TOTAL_MAHJONG_TILES) + 1) : 0;
            }
        }
        return tileMap;
    }

    public static Block createRandomBlock() {
        int patternIndex = selectWeightedPatternIndex();
        int tileId = random.nextInt(TOTAL_MAHJONG_TILES) + 1;
        int[][] pattern = PATTERNS[patternIndex];

        Block block = acquireBlock(pattern, tileId);
        notifyListeners(block, patternIndex, tileId);
        return block;
    }

    public static Block createMixedTileRandomBlock() {
        int patternIndex = selectWeightedPatternIndex();
        int[][] pattern = PATTERNS[patternIndex];
        int[][] mixedGrid = buildMixedTileMapping(pattern);

        Block block = acquireBlockWithTileGrid(pattern, mixedGrid);
        notifyListeners(block, patternIndex, block.getTileId());
        return block;
    }

    public static Block createBlockWithTile(int patternIndex, int tileId) {
        int safePatternIndex = Math.max(0, Math.min(patternIndex, PATTERNS.length - 1));
        int safeTileId = Math.max(1, Math.min(tileId, TOTAL_MAHJONG_TILES));
        int[][] pattern = PATTERNS[safePatternIndex];

        Block block = acquireBlock(pattern, safeTileId);
        notifyListeners(block, safePatternIndex, safeTileId);
        return block;
    }

    public static Block createTransformedBlock(int patternIndex, int tileId, int cwRotations, boolean mirrorHorizontal) {
        Block base = createBlockWithTile(patternIndex, tileId);
        Block current = base;
        int steps = ((cwRotations % 4) + 4) % 4;
        for (int i = 0; i < steps; i++) {
            current = current.rotatedCW();
        }
        if (mirrorHorizontal) {
            current = current.mirroredHorizontal();
        }
        return prepareBlockState(current);
    }

    private static int[][] deepCopy(int[][] source) {
        int[][] copy = new int[source.length][];
        for (int i = 0; i < source.length; i++) {
            copy[i] = source[i].clone();
        }
        return copy;
    }

    private static void notifyListeners(Block block, int patternIndex, int tileId) {
        for (BlockGenerationListener listener : listeners) {
            listener.onBlockGenerated(block, patternIndex, tileId);
        }
    }
}