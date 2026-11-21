package com.ProyekPermainanOOP.blockblast.localization;

import com.ProyekPermainanOOP.blockblast.model.GameMode;

public class LocalizationManager {

    public enum Language {
        INDONESIA("BAHASA INDONESIA", "SKOR", "NYAWA", "WAKTU", "REKOR", "ULANG", "SUARA", "NYALA", "MATI", "BAHASA", "BATAL", "KOLEKSI", "KLASIK", "MISI", "KILAT", "TARGET MISI", "UBIN", "+5d TIAP KOMBO!", "PILIH BAHASA", "(AKTIF)", "Klik bahasa atau tekan Panah + Enter", "PERMAINAN SELESAI", "Klik layar atau tekan R untuk ulang", "NYAWA -1! BALOK BARU", "KOLEKSI PUSAKA", "Hancurkan 4 ubin tiap aksara ([K] Tutup)"),
        ENGLISH("ENGLISH", "SCORE", "LIVES", "TIME", "BEST", "RESET", "AUDIO", "ON", "OFF", "LANG", "UNDO", "COLLECTION", "CLASSIC", "QUEST", "BLITZ", "QUEST TARGET", "TILES", "+5s PER COMBO!", "SELECT LANGUAGE", "(ACTIVE)", "Click language or press Arrows + Enter", "GAME OVER", "Click screen or press R to restart", "LIFE -1! NEW BLOCKS", "PUSAKA COMPENDIUM", "Clear 4 tiles of each relic ([K] Close)"),
        MELAYU("BAHASA MELAYU", "MARKAH", "NYAWA", "MASA", "TERBAIK", "ULANG", "AUDIO", "HIDUP", "MATI", "BAHASA", "BATAL", "KOLEKSI", "KLASIK", "MISI", "PANTAS", "SASARAN MISI", "JUBIN", "+5s SETIAP KOMBO!", "PILIH BAHASA", "(AKTIF)", "Klik bahasa atau tekan Anak Panah + Enter", "TAMAT PERMAINAN", "Klik skrin atau tekan R untuk ulang", "NYAWA -1! BLOK BAHARU", "KOLEKSI PUSAKA", "Padam 4 jubin setiap aksara ([K] Tutup)"),
        JAWA("BASA JAWA", "BIJI", "NYAWA", "WEKTU", "REKOR", "BALENI", "SWARA", "URIP", "MATI", "BASA", "WURUNG", "KOLEKSI", "KLASIK", "MISI", "CEPET", "TARGET MISI", "KOTHAK", "+5d SABEN KOMBO!", "PILIH BASA", "(AKTIF)", "Klik basa utawa pencet Panah + Enter", "DOLANAN RAMPUNG", "Klik layar utawa pencet R kanggo baleni", "NYAWA -1! BALOK ANYAR", "KOLEKSI PUSAKA", "Busek 4 kothak saben aksara ([K] Tutup)"),
        SUNDA("BASA SUNDA", "PEUNTEUN", "NYAWA", "WAKTOS", "REKOR", "BALIKAN", "SORA", "HURUNG", "PAEH", "BASA", "BATAL", "KOLEKSI", "KLASIK", "MISI", "KILAT", "TARGET MISI", "UBIN", "+5d UNGGAL KOMBO!", "PILIH BASA", "(AKTIF)", "Klik basa atawa pencet Panah + Enter", "KAULINAN RENGSE", "Klik layar atawa pencet R pikeun balikan", "NYAWA -1! BALOK ANYAR", "KOLEKSI PUSAKA", "Hapus 4 ubin unggal aksara ([K] Tutup)"),
        ESPANOL("ESPANOL", "PUNTOS", "VIDAS", "TIEMPO", "MEJOR", "REINICIAR", "AUDIO", "SI", "NO", "IDIOMA", "DESHACER", "COLECCION", "CLASICO", "MISION", "RELAMPAGO", "OBJETIVO", "FICHAS", "+5s POR COMBO!", "ELEGIR IDIOMA", "(ACTIVO)", "Haz clic o usa Flechas + Enter", "FIN DEL JUEGO", "Haz clic o pulsa R para reiniciar", "VIDA -1! BLOQUES NUEVOS", "COMPENDIO PUSAKA", "Elimina 4 fichas de cada reliquia ([K] Cerrar)"),
        FRANCAIS("FRANCAIS", "SCORE", "VIES", "TEMPS", "RECORD", "REJOUER", "AUDIO", "OUI", "NON", "LANGUE", "ANNULER", "COLLECTION", "CLASSIQUE", "QUETE", "BLITZ", "OBJECTIF", "TUILES", "+5s PAR COMBO!", "CHOISIR LA LANGUE", "(ACTIF)", "Cliquez ou utilisez Fleches + Entree", "PARTIE TERMINEE", "Cliquez ou appuyez sur R pour rejouer", "VIE -1! NOUVEAUX BLOCS", "COLLECTION PUSAKA", "Eliminez 4 tuiles par relique ([K] Fermer)"),
        DEUTSCH("DEUTSCH", "PUNKTE", "LEBEN", "ZEIT", "REKORD", "NEUSTART", "TON", "AN", "AUS", "SPRACHE", "ZURUECK", "SAMMLUNG", "KLASSISCH", "AUFGABE", "BLITZ", "ZIEL", "STEINE", "+5s PRO KOMBO!", "SPRACHE WAEHLEN", "(AKTIV)", "Klicken oder Pfeile + Enter druecken", "SPIEL VORBEI", "Klicken oder R druecken zum Neustart", "LEBEN -1! NEUE BLOECKE", "PUSAKA SAMMLUNG", "4 Steine je Relikt loeschen ([K] Zu)"),
        ITALIANO("ITALIANO", "PUNTI", "VITE", "TEMPO", "RECORD", "RIAVVIA", "AUDIO", "SI", "NO", "LINGUA", "ANNULLA", "COLLEZIONE", "CLASSICO", "MISSIONE", "BLITZ", "OBIETTIVO", "TESSERE", "+5s PER COMBO!", "SCEGLI LINGUA", "(ATTIVO)", "Clicca o usa Frecce + Invio", "GIOCO FINITO", "Clicca o premi R per riavviare", "VITA -1! NUOVI BLOCCHI", "COLLEZIONE PUSAKA", "Elimina 4 tessere per reliquia ([K] Chiudi)"),
        PORTUGUES("PORTUGUES", "PONTOS", "VIDAS", "TEMPO", "RECORDE", "REINICIAR", "AUDIO", "SIM", "NAO", "IDIOMA", "DESFAZER", "COLECAO", "CLASSICO", "MISSAO", "BLITZ", "ALVO", "PECAS", "+5s POR COMBO!", "ESCOLHER IDIOMA", "(ATIVO)", "Clique ou use Setas + Enter", "FIM DE JOGO", "Clique ou prima R para reiniciar", "VIDA -1! NOVOS BLOCOS", "COLECAO PUSAKA", "Limpe 4 pecas de cada reliquia ([K] Fechar)"),
        NEDERLANDS("NEDERLANDS", "SCORE", "LEVENS", "TIJD", "RECORD", "OPNIEUW", "GELUID", "AAN", "UIT", "TAAL", "ONGEDAAN", "COLLECTIE", "KLASSIEK", "MISSIE", "BLITZ", "DOEL", "STENEN", "+5s PER COMBO!", "KIES TAAL", "(ACTIEF)", "Klik of gebruik Pijlen + Enter", "SPEL VOORBIJ", "Klik of druk op R om opnieuw te starten", "LEVEN -1! NIEUWE BLOKKEN", "PUSAKA COLLECTIE", "Verwijder 4 stenen per reliek ([K] Sluiten)"),
        POLSKI("POLSKI", "WYNIK", "ZYCIA", "CZAS", "REKORD", "RESET", "DZWIEK", "WL", "WYL", "JEZYK", "COFNIJ", "KOLEKCJA", "KLASYK", "MISJA", "BLITZ", "CEL", "KAFELKI", "+5s ZA KOMBO!", "WYBIERZ JEZYK", "(AKTYWNY)", "Kliknij lub uzyj Strzalek + Enter", "KONIEC GRY", "Kliknij lub nacisnij R aby zagrac ponownie", "ZYCIE -1! NOWE KLOCKI", "KOLEKCJA PUSAKA", "Usun 4 kafelki kazdego znaku ([K] Zamknij)"),
        TURKCE("TURKCE", "PUAN", "CAN", "SURE", "REKOR", "SIFIRLA", "SES", "ACIK", "KAPALI", "DIL", "GERI AL", "KOLEKSIYON", "KLASIK", "GOREV", "HIZLI", "HEDEF", "TAS", "KOMBO +5sn!", "DIL SECIN", "(AKTIF)", "Tiklayin veya Yon Tuslari + Enter", "OYUN BITTI", "Yeniden baslamak icin tiklayin veya R", "CAN -1! YENI BLOKLAR", "PUSAKA KOLEKSIYONU", "Her sembolden 4 tas sil ([K] Kapat)"),
        SVENSKA("SVENSKA", "POANG", "LIV", "TID", "REKORD", "OMSTART", "LJUD", "PA", "AV", "SPRAK", "ANGRA", "SAMLING", "KLASSISK", "UPPDRAG", "BLITZ", "MAL", "BRICKOR", "+5s PER KOMBO!", "VALJ SPRAK", "(AKTIV)", "Klicka eller anvand Pilar + Enter", "SPELET SLUT", "Klicka eller tryck R for att borja om", "LIV -1! NYA BLOCK", "PUSAKA SAMLING", "Rensa 4 brickor av varje tecken ([K] Stang)"),
        NORSK("NORSK", "POENG", "LIV", "TID", "REKORD", "OMSTART", "LYD", "PA", "AV", "SPRAK", "ANGRE", "SAMLING", "KLASSISK", "OPPDRAG", "BLITZ", "MAL", "BRIKKER", "+5s PER KOMBO!", "VELG SPRAK", "(AKTIV)", "Klikk eller bruk Piler + Enter", "SPILLET ER SLUTT", "Klikk eller trykk R for a starte pa nytt", "LIV -1! NYE BLOKKER", "PUSAKA SAMLING", "Fjern 4 brikker av hvert tegn ([K] Lukk)"),
        DANSK("DANSK", "POINT", "LIV", "TID", "REKORD", "GENSTART", "LYD", "TIL", "FRA", "SPROG", "FORTRYD", "SAMLING", "KLASSISK", "MISSION", "BLITZ", "MAL", "BRIKKER", "+5s PR KOMBO!", "VAELG SPROG", "(AKTIV)", "Klik eller brug Pile + Enter", "SPILLET ER SLUT", "Klik eller tryk R for at genstarte", "LIV -1! NYE BLOKKE", "PUSAKA SAMLING", "Fjern 4 brikker af hvert tegn ([K] Luk)"),
        SUOMI("SUOMI", "PISTEET", "ELAMAT", "AIKA", "ENNATYS", "ALUSTA", "AANI", "PAALLA", "POIS", "KIELI", "KUMOA", "KOKOELMA", "KLASSINEN", "TEHTAVA", "PIKA", "TAVOITE", "LAATTAA", "+5s KOMBOSTA!", "VALITSE KIELI", "(KAYTOSSA)", "Klikkaa tai kayta Nuolia + Enter", "PELI OHI", "Klikkaa tai paina R aloittaaksesi alusta", "ELAMA -1! UUDET PALIKAT", "PUSAKA KOKOELMA", "Poista 4 laattaa kustakin merkista ([K] Sulje)"),
        TAGALOG("TAGALOG", "PUNTOS", "BUHAY", "ORAS", "REKORD", "ULITIN", "TUNOG", "BUKAS", "PATAY", "WIKA", "IBALIK", "KOLEKSYON", "KLASIKO", "MISYON", "MABILIS", "TARGET", "TILES", "+5s BAWAT COMBO!", "PUMILI NG WIKA", "(AKTIBO)", "I-click o gamitin ang Arrows + Enter", "TAPOS NA ANG LARO", "I-click o pindutin ang R upang umulit", "BUHAY -1! BAGONG BLOKE", "KOLEKSYON NG PUSAKA", "Mag-alis ng 4 na tile bawat simbolo ([K] Isara)"),
        VIETNAMESE("TIENG VIET", "DIEM", "MANG", "THOI GIAN", "KY LUC", "CHOI LAI", "AM THANH", "BAT", "TAT", "NGON NGU", "HOAN TAC", "BO SUU TAP", "CO DIEN", "NHIEM VU", "TOC DO", "MUC TIEU", "O", "+5s MOI COMBO!", "CHON NGON NGU", "(DANG DUNG)", "Nhan chuot hoac phim Mui ten + Enter", "TRO CHOI KET THUC", "Nhan chuot hoac phim R de choi lai", "MANG -1! KHOI MOI", "BO SUU TAP PUSAKA", "Xoa 4 o moi ky tu de mo khoa ([K] Dong)"),
        SWAHILI("SWAHILI", "ALAMA", "UHAI", "MUDA", "REKODI", "ANZA UPYA", "SAUTI", "WASHA", "ZIMA", "LUGHA", "TENGUA", "MKUSANYIKO", "KAWAIDA", "JUKUMU", "KASI", "LENGO", "VIGAE", "+5s KWA KOMBO!", "CHAGUA LUGHA", "(INATUMIKA)", "Bofya au tumia Mishale + Enter", "MCHEZO UMEISHA", "Bofya au bonyeza R kuanza upya", "UHAI -1! VIZUIZI VIPYA", "MKUSANYIKO WA PUSAKA", "Futa vigae 4 kwa kila alama ([K] Funga)");

