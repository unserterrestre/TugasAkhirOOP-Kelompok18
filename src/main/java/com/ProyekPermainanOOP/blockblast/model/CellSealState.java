package com.ProyekPermainanOOP.blockblast.model;

/**
 * State Pattern pada setiap sel papan untuk transisi ubin terkunci:
 * NORMAL -> CLEARED (1x clear)
 * SEALED -> CRACKED -> CLEARED (butuh 2x clear)
 */
public enum CellSealState {
    NORMAL {
        @Override
        public CellSealState onClearHit() {
            return NORMAL; // Langsung hancur
        }

        @Override
        public boolean isDestroyedOnHit() {
            return true;
        }
    },
    SEALED {
        @Override
        public CellSealState onClearHit() {
            return CRACKED; // Segel batu giok retak, belum hancur
        }

        @Override
        public boolean isDestroyedOnHit() {
            return false;
        }
    },
    CRACKED {
        @Override
        public CellSealState onClearHit() {
            return NORMAL; // Segel retak akhirnya pecah dan petak dibersihkan
        }

        @Override
        public boolean isDestroyedOnHit() {
            return true;
        }
    };

    public abstract CellSealState onClearHit();
    public abstract boolean isDestroyedOnHit();
}