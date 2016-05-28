package edu.isep.sixcolors;

import edu.isep.sixcolors.controller.Play;
import edu.isep.sixcolors.model.Game;
import edu.isep.sixcolors.view.Window;

public class SixColors {

    public static void main(String[] args) {

        //Model
        Game game = new Game();

        //Controller
        Play play = new Play(game);

        //View
        Window view = new Window(play, game);

        game.addObserver(view);
        //game.getPlayers().addObserver(view);
        //game.getBoard().addObserver(view);

    }
}