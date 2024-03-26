package com.example.wrongsiderocky;

//----------------------------------------------------------------------------
//Player
//Class to store player name, distance travelled and score for the leaderboard
//-----------------------------------------------------------------------------
public class Player {
    private final String playerName;
    private long highestDistance;
    private int highestScore;


    public Player(String playerName, long highestDistance, int highestScore) {
        this.playerName = playerName;
        this.highestDistance = highestDistance;
        this.highestScore = highestScore;
    }

    public String getPlayerName() {
        return playerName;
    }

    public long getHighestDistance() {
        return highestDistance;
    }


    public int getHighestScore() {
        return highestScore;
    }

    public long getTotalScore() {
        return highestScore+highestDistance;
    }

}
