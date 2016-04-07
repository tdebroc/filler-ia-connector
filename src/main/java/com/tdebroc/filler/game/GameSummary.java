package com.tdebroc.filler.game;

import java.util.Date;

/**
 * Created by thibautdebroca on 05/04/16.
 */
public class GameSummary {

    private Date dateCreated;

    private int idGame;


    public GameSummary(Game game) {
        dateCreated = game.getDateCreated();
        idGame = game.getIdGame();
    }


    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public int getIdGame() {
        return idGame;
    }

    public void setIdGame(int idGame) {
        this.idGame = idGame;
    }
}
