package edu.isep.sixcolors.controller;

import edu.isep.sixcolors.model.*;

import edu.isep.sixcolors.model.AI.*;

import edu.isep.sixcolors.model.entity.Board;
import edu.isep.sixcolors.model.entity.Player;
import edu.isep.sixcolors.model.entity.Players;
import edu.isep.sixcolors.model.entity.TileColor;

class Play {

    private final Game game;
    private TileColor currentSelectedColor = TileColor.Blue;


    public Play(Game game){
        this.game = game;
    }

    public void control(String sourceText, int boardSize, int playerNb, String[] playerName, String[] playerType, String sourceActionCommand) {

        switch(game.getState()) {

            case Menu:
                switch(sourceActionCommand) {
                    case Config.NEW_LOCAL_GAME_BUTTON_TEXT:
                        game.setState(GameState.GridConfig);
                        break;
                    case Config.EXIT_BUTTON_TEXT:
                        System.exit(0);
                        break;
                }
                break;
            case GridConfig:
                initGrid(boardSize, playerNb);
                break;
            case NameConfig:
                boolean emptyName = initPlayers(playerName, playerType);
                if (!emptyName) {
                    switch (sourceActionCommand) {
                        case Config.RANDOM_BOARD_BUTTON_TEXT:
                            game.setState(GameState.Game);
                            break;
                        case Config.CUSTOM_BOARD_BUTTON_TEXT:
                            game.setState(GameState.CustomGrid);
                            break;
                    }
                }
                break;
            case CustomGrid:
                switch (sourceActionCommand) {
                    case "Play":
                        game.setState(GameState.Game);
                        break;

                    default:
                        if (sourceActionCommand.contains(":")){
                            int[] coords = new int[2];
                            int i = 0;
                            for (String str : sourceActionCommand.split(":")) {
                                coords[i] = Integer.parseInt(str);
                                i++;
                            }
                            game.getBoard().getTile(coords).setTileColor(currentSelectedColor);
                            game.customNotify();
                        } else{
                            try {
                                currentSelectedColor = TileColor.parseTileColor(sourceText);
                            } catch (Exception e1) {
                                e1.printStackTrace();
                            }
                        }
                        break;
                }
                break;
            case Game:
                colorButtonPressed(sourceActionCommand);
                break;
            case End:
                // TODO put this to config (here and in Window.java:322)
                if(sourceActionCommand.equals("Main Menu")) {
                    // this.game = new Game();

                    // this could *maybe* induce a bug where the new grid is polluted by the remainder of
                    // the old one ; declare a Game.clean() method and invoke it here in case it happens.
                    game.setState(GameState.Menu);
                }
                break;
        }
    }

    private void initGrid(int boardSize, int playerNb){

            // Check if the inputs are within boundaries :
            if (boardSize >= Config.GRID_MIN && boardSize <= Config.GRID_MAX && playerNb >= Config.PLAYER_NB_MIN && playerNb <= Config.PLAYER_NB_MAX){

                game.setBoard(new Board(boardSize));

                Players players = new Players(playerNb);
                players.setPlayerNumber(playerNb);
                game.setPlayers(players);

                // Set game state :
                game.setState(GameState.NameConfig);
            }
    }

    private boolean initPlayers(String[] playerName, String[] playerType){
        int playerNb = game.getPlayers().getPlayerNumber();
        Players players = game.getPlayers();
        boolean emptyName = false;
        for (int i = 0; i < playerNb; i++) {
            if (playerName[i] == null || playerName[i].equals("")) {
                emptyName = true;
            } else {
                players.setPlayer(i, new Player(playerName[i]));
                AIInterface AI = null;

                switch(playerType[i]) {
                    case "Human":
                        break;
                    case "Dumb AI":
                        AI = new DumbAI();
                        break;
                    case "Greedy AI":
                        AI = new GreedyAI();
                        break;
                    case "Machiavelic AI":
                        AI = new DumbAI();
                        break;
                    case "Clever AI":
                        AI = new CleverAI();
                        break;
                    case "Genius AI":
                        AI = new GeniusAI();
                }

                if(AI != null) {
                    players.getPlayer(i).setAi(true);
                    players.getPlayer(i).setAIInstance(AI);
                }
            }
        }
        if (!emptyName){
                game.initGame();
        }
        return emptyName;
    }

    /**
     * Returns the winner or null if there is none yet
     *
     * @return Player winner or null;
     */

    private Player checkForWinner() {
        int winPoints = (int) Math.floor(Math.pow(game.getBoard().getWidth(), 2) / 2);
        int totalPoints = 0;
        int maxPoints = 0;
        int playerPoints;
        Player winner = game.getCurrentPlayer();
        for (int i = 0; i < game.getPlayers().getPlayerNumber(); i++) {
            playerPoints = game.getPlayers().getPlayer(i).getPoints();
            if (playerPoints > maxPoints) {
                maxPoints = playerPoints;
                winner = game.getPlayers().getPlayer(i);
            }
            totalPoints += game.getPlayers().getPlayer(i).getPoints();
        }
        if (game.getCurrentPlayer().getPoints() > winPoints || totalPoints == Math.pow(game.getBoard().getWidth(), 2)) {
            return winner;
        }
        return null;

    }

    private void colorButtonPressed(String sourceActionCommand){
        // 1. Fetch the current player & declare choice to catch :
        Player currentPlayer = game.getCurrentPlayer();
        TileColor chosenColor = null;

        // 2. Get what color the player has chosen :
        if(game.getCurrentPlayer().isAi()) {
            // If it's an AI, then we wait for the ai to send back a choice
            chosenColor = currentPlayer.getAIInstance().colorChoice(game);

        } else {
            // If it's not an AI, then we wait for the physical player to make a choice in the view
            //Parse the color choice of the player :
            try {
                chosenColor = TileColor.parseTileColor(sourceActionCommand);

            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }

        // 3. Set the current player's color :
        currentPlayer.setTileColor(chosenColor);


        // 4. Update the board to apply the color choice :
        game.updateBoard (
                currentPlayer.getStartingTile(),
                currentPlayer
        );

        //5. Checks if a player has won :
        Player winner = checkForWinner();

        if (winner != null) {
            game.setWinner(winner);
            game.setState(GameState.End);
        }

        game.nextPlayer();
    }

}
