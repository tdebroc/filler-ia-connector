package com.tdebroc.myapp.filler.game;

public class Player {

	private Position initPosition;

	private char playerColor;

    private String playerName;

	private int score;

    public final static String PLAYER_NAME_DEFAULT = "Anonymous";

	public char getPlayerColor() {
		return playerColor;
	}

	public void setPlayerColor(char playerColor) {
		this.playerColor = playerColor;
	}

	public Position getInitPosition() {
		return initPosition;
	}

	public void setInitPosition(Position initPosition) {
		this.initPosition = initPosition;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    @Override
    public String toString() {
        return "Player{" +
            "initPosition=" + initPosition +
            ", playerColor=" + playerColor +
            ", playerName='" + playerName + '\'' +
            ", score=" + score +
            '}';
    }
}
