package com.tdebroc.myapp.filler.utils;

import com.tdebroc.myapp.filler.game.Game;
import com.tdebroc.myapp.filler.game.Player;
import com.tdebroc.myapp.filler.game.ia.utils.ShortestPathToCenter;
import org.junit.Test;

/**
 * Created by thibautdebroca on 23/04/16.
 */
public class ShortestPathToCenterTest {


    @Test
    public void testShortestPath() {
        Game game = new Game();
        game.initGame(20);
        game.addPlayer();
        game.addPlayer();

        game.getGrid().displayGrid();

        ShortestPathToCenter shortestPathToCenter = new ShortestPathToCenter();
        char c = shortestPathToCenter.getNextColorForShortestPath(game);
        System.out.println(c);
    }
}
