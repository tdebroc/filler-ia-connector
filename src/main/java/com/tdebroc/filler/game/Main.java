package com.tdebroc.filler.game;


import com.tdebroc.filler.connector.PlayerConnector;
import com.tdebroc.filler.game.ia.ManualIA;

public class Main {

    static String baseUrl = "http://localhost:8080";

    public static void main(String[] args) {

        ManualIAVsOther();

    }

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

    public static void SimpleIAVSManual(String[] args) {

        int gameId = PlayerConnector.addGame(baseUrl, 5);

        PlayerConnector playerConnector1 = new PlayerConnector(gameId, baseUrl);
        playerConnector1.registerPlayer("Bob");
        ManualIA manualIA = new ManualIA();

        PlayerConnector playerConnector2 = new PlayerConnector(gameId, baseUrl);
        playerConnector2.registerPlayer("Anna");

        Game game = playerConnector1.getGame();

        while (!game.isFinished()) {



        }

    }




}
