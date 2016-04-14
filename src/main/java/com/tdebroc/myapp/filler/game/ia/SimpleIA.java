package com.tdebroc.myapp.filler.game.ia;

import com.tdebroc.myapp.filler.game.Colors;
import com.tdebroc.myapp.filler.game.Game;

/**
 * Created by thibautdebroca on 03/04/16.
 */
public class SimpleIA implements IA {

    @Override
    public char getNextMove(Game game) {
        char c;
        do {
            c = Colors.getRandomColor();
        } while (!Colors.isValidColor(c, game.getPlayers()));
        return c;
    }

}
