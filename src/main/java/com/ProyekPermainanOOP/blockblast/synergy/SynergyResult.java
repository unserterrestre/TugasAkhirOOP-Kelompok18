package com.ProyekPermainanOOP.blockblast.synergy;

/**
 * Menyimpan hasil akumulasi efek dari rantai Sinergi Kaligrafi (Hanzi Word Recipes).
 */
public class SynergyResult {
    private boolean triggered;
    private String recipeName;
    private int bonusScore;
    private int bonusLives;

    public SynergyResult() {
        this.triggered = false;
        this.recipeName = "";
        this.bonusScore = 0;
        this.bonusLives = 0;
    }

    public void addEffect(String name, int extraScore, int extraLives) {
        this.triggered = true;
        if (this.recipeName.isEmpty()) {
            this.recipeName = name;
        } else {
            this.recipeName += " + " + name;
        }
        this.bonusScore += extraScore;
        this.bonusLives += extraLives;
    }

    public boolean isTriggered() {
        return triggered;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public int getBonusScore() {
        return bonusScore;
    }

    public int getBonusLives() {
        return bonusLives;
    }
}