        private final String displayName;
        private final String scoreLabel;
        private final String livesLabel;
        private final String timeLabel;
        private final String bestLabel;
        private final String resetLabel;
        private final String audioLabel;
        private final String onLabel;
        private final String offLabel;
        private final String langLabel;
        private final String undoLabel;
        private final String collectionLabel;
        private final String classicLabel;
        private final String questLabel;
        private final String blitzLabel;
        private final String questTargetLabel;
        private final String tilesLabel;
        private final String blitzHintLabel;
        private final String menuTitle;
        private final String activeTag;
        private final String menuHint;
        private final String gameOverTitle;
        private final String gameOverSubtitle;
        private final String newTrayAlert;
        private final String compendiumTitle;
        private final String compendiumHint;

        Language(String displayName, String scoreLabel, String livesLabel, String timeLabel,
                 String bestLabel, String resetLabel, String audioLabel, String onLabel,
                 String offLabel, String langLabel, String undoLabel, String collectionLabel,
                 String classicLabel, String questLabel, String blitzLabel, String questTargetLabel,
                 String tilesLabel, String blitzHintLabel, String menuTitle, String activeTag,
                 String menuHint, String gameOverTitle, String gameOverSubtitle, String newTrayAlert,
                 String compendiumTitle, String compendiumHint) {
            this.displayName = displayName;
            this.scoreLabel = scoreLabel;
            this.livesLabel = livesLabel;
            this.timeLabel = timeLabel;
            this.bestLabel = bestLabel;
            this.resetLabel = resetLabel;
            this.audioLabel = audioLabel;
            this.onLabel = onLabel;
            this.offLabel = offLabel;
            this.langLabel = langLabel;
            this.undoLabel = undoLabel;
            this.collectionLabel = collectionLabel;
            this.classicLabel = classicLabel;
            this.questLabel = questLabel;
            this.blitzLabel = blitzLabel;
            this.questTargetLabel = questTargetLabel;
            this.tilesLabel = tilesLabel;
            this.blitzHintLabel = blitzHintLabel;
            this.menuTitle = menuTitle;
            this.activeTag = activeTag;
            this.menuHint = menuHint;
            this.gameOverTitle = gameOverTitle;
            this.gameOverSubtitle = gameOverSubtitle;
            this.newTrayAlert = newTrayAlert;
            this.compendiumTitle = compendiumTitle;
            this.compendiumHint = compendiumHint;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    private static LocalizationManager instance;
    private Language currentLanguage;
    private boolean languageMenuOpen;

    private LocalizationManager() {
        this.currentLanguage = Language.INDONESIA;
        this.languageMenuOpen = false;
    }

    public static LocalizationManager getInstance() {
        if (instance == null) {
            instance = new LocalizationManager();
        }
        return instance;
    }

    public Language getCurrentLanguage() {
        return currentLanguage;
    }

    public void setLanguage(Language language) {
        if (language != null) {
            this.currentLanguage = language;
        }
    }

    public void setLanguageByIndex(int index) {
        Language[] values = Language.values();
        if (index >= 0 && index < values.length) {
            this.currentLanguage = values[index];
        }
    }

    public void moveSelection(int delta) {
        Language[] values = Language.values();
        int next = Math.floorMod(currentLanguage.ordinal() + delta, values.length);
        currentLanguage = values[next];
    }

    public boolean isLanguageMenuOpen() {
        return languageMenuOpen;
    }

    public void setLanguageMenuOpen(boolean open) {
        this.languageMenuOpen = open;
    }

    public void toggleLanguageMenu() {
        this.languageMenuOpen = !this.languageMenuOpen;
    }

    public String getScoreLabel() {
        return currentLanguage.scoreLabel;
    }

    public String getLivesLabel() {
        return currentLanguage.livesLabel;
    }

    public String getTimeLabel() {
        return currentLanguage.timeLabel;
    }

    public String getModeName(GameMode mode) {
        if (mode == GameMode.QUEST) {
            return currentLanguage.questLabel;
        }
        if (mode == GameMode.BLITZ) {
            return currentLanguage.blitzLabel;
        }
        return currentLanguage.classicLabel;
    }

    public String formatCenterTopBar(int highScore, int combo, GameMode mode, int undoRemaining) {
        return currentLanguage.bestLabel + ": " + highScore
                + "  x" + combo
                + "  |  [G] " + getModeName(mode)
                + "  |  [Z] " + currentLanguage.undoLabel + ": " + undoRemaining
                + "  |  [L] " + currentLanguage.langLabel;
    }

    public String formatBottomLeftControls(boolean muted, GameMode mode, int questCurrent, int questTarget) {
        if (mode == GameMode.QUEST) {
            return currentLanguage.questTargetLabel + ": " + questCurrent + "/" + questTarget + " " + currentLanguage.tilesLabel;
        }
        String audioState = muted ? currentLanguage.offLabel : currentLanguage.onLabel;
        String base = "[R] " + currentLanguage.resetLabel + "  |  [M] " + currentLanguage.audioLabel + ": " + audioState;
        if (mode == GameMode.BLITZ) {
            return base + " (" + currentLanguage.blitzHintLabel + ")";
        }
        return base;
    }

    public String formatBottomRightCollection(int unlocked, int total) {
        return "[K] " + currentLanguage.collectionLabel + " (" + unlocked + "/" + total + ")";
    }

    public String getLanguageMenuTitle() {
        return currentLanguage.menuTitle;
    }

    public String getActiveTag() {
        return currentLanguage.activeTag;
    }

    public String getLanguageMenuHint() {
        return currentLanguage.menuHint;
    }

    public String getGameOverTitle() {
        return currentLanguage.gameOverTitle;
    }

    public String getGameOverSubtitle() {
        return currentLanguage.gameOverSubtitle;
    }

    public String getNewTrayAlert() {
        return currentLanguage.newTrayAlert;
    }

    public String getCompendiumTitle() {
        return currentLanguage.compendiumTitle;
    }

    public String getCompendiumHint() {
        return currentLanguage.compendiumHint;
    }
}