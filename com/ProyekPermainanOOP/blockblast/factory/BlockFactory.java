package com.ProyekPermainanOOP.blockblast.factory;

import com.ProyekPermainanOOP.blockblast.model.Block;
import java.util.Random;

public class BlockFactory {
    private static final Random random = new Random();

    private static final int[][][] PATTERNS = {
            {{1}},
            {{1, 1}},
            {{1}, {1}},
            {{1, 1, 1}},
            {{1}, {1}, {1}},
            {{1, 1}, {1, 1}},
            {{1, 0}, {1, 1}},
            {{1, 1, 1}, {0, 1, 0}},
            {{1, 1, 1}, {1, 0, 0}}
    };

    public static Block createRandomBlock() {
        int index = random.nextInt(PATTERNS.length);
        int[][] pattern = PATTERNS[index];
        return new Block(pattern);
    }
}