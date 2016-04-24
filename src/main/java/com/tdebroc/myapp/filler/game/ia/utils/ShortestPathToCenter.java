package com.tdebroc.myapp.filler.game.ia.utils;

import com.tdebroc.myapp.filler.game.Colors;
import com.tdebroc.myapp.filler.game.Game;
import com.tdebroc.myapp.filler.game.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thibautdebroca on 23/04/16.
 */
public class ShortestPathToCenter {

    public int currentShortestPath = Integer.MAX_VALUE;

    public char getNextColorForShortestPath(Game game) {
        List<Character> validColors = Colors.getValidColors(game.getPlayers());

        Player player = game.currentPlayerPlaying();
        List<Player> players = new ArrayList<Player>();
        players.add(player);
        game.setPlayers(players);


        String s = getShortestPathToCenter(game, "", validColors);

        return s.charAt(0);
    }

    public String getShortestPathToCenter(Game game, String s, List<Character> validColors) {


        if (Utils.isCenterTaken(game)) {
            System.out.println(s);
            game.getGrid().displayGrid();
            currentShortestPath = Math.min(s.length(), currentShortestPath);
            return s;
        }
        if (s.length() > currentShortestPath) {
            return s;
        }

        int currentPlayerScore = game.currentPlayerPlaying().getScore();
        String shortestPath = null;
        for (int i = 0; i < validColors.size(); i++) {

            Game gameNextState = Utils.getNextGameState(game, validColors.get(i));
            if (currentPlayerScore == gameNextState.currentPlayerPlaying().getScore()){
                continue;
            }

            String currentPath = getShortestPathToCenter(gameNextState, s + validColors.get(i),
                                                        Colors.getValidColors(gameNextState.getPlayers()));
            if (shortestPath == null) {
                shortestPath = currentPath;
            } else {
                if (currentPath.length() < shortestPath.length()) {
                    shortestPath = currentPath;
                }
            }
        }
        return shortestPath;
    }




}
