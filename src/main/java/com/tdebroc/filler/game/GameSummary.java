package com.tdebroc.filler.game;

import java.util.Date;

/**
 * Created by thibautdebroca on 05/04/16.
 */
public class GameSummary {

    private Date dateCreated;


    public GameSummary() {
    }

    public GameSummary(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

}
