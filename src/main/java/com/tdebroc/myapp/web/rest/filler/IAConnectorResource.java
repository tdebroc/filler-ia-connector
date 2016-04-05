package com.tdebroc.myapp.web.rest.filler;

import com.tdebroc.filler.connector.MessageResponse;
import com.tdebroc.filler.game.Colors;
import com.tdebroc.filler.game.Game;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.util.*;

/**
 * Created by thibautdebroca on 02/04/16.
 */
@RestController
@RequestMapping(value = "/iaconnector", produces = MediaType.APPLICATION_JSON_VALUE)
public class IAConnectorResource {

    public static int NEXT_GAME_ID = 1;

    public static Map<Integer, Game> gamesMap = new HashMap<>();

    public static Map<String, PlayerInstance> playersInstances = new HashMap<>();

    public IAConnectorResource() throws URISyntaxException {
        addGame(13);
        Game game = gamesMap.get(1);
        PlayerInstance playerInstance = new PlayerInstance(1, game.getPlayers().size());
        game.addPlayer();
        playersInstances.put("123", playerInstance);
        PlayerInstance playerInstance2 = new PlayerInstance(1, game.getPlayers().size());
        game.addPlayer();
        playersInstances.put("124", playerInstance2);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/addGame")
    public int addGame(@RequestParam(value = "gridSize") int gridSize) throws URISyntaxException {
        if (gridSize >= 42) {
            return -1;
        }
        Game game = new Game();
        game.setDateCreated(new Date());
        game.initGame(gridSize);
        int newLyGameId = NEXT_GAME_ID;
        gamesMap.put(newLyGameId, game);
        NEXT_GAME_ID++;
        return newLyGameId;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/removeGame")
    public ResponseEntity<MessageResponse>  removeGame(@RequestParam(value = "idGame") int idGame) throws URISyntaxException {
        gamesMap.remove(idGame);
        return sendMessage(null, "OK");
    }

    @RequestMapping(method = RequestMethod.GET, value = "/game")
    public ResponseEntity<Game> game(@RequestParam(value = "idGame") int idGame) throws URISyntaxException {
        return new ResponseEntity<>(gamesMap.get(idGame), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/games")
    public ResponseEntity<Map<Integer, Game>> getGames() throws URISyntaxException {
        return new ResponseEntity<>(gamesMap, HttpStatus.OK);
    }


    @RequestMapping(method = RequestMethod.GET, value = "/startGame")
    public ResponseEntity<MessageResponse> startGame(@RequestParam(value = "idGame") int idGame) throws URISyntaxException {
        gamesMap.get(idGame).setStarted(true);
        return sendMessage(null, "OK");
    }


    @RequestMapping(method = RequestMethod.GET, value = "/addPlayer")
    public ResponseEntity<String> addPlayer(@RequestParam(value = "idGame") int idGame) throws URISyntaxException {
        Game game = gamesMap.get(idGame);
        PlayerInstance playerInstance = new PlayerInstance(idGame, game.getPlayers().size());
        game.addPlayer();
        String userId;
        do {
            userId = UUID.randomUUID().toString();
        } while (playersInstances.containsKey(userId));
        playersInstances.put(userId, playerInstance);
        return new ResponseEntity<>(userId, HttpStatus.OK);
    }


    @RequestMapping(method = RequestMethod.GET, value = "/sendMove")
    public ResponseEntity<MessageResponse> sendMove(@RequestParam(value = "playerUUID") String playerUUID,
                                            @RequestParam(value = "color") char color) throws URISyntaxException {
        PlayerInstance playerInstance = playersInstances.get(playerUUID);
        Game game = gamesMap.get(playerInstance.idGame);
        if (game.getCurrentIdPlayerTurn() != playerInstance.idPlayer) {
            return sendMessage("It's not the turn of player " + (playerInstance.idPlayer + 1), null);
        }
        if (!Colors.isExistingColor(color)) {
            return sendMessage("Color " + color + " is invalid (not in the list).", null);
        }
        if (Colors.isTakenColor(color, game.getPlayers())) {
            return sendMessage("Color " + color + " has been taken by another opponent.", null);
        }
        game.playColor(game.getPlayers().get(playerInstance.idPlayer), color);
        return sendMessage(null, "OK");
    }


    @RequestMapping(method = RequestMethod.GET, value = "/getOpponentMoves")
    public ResponseEntity<Game> getOpponentMoves(@RequestParam(value = "playerUUID") String playerUUID) throws URISyntaxException, InterruptedException {
        PlayerInstance playerInstance = playersInstances.get(playerUUID);
        Game game = gamesMap.get(playerInstance.idGame);
        int timeRequest = 0;
        while (game.getCurrentIdPlayerTurn() != playerInstance.idPlayer || !game.isStarted()) {

            Thread.sleep(100);
            timeRequest += 100;
            if (timeRequest > 60 * 1000 * 5) {
                return null;
            }
        }
        return new ResponseEntity<>(game, HttpStatus.OK);
    }



    public ResponseEntity<MessageResponse> sendMessage(String error, String message) {
        return new ResponseEntity<>(new MessageResponse(error, message), HttpStatus.OK);
    }



    public class PlayerInstance {
        int idGame;
        int idPlayer;
        Date timeStart;
        public PlayerInstance(int idGame, int idPlayer) {
            this.idPlayer = idPlayer;
            this.idGame = idGame;
            timeStart = new Date();
        }
    }
}
