package com.tdebroc.filler.connector;

import java.util.Date;

/**
 * Created by thibautdebroca on 06/04/16.
 */
public class PlayerInstance {
    public int idGame;
    public String UUID;
    public int idPlayer;
    public Date timeStart;
    public PlayerInstance(int idGame, int idPlayer, String userId) {
        this.idPlayer = idPlayer;
        this.idGame = idGame;
        this.UUID = userId;
        timeStart = new Date();
    }
}
