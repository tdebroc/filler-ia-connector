package com.tdebroc.myapp.filler.game.ia;

import com.tdebroc.myapp.filler.game.Colors;
import com.tdebroc.myapp.filler.game.Game;

/**
 * Created by thibautdebroca on 03/04/16.
 */
public class ManualIA implements IA {
    @Override
    public char getNextMove(Game game) {
        return Colors.askColor(game.getPlayers());
    }
}
