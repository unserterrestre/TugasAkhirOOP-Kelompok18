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

    public static void addListener(BlockGenerationListener listener) {
        if (listener != null && !listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public static void removeListener(BlockGenerationListener listener) {
        listeners.remove(listener);
    }

    public static void releaseBlock(Block block) {
        if (block == null) {
            return;
        }
        block.setActive(false);
        if (blockPool.size() < POOL_CAPACITY && !blockPool.contains(block)) {
            blockPool.add(block);
        }
    }

    private static Block acquireBlock(int[][] pattern, int tileId) {
        Block block = new Block(pattern, tileId);
        block.setActive(true);
        return block;
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
        int[][] tileMap = new int[pattern.length][pattern[0].length];
        for (int r = 0; r < pattern.length; r++) {
            for (int c = 0; c < pattern[r].length; c++) {
                tileMap[r][c] = (pattern[r][c] != 0) ? tileId : 0;
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

    public static Block createBlockWithTile(int patternIndex, int tileId) {
        int safePatternIndex = Math.max(0, Math.min(patternIndex, PATTERNS.length - 1));
        int safeTileId = Math.max(1, Math.min(tileId, TOTAL_MAHJONG_TILES));
        int[][] pattern = PATTERNS[safePatternIndex];

        Block block = acquireBlock(pattern, safeTileId);
        notifyListeners(block, safePatternIndex, safeTileId);
        return block;
    }

    private static void notifyListeners(Block block, int patternIndex, int tileId) {
        for (BlockGenerationListener listener : listeners) {
            listener.onBlockGenerated(block, patternIndex, tileId);
        }
    }
}