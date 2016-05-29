package edu.isep.sixcolors.view.listener;


import edu.isep.sixcolors.model.Config;
import edu.isep.sixcolors.model.Game;
import edu.isep.sixcolors.model.GameState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class Load implements ActionListener {

    final JFileChooser fc = new JFileChooser();
    private Game game;

    public Load(Game game){
        this.game = game;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
            int returnVal = fc.showDialog(SwingUtilities.getRoot((Component) e.getSource()), Config.LOAD_SAVE_ACTION_NAME);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                try (
                    InputStream inFile = new FileInputStream(file.getAbsolutePath());
                    InputStream buffer = new BufferedInputStream(inFile);
                    ObjectInput input = new ObjectInputStream(buffer);
                ){
                    Game toBeLoaded = ((Game) input.readObject());
                    /*
                    if (game.getState() == GameState.CustomGrid) {
                        toBeLoaded.setState(GameState.CustomGrid);
                    }
                    else if (game.getState() == GameState.Game) {
                        toBeLoaded.setState(GameState.Game);
                    }
                    */
                    this.game.setGame(toBeLoaded);

                } catch (ClassNotFoundException | IOException e1) {
                    e1.printStackTrace();
                }


            }
    }

}
