package edu.isep.sixcolors.view.graphic;


import edu.isep.sixcolors.controller.Game;
import edu.isep.sixcolors.model.Board;
import edu.isep.sixcolors.model.Tile;
import edu.isep.sixcolors.model.TileColor;

import java.awt.*;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;



public class GameWindow extends JFrame{
    private JPanel container = new JPanel();
    private JPanel gamePanel = new JPanel();
    private JPanel playerList = new JPanel();
    private JPanel colorButtons = new JPanel();
    private JLabel title = new JLabel();
    private Game game;

    private MouseListener listener = new MouseListener() {
        @Override
        public void mouseClicked(MouseEvent e) {
            JButton fire = (JButton) e.getComponent();
            Color color = fire.getBackground();

            for(TileColor tc: TileColor.values()) {
                if(color == tc.getColor()) {
                    game.getCurrentPlayer().setTileColor(tc);
                }
            }

        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    };


    public GameWindow(Board board, Game game){
        this.game = game;
        this.setTitle("Six Colors Game");
        this.setSize(800, 500);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);

        this.setContentPane(container);
        container.setLayout(new BorderLayout());

        gamePanel.setBackground(Color.BLACK);
        gamePanel.setLayout(new GridBagLayout());

        updateBoard(board);
        playerList.setBackground(Color.RED);

        colorButtons.setLayout(new FlowLayout());
        updateColorButtons();

        this.getContentPane().add(gamePanel, BorderLayout.CENTER);
        this.getContentPane().add(playerList, BorderLayout.EAST);
        this.getContentPane().add(colorButtons, BorderLayout.SOUTH);

        title.setText("Six Colors");
        Font police = new Font("Roboto", Font.BOLD, 16);
        title.setFont(police);
        title.setForeground(Color.BLACK);
        title.setHorizontalAlignment(JLabel.CENTER);
        this.getContentPane().add(title, BorderLayout.NORTH);

        this.setVisible(true);
    }

    private void updateColorButtons(){

        JButton[] buttons = new JButton[6];

        TileColor[] tileColors = TileColor.values();

        for(int i = 0; i < tileColors.length; i++) {
            buttons[i] = new JButton();
            buttons[i].setText(tileColors[i].name());
            buttons[i].setBackground(tileColors[i].getColor());
            buttons[i].addMouseListener(listener);
            colorButtons.add(buttons[i]);
        }
    }

    private void updateBoard(Board board){
        // TODO Force square show
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridheight = 1;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx=1;
        gbc.weighty=1;

        for (gbc.gridy = 0; gbc.gridy < board.getTiles().length; gbc.gridy++) {
            for (gbc.gridx = 0; gbc.gridx < board.getTiles().length; gbc.gridx++) {
                JPanel cell = new JPanel();
                cell.setBackground(board.getTile(gbc.gridy, gbc.gridx).getTileColor().getColor());
                gamePanel.add(cell, gbc);
            }
        }
    }
}
