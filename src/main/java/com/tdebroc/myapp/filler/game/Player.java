package com.tdebroc.myapp.filler.game;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Player {

	private Position initPosition;

	private char playerColor;

    private String playerName;

	private int score;

    private int idGame;

    @JsonIgnore
    private String uuid = "321123";

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

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
