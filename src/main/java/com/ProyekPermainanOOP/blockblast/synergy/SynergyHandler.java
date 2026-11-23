package com.ProyekPermainanOOP.blockblast.synergy;

import com.ProyekPermainanOOP.blockblast.model.Board;
import java.util.Set;

/**
 * GoF Chain of Responsibility Pattern untuk mengevaluasi kombinasi aksara Hanzi
 * yang hancur dalam satu langkah penghapusan.
 */
public abstract class SynergyHandler {
    protected SynergyHandler nextHandler;

    public SynergyHandler setNext(SynergyHandler nextHandler) {
        this.nextHandler = nextHandler;
        return nextHandler;
    }

    public void evaluate(Set<Integer> clearedBaseIds, boolean[][] toClear, Board board, SynergyResult result) {
        applyRule(clearedBaseIds, toClear, board, result);
        if (nextHandler != null) {
            nextHandler.evaluate(clearedBaseIds, toClear, board, result);
        }
    }

    protected abstract void applyRule(Set<Integer> clearedBaseIds, boolean[][] toClear, Board board, SynergyResult result);
}