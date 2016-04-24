package com.tdebroc.myapp.filler.game.ia;

import com.tdebroc.myapp.filler.game.Colors;
import com.tdebroc.myapp.filler.game.Game;
import com.tdebroc.myapp.filler.game.Player;
import com.tdebroc.myapp.filler.game.ia.utils.Utils;

import java.util.List;

/**
 * Created by thibautdebroca on 18/04/16.
 */
public class MaxSquareWithDepthIA implements IA {

    @Override
    public char getNextMove(Game game) {
        List<Character> colors = Colors.getValidColors(game.getPlayers());

        Player p = game.getPlayers().get(game.getCurrentIdPlayerTurn());

        int scoreMax = 0;
        char charSelected = ' ';
        for (int i = 0; i < colors.size(); i++) {
            Game gameCloned = Utils.getNextGameState(game, colors.get(i));
            int newPlayerScore = gameCloned.getPlayers().get(gameCloned.getCurrentIdPlayerTurn()).getScore();
            if (scoreMax < newPlayerScore) {
                scoreMax = newPlayerScore;
                charSelected = colors.get(i);
            }
        }
        return charSelected;
    }





}
