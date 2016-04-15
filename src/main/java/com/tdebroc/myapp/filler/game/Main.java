package com.tdebroc.myapp.filler.game;


import com.tdebroc.myapp.filler.connector.PlayerConnector;
import com.tdebroc.myapp.filler.game.ia.IA;
import com.tdebroc.myapp.filler.game.ia.ManualIA;
import com.tdebroc.myapp.filler.game.ia.SimpleIA;

public class Main {

    static String baseUrl = "http://62.210.105.118:8081";

    public static void main(String[] args) {

        // ManualIAVsOther();
        //onlyIA(44);
        IAversusIA(2);
    }

    //================================================
    // Example 1: Simple IA subscribing
    //================================================
    public static void onlyIA(int gameId) {
        System.out.println("Open a new game is " + gameId);
        PlayerConnector playerConnector1 = new PlayerConnector(gameId, baseUrl);
        playerConnector1.registerPlayer("Carl");
        Game game = playerConnector1.getGame();
        IA simpleIA = new SimpleIA();

        do {
            game = playerConnector1.getOpponentMoves();
            char c = simpleIA.getNextMove(game);
            playerConnector1.sendMove(c);

        } while (!game.isFinished());

    }

    //================================================
    // Example 2: Manual IA playing.
    //================================================
    public static void ManualIAVsOther() {

        int gameId = PlayerConnector.addGame(baseUrl, 5);
        System.out.println("Open a new game is " + gameId);

        PlayerConnector playerConnector1 = new PlayerConnector(gameId, baseUrl);
        playerConnector1.registerPlayer("Carl");
        Game game;

        ManualIA manualIA = new ManualIA();

        do {
            game = playerConnector1.getOpponentMoves();
            char c = manualIA.getNextMove(game);
            playerConnector1.sendMove(c);

        } while (!game.isFinished());

    }


    //================================================
    // Example 3: IA VS IA.
    //================================================
    public static void IAversusIA(int gameId) {
        System.out.println("Open a new game is " + gameId);

        PlayerConnector playerConnector1 = new PlayerConnector(gameId, baseUrl);
        playerConnector1.registerPlayer("Carl");

        PlayerConnector playerConnector2 = new PlayerConnector(gameId, baseUrl);
        playerConnector2.registerPlayer("Bob");
        Game game = playerConnector1.getGame();
        IA simpleIA = new SimpleIA();
        IA simpleIA2 = new SimpleIA();

        do {
            game = playerConnector1.getOpponentMoves();
            char c = simpleIA.getNextMove(game);
            playerConnector1.sendMove(c);

            game = playerConnector1.getOpponentMoves();
            c = simpleIA2.getNextMove(game);
            playerConnector2.sendMove(c);

        } while (!game.isFinished());

    }
}
