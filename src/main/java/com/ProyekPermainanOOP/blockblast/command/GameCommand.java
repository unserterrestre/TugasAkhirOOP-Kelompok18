package src.main.java.com.ProyekPermainanOOP.blockblast.command;

/**
 * Kontrak GoF Command Pattern untuk membungkus aksi permainan (seperti penempatan balok)
 * beserta dukungan pembatalan langkah (undo).
 */
public interface GameCommand {
    boolean execute();
    void undo();
}
