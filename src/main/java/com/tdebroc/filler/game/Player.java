package com.tdebroc.filler.game;

public class Player {

	private Position initPosition;

	private char playerColor;

	private int score;

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
}
