
package com.ProyekPermainanOOP.blockblast.graphics;

import com.ProyekPermainanOOP.blockblast.model.Block;
import com.badlogic.gdx.math.MathUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class AnimationManager {

    private static final float DURATION = 0.24f;
    private static final float INITIAL_SCALE = 0.28f;

    private final Map<Integer, Float> cellProgress = new HashMap<>();

    public void playPlacement(Block block, int startRow, int startCol) {
        if (block == null) {
            return;
        }

        int[][] shape = block.getShape();

        for (int row = 0; row < shape.length; row++) {
            for (int col = 0; col < shape[row].length; col++) {

                if (shape[row][col] != 0) {
                    int boardRow = startRow + row;
                    int boardCol = startCol + col;

                    if (boardRow >= 0
                            && boardRow < BoardRenderer.GRID_SIZE
                            && boardCol >= 0
                            && boardCol < BoardRenderer.GRID_SIZE) {

                        int key = boardRow * BoardRenderer.GRID_SIZE
                                + boardCol;

                        cellProgress.put(key, 0f);
                    }
                }
            }
        }
    }

    public void update(float delta) {
        Iterator<Map.Entry<Integer, Float>> iterator =
                cellProgress.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<Integer, Float> entry = iterator.next();

            float progress = entry.getValue() + delta / DURATION;

            if (progress >= 1f) {
                iterator.remove();
            } else {
                entry.setValue(progress);
            }
        }
    }

    public float getScaleAt(int row, int col) {
        int key = row * BoardRenderer.GRID_SIZE + col;

        Float progressValue = cellProgress.get(key);

        if (progressValue == null) {
            return 1f;
        }

        float progress = MathUtils.clamp(
                progressValue,
                0f,
                1f
        );

        float scale = INITIAL_SCALE
                + (1f - INITIAL_SCALE) * progress;

        float bounce = 0.16f
                * MathUtils.sin(progress * MathUtils.PI)
                * (1f - progress);

        return scale + bounce;
    }

    public boolean isAnimating() {
        return !cellProgress.isEmpty();
    }

    public void clear() {
        cellProgress.clear();
    }
}
