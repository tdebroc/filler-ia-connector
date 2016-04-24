package com.tdebroc.myapp.filler.game.ia.utils;

import com.tdebroc.myapp.filler.game.Cell;
import com.tdebroc.myapp.filler.game.Game;
import com.tdebroc.myapp.filler.game.Grid;
import com.tdebroc.myapp.filler.game.Player;
import org.apache.commons.lang.SerializationUtils;

/**
 * Created by thibautdebroca on 23/04/16.
 */
public class Utils {

    public static Game getNextGameState(Game game, char c) {
        Game gameCloned = (Game) SerializationUtils.clone(game);
        gameCloned.playColor(gameCloned.currentPlayerPlaying(), c);
        return gameCloned;
    }

    public static boolean isCenterTaken(Game game) {
        Cell[][] grid = game.getGrid().getGrid();
        return grid[grid.length / 2][grid.length / 2].isControlled();

    }


}
