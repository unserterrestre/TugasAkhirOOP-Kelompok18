package src.main.java.com.ProyekPermainanOOP.blockblast.scoring;

public class ScoreManager {
    private static ScoreManager instance;
    private int currentScore;
    private int highScore;
    private int comboMultiplier;

    private ScoreManager() {
        this.currentScore = 0;
        this.highScore = 0;
        this.comboMultiplier = 1;
    }

    public static synchronized ScoreManager getInstance() {
        if (instance == null) {
            instance = new ScoreManager();
        }
        return instance;
    }

    public void addScore(int basePoints, boolean lineCleared) {
        if (lineCleared) {
            comboMultiplier++;
        } else {
            comboMultiplier = 1;
        }

        int earnedPoints = basePoints * comboMultiplier;
        currentScore += earnedPoints;

        if (currentScore > highScore) {
            highScore = currentScore;
        }
    }

    public void resetScore() {
        currentScore = 0;
        comboMultiplier = 1;
    }

    public int getCurrentScore() {
        return currentScore;
    }

    public int getHighScore() {
        return highScore;
    }

    public int getComboMultiplier() {
        return comboMultiplier;
    }
}