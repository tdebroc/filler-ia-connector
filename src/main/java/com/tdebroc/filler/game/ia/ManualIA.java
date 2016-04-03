package com.tdebroc.filler.game.ia;

import com.tdebroc.filler.game.Colors;
import com.tdebroc.filler.game.Game;
import com.tdebroc.filler.game.ia.IA;

/**
 * Created by thibautdebroca on 03/04/16.
 */
public class ManualIA implements IA {
    @Override
    public char getNextMove(Game game) {
        return Colors.askColor(game.getPlayers());
    }
}
