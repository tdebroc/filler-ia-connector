package com.tdebroc.myapp.filler.game;

import java.io.Serializable;

public class Cell implements Serializable {

    private char color;

    private boolean isControlled = false;

    public Cell(){}

    public Cell(char color) {
        this.color = color;
    }

    public char getColor() {
        return color;
    }

    public void setColor(char color) {
        this.color = color;
    }

    public boolean isControlled() {
        return isControlled;
    }

    public void setControlled(boolean controlled) {
        isControlled = controlled;
    }
}
