package edu.isep.sixcolors;
import edu.isep.sixcolors.controller.*;
import edu.isep.sixcolors.model.*;
import edu.isep.sixcolors.view.*;

import java.util.Random;

// TODO factorize random color generation (duplicated in this and model.Board)

/**
 * TODO annotate
 */
public class SixColors {
	
	/**
	 * Main method of the game (currently)
	 * @param args
	 */
	public static void main(String[] args) {
		Random random = new Random();
		
		Game game = new Game();
		
		String name;
		for(int i = 0; i < game.getPlayers().length; i++) {
			name = Console.promptPlayerName(i + 1);
			game.getPlayer(i).setName(name);
		}
		
		// TODO store board width as an independant parameter
		
		Board board = new Board(4);
		
		board.getTile(0, 0).setOwner(game.getPlayer(0));
		while(board.getTile(3, 3).getColor() == board.getTile(0, 0).getColor()) {
			board.getTile(3, 3).setColor(Colors.values()[random.nextInt(Colors.values().length)]);
		}
		
		board.getTile(3, 3).setOwner(game.getPlayer(1));
		
		// TODO let players choose their colors
		// TODO allow for more than 2 players
		
		Console.showBoard(board);
		
		
	}

}
