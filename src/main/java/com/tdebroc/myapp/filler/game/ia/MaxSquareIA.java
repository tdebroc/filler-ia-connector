package com.tdebroc.myapp.filler.game.ia;

import com.tdebroc.myapp.filler.game.Colors;
import com.tdebroc.myapp.filler.game.Game;
import com.tdebroc.myapp.filler.game.Player;
import com.tdebroc.myapp.filler.game.ia.utils.ShortestPathToCenter;
import com.tdebroc.myapp.filler.game.ia.utils.Utils;
import org.apache.commons.lang.SerializationUtils;

import java.util.List;

/**
 * Created by thibautdebroca on 18/04/16.
 */
public class MaxSquareIA implements IA {

    @Override
    public char getNextMove(Game game) {

        if (!Utils.isCenterTaken(game)) {
            ShortestPathToCenter shortestPathToCenter = new ShortestPathToCenter();
            char c = shortestPathToCenter.getNextColorForShortestPath(game);
            System.out.println(c);
            return c;
        }

        List<Character> colors = Colors.getValidColors(game.getPlayers());

        Player p = game.getPlayers().get(game.getCurrentIdPlayerTurn());

        int scoreMax = 0;
        char charSelected = ' ';
        for (int i = 0; i < colors.size(); i++) {
            Game gameCloned = (Game) SerializationUtils.clone(game);
            int idCurrentPlayerTurn = gameCloned.getCurrentIdPlayerTurn();
            gameCloned.playColor(gameCloned.currentPlayerPlaying(), colors.get(i));
            int newPlayerScore = gameCloned.getPlayers().get(idCurrentPlayerTurn).getScore();
            if (scoreMax < newPlayerScore) {
                scoreMax = newPlayerScore;
                charSelected = colors.get(i);
            }
        }
        return charSelected;
    }
}
