package com.example.wrongsiderocky;

public class Player {
    private String playerName;
    private long highestDistance;
    private int highestScore;
    private long totalScore;

    public Player(String playerName, long highestDistance, int highestScore) {
        this.playerName = playerName;
        this.highestDistance = highestDistance;
        this.highestScore = highestScore;
        this.totalScore = highestScore + highestDistance;
    }

    public String getPlayerName() {
        return playerName;
    }

    public long getHighestDistance() {
        return highestDistance;
    }

    public void setHighestDistance(long highestDistance) {
        this.highestDistance = highestDistance;
    }

    public int getHighestScore() {
        return highestScore;
    }

    public void setHighestScore(int highestScore) {
        this.highestScore = highestScore;
    }

    public long getTotalScore() {
        return highestScore+highestDistance;
    }

}
