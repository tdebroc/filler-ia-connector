package com.tdebroc.filler.game.ia;

import com.tdebroc.filler.game.Colors;
import com.tdebroc.filler.game.Game;
import com.tdebroc.filler.game.ia.IA;

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
