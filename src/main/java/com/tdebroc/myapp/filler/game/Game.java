package com.tdebroc.myapp.filler.game;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Entity
public class Game implements Serializable {

    public static final int MAX_NUM_PLAYER = 4;

    @Id
    private int idGame;

    @Column(columnDefinition="text")
    @Convert(converter = JpaConverterJson.class)
    private Grid grid;

    @Column(columnDefinition="text")
    @Convert(converter = JpaConverterJson.class)
    private List<Player> players;

    private Date dateCreated;

    private boolean started = false;

    private boolean finished = false;

    private int currentIdPlayerTurn = 0;

    private int round = 1;

    static final int DEFAULT_GRID_SIZE = 4;

    @Transient
    Position[] increments = new Position[]{
        new Position(1,0), new Position(0,1),new Position(-1,0),new Position(0,-1)
    };

    public void initGame() {
        initGame(DEFAULT_GRID_SIZE);
    }

    public void initGame(int gridSize) {
        setGrid(new Grid());
        getGrid().randomInit(gridSize);
        setPlayers(new ArrayList<Player>());
    }

    public void addPlayer() {
        addPlayer(Player.PLAYER_NAME_DEFAULT);
    }

    public void addPlayer(String playerName) {
        if (getPlayers().size() == 0) {
            addPlayer(0, 0, playerName);
        } else if (getPlayers().size() == 1) {
            addPlayer(getGrid().getGrid().length -1, getGrid().getGrid().length -1, playerName);
        } else if (getPlayers().size() == 2) {
            addPlayer(0, getGrid().getGrid().length -1, playerName);
        } else if (getPlayers().size() == 3) {
            addPlayer(getGrid().getGrid().length -1, 0, playerName);
        }
    }

    public void addPlayer(int x, int y, String playerName) {
        if (playerName == null) {
            playerName = Player.PLAYER_NAME_DEFAULT;
        }
        Position position = new Position(x, y);
        Player player = new Player();
        player.setPlayerName(playerName);
        player.setInitPosition(position);
        player.setPlayerColor(getGrid().getCell(x, y).getColor());
        getPlayers().add(player);
        calculateScore(getPlayers().get(getPlayers().size() - 1));
    }


    public void launchGame() {
        while (!determineIfGameIsFinished()) {
            System.out.println("Round " + getRound());
            playRound();
        }
        displayWinner();
    }

    public void playRound() {
        for (int i = 0; i < getPlayers().size(); i++) {
            getGrid().displayGrid();
            System.out.println("Player " + i + " it's your turn");
            Player currentPlayer = getPlayers().get(i);
            char c = Colors.askColor(getPlayers());
            playColor(currentPlayer, c);
            if (determineIfGameIsFinished()) {
                return;
            }

        }
    }



    public void playColor(Player currentPlayer, char c) {
        startEatColors(currentPlayer, c);
        currentPlayer.setPlayerColor(c);
        currentIdPlayerTurn++;
        if (currentIdPlayerTurn >= players.size()) {
            currentIdPlayerTurn = 0;
            round++;
        }
        calculateScore(currentPlayer);
        determineIfGameIsFinished();
    }


    public void startEatColors(Player currentPlayer, char c) {
        Set<Position> visited = new HashSet<Position>();
        Position pos = currentPlayer.getInitPosition();
        eatColors(currentPlayer, c, pos, visited);
    }



    private void eatColors(Player currentPlayer, char c, Position pos, Set<Position> visited) {
        if (visited.contains(pos)) {
            return;
        }
        visited.add(pos);
        if (getGrid().getCell(pos).getColor() != currentPlayer.getPlayerColor()) {
            return;
        }
        getGrid().getCell(pos).setColor(c);

        for (Position increment: increments) {
            int x = pos.getX() + increment.getX();
            int y = pos.getY() + increment.getY();
            if (x >= 0 && x < getGrid().getGrid().length && y >= 0 && y < getGrid().getGrid()[0].length) {
                eatColors(currentPlayer, c, new Position(x, y), visited);
            }
        }
    }


    public void calculateScore(Player currentPlayer) {
        currentPlayer.setScore(0);
        Set<Position> visited = new HashSet<Position>();
        Position pos = currentPlayer.getInitPosition();
        calculateScore(currentPlayer, pos, visited);
    }


    private void calculateScore(Player currentPlayer, Position pos, Set<Position> visited) {
        if (visited.contains(pos)) {
            return;
        }
        visited.add(pos);
        if (getGrid().getCell(pos).getColor() != currentPlayer.getPlayerColor()) {
            return;
        }
        currentPlayer.setScore(currentPlayer.getScore() + 1);
        getGrid().getCell(pos).setControlled(true);

        for (Position increment: increments) {
            int x = pos.getX() + increment.getX();
            int y = pos.getY() + increment.getY();
            if (x >= 0 && x < getGrid().getGrid().length && y >= 0 && y < getGrid().getGrid()[0].length) {
                calculateScore(currentPlayer, new Position(x, y), visited);
            }
        }
    }

    public boolean determineIfGameIsFinished() {
        finished = isGameFinished();
        return finished;
    }

    private boolean isGameFinished() {
        for (int i = 0; i < getPlayers().size(); i++) {
            if (getPlayers().get(i).getScore() > getGrid().getNumberOfCells() / 2) {
                return true;
            }
        }
        for (int i = 0; i < getGrid().getGrid().length; i++) {
            for (int j = 0; j < getGrid().getGrid()[i].length; j++) {
                if (!getGrid().getCell(i, j).isControlled()) {
                    return false;
                }
            }
        }
        return true;
    }

    public void displayWinner() {
        Map<Character, Integer> colorsCount = new HashMap<Character, Integer>();
        for (int i = 0; i < getGrid().getGrid().length; i++) {
            for (int j = 0; j < getGrid().getGrid()[i].length; j++) {
                char c = getGrid().getCell(i, j).getColor();
                if (!colorsCount.containsKey(c)) {
                    colorsCount.put(c, 1);
                } else {
                    colorsCount.put(c, colorsCount.get(c) + 1);
                }
            }
        }
        for (int i = 0; i < getPlayers().size(); i++) {
            Player p = getPlayers().get(i);
            System.out.println("Player " + i + " has " +  colorsCount.get(p.getPlayerColor()));
        }
    }

    public Player currentPlayerPlaying() {
        return players.get(currentIdPlayerTurn);
    }


    public Grid getGrid() {
        return grid;
    }

    public void setGrid(Grid grid) {
        this.grid = grid;
    }


    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public int getCurrentIdPlayerTurn() {
        return currentIdPlayerTurn;
    }

    public void setCurrentIdPlayerTurn(int currentIdPlayerTurn) {
        this.currentIdPlayerTurn = currentIdPlayerTurn;
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public int getIdGame() {
        return idGame;
    }

    public void setIdGame(int idGame) {
        this.idGame = idGame;
    }
}
