package com.tdebroc.filler.game;


import com.tdebroc.filler.connector.Connector;
import com.tdebroc.filler.game.ia.ManualIA;

public class Main {

    static String baseUrl = "http://localhost:8080";

    public static void main(String[] args) {

        ManualIAVsOther();

    }

    public static void ManualIAVsOther() {

        int gameId = Connector.addGame(baseUrl, 5);
        System.out.println("Open a new game is " + gameId);

        Connector connector1 = new Connector(gameId, baseUrl);
        connector1.registerPlayer();
        Game game;

        ManualIA manualIA = new ManualIA();

        do {
            game = connector1.getOpponentMoves();
            char c = manualIA.getNextMove(game);
            connector1.sendMove(c);

        } while (!game.isFinished());

    }

    public static void SimpleIAVSManual(String[] args) {

        int gameId = Connector.addGame(baseUrl, 5);

        Connector connector1 = new Connector(gameId, baseUrl);
        connector1.registerPlayer();
        ManualIA manualIA = new ManualIA();

        Connector connector2 = new Connector(gameId, baseUrl);
        connector2.registerPlayer();

        Game game = connector1.getGame();

        while (!game.isFinished()) {



        }

    }




}
