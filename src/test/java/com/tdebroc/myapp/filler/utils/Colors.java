package com.tdebroc.myapp.filler.utils;

import com.tdebroc.myapp.filler.game.Game;
import org.junit.Test;

/**
 * Created by thibautdebroca on 23/04/16.
 */
public class Colors {

    @Test
    public void testValidColors() {

        Game game = new Game();
        game.initGame(20);
        game.addPlayer();

        game.getGrid().displayGrid();
        // Colors.
    }
}
