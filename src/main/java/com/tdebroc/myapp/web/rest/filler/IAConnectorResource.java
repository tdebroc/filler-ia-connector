package com.tdebroc.myapp.web.rest.filler;

import com.tdebroc.filler.connector.MessageResponse;
import com.tdebroc.filler.game.Colors;
import com.tdebroc.filler.game.Game;
import com.tdebroc.filler.game.GameSummary;
import com.tdebroc.myapp.security.SecurityUtils;
import com.tdebroc.myapp.web.websocket.dto.ActivityDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URISyntaxException;
import java.security.Principal;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.*;

import static com.tdebroc.myapp.config.WebsocketConfiguration.IP_ADDRESS;

/**
 * Created by thibautdebroca on 02/04/16.
 */
@RestController
@Controller
@RequestMapping(value = "/iaconnector", produces = MediaType.APPLICATION_JSON_VALUE)
public class IAConnectorResource {

    @Inject
    SimpMessageSendingOperations messagingTemplate;

    public static int NEXT_GAME_ID = 1;

    public static Map<Integer, Game> gamesMap = new HashMap<>();

    public static Map<String, PlayerInstance> playersInstances = new HashMap<>();

    public IAConnectorResource() throws URISyntaxException {
        addGameToGamesMap(13);
        Game game = gamesMap.get(1);
        PlayerInstance playerInstance = new PlayerInstance(1, game.getPlayers().size());
        game.addPlayer();
        playersInstances.put("123", playerInstance);
        PlayerInstance playerInstance2 = new PlayerInstance(1, game.getPlayers().size());
        game.addPlayer();
        playersInstances.put("124", playerInstance2);
    }

    public int addGameToGamesMap(int gridSize) {
        Game game = new Game();
        game.setDateCreated(new Date());
        game.initGame(gridSize);
        int newLyGameId = NEXT_GAME_ID;
        game.setIdGame(newLyGameId);
        gamesMap.put(newLyGameId, game);
        NEXT_GAME_ID++;
        return newLyGameId;
    }

    //==================================================================================================================
    //= Sockets
    //==================================================================================================================
    private void refreshGames() {
        messagingTemplate.convertAndSend("/topic/refreshGames", buildGamesSummarized());
    }
    private void refreshGame(Game game) {
        messagingTemplate.convertAndSend("/topic/refreshGame", game);
    }

    //==================================================================================================================
    //= Resource
    //==================================================================================================================



    @RequestMapping(method = RequestMethod.GET, value = "/addGame")
    public int addGame(@RequestParam(value = "gridSize") int gridSize) throws URISyntaxException {
        if (gridSize >= 42) {
            return -1;
        }
        int newId = addGameToGamesMap(gridSize);
        refreshGames();
        return newId;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/removeGame")
    public ResponseEntity<MessageResponse>  removeGame(@RequestParam(value = "idGame") int idGame) throws URISyntaxException {
        gamesMap.remove(idGame);
        refreshGames();
        return sendMessage(null, "OK");
    }


    @RequestMapping(method = RequestMethod.GET, value = "/game")
    public ResponseEntity<Game> game(@RequestParam(value = "idGame") int idGame) throws URISyntaxException {
        return new ResponseEntity<>(gamesMap.get(idGame), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/games")
    public ResponseEntity<Map<Integer, GameSummary>> getGames() throws URISyntaxException {
        return new ResponseEntity<>(buildGamesSummarized(), HttpStatus.OK);
    }

    private Map<Integer,GameSummary> buildGamesSummarized() {
        Map<Integer, GameSummary> gamesSummarized = new HashMap<Integer, GameSummary>();
        for (Integer key : gamesMap.keySet()) {
            gamesSummarized.put(key, new GameSummary(gamesMap.get(key).getDateCreated()));
        }
        return gamesSummarized;
    }


    @RequestMapping(method = RequestMethod.GET, value = "/startGame")
    public ResponseEntity<MessageResponse> startGame(@RequestParam(value = "idGame") int idGame) throws URISyntaxException {
        gamesMap.get(idGame).setStarted(true);
        refreshGame(gamesMap.get(idGame));
        return sendMessage(null, "OK");
    }



    @RequestMapping(method = RequestMethod.GET, value = "/addPlayer")
    public ResponseEntity<String> addPlayer(@RequestParam(value = "idGame") int idGame) throws URISyntaxException {
        Game game = gamesMap.get(idGame);
        if (game.isStarted() || game.getPlayers().size() >= 4) {
            return null;
        }
        PlayerInstance playerInstance = new PlayerInstance(idGame, game.getPlayers().size());
        game.addPlayer();
        refreshGame(game);
        String userId;
        do {
            userId = UUID.randomUUID().toString();
        } while (playersInstances.containsKey(userId));
        playersInstances.put(userId, playerInstance);
        // TODO: return player UUID + idTurnPlayer
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
        refreshGame(game);
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
