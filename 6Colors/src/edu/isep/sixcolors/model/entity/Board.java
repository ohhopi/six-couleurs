package edu.isep.sixcolors.model.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

/**
 * Board model : represents the board of the game.
 */
public class Board implements Serializable {

    private int width;


    /**
     * Initialises a square board filled with random colors
     * TODO decide if the board is square or if it can be rectangular
     *
     * @param width of the board
     */
    public Board(int width) {
        this.tiles = new Tile[width][width];

        this.width = width;

        Random random = new Random();

        //Random Map Generation picking tiles colors from the TileColor enum
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < width; j++) {
                tiles[i][j] = new Tile(TileColor.random());
            }
        }
    }


    /**
     * Updates the board for a players turn
     *
     * @param player Player who just picked a new color
     * @param tileX  Abscissa of the first tile to update
     * @param tileY  Ordinate of the first tile to update
     */
    public void update(int tileX, int tileY, Player player) {
        Tile tile = getTile(tileX, tileY);
        boolean updateNeighbours = false;
        int[][] neighboursCoords = getNeighboursCoords(tileX, tileY);
        int neighbourCounter = 0;

        // The tile being updated is *always* neighbouring the current player's territory.
        if (tile.getTileColor() == player.getTileColor() && tile.getOwner() != player) {
            tile.setOwner(player);
            updateNeighbours = true;
            player.addPoints();
        }
        // updating color of conquered tiles
        else if (tile.getTileColor() == player.getPreviousTileColor() && tile.getOwner() == player) {
            tile.setTileColor(player.getTileColor());
            tile.setOwner(player);
            updateNeighbours = true;
        }



        for (int[] coords : neighboursCoords) {
            if (
                getTile(coords[0], coords[1]).getOwner() != player ||    // not in my territory
                getTile(coords[0], coords[1]).getTileColor() != player.getTileColor()    //
            ) {
                if (updateNeighbours) {
                    update(coords[0], coords[1], player);
                }
            }else {
                neighbourCounter++;
            }

        }
        if(neighbourCounter == neighboursCoords.length){
            tile.setTileColor(player.getTileColor());
            tile.setOwner(player);
            player.addPoints();
        }
    }
    /**
     * Squares of the board
     */
    private Tile[][] tiles;

    /**
     * Get a tile of the board
     *
     * @param line
     * @param row
     */
    public Tile getTile(int line, int row) {
        return this.tiles[line][row];
    }

    public Tile getTile(int[] coords) {
        return this.tiles[coords[0]][coords[1]];
    }

    /**
     * Get the whole tiles arrays
     *
     * @return Tile[][] tiles
     */
    public Tile[][] getTiles() {
        return this.tiles;
    }

    public int getWidth() {
        return width;
    }

    /**
     * Returns a list of the neighbours' coordinates of a given tile
     *
     * @param x Abscissa of the tile whose neighbours are wanted
     * @param y Ordinates of the tile whose neighbours are wanted
     * @return List of the neighbours' coordinates {0 : {neigh0x, neigh0y}, 1 : {neigh1x, neigh1y}}
     */
    public int[][] getNeighboursCoords(int x, int y) {

        // Using ArrayList because we're not sure how many neighbours actually exist
        ArrayList<int[]> neighbours = new ArrayList<int[]>();
        int[] coords;

        for (int k = -1; k <= 1; k++) {
            for (int l = -1; l <= 1; l++) {
                // Picking only direct neighbours (no diagonal neighbours)
                if ((k == 0 && l != 0) || (l == 0 && k != 0)) {
                    // Checking neighbour's existence
                    if (x + k >= 0 && x + k < this.tiles.length && y + l >= 0 && y + l < this.tiles.length) {
                        coords = new int[]{x + k, y + l};
                        neighbours.add(coords);
                    }
                }
            }
        }

        // Returning array because the neighbors list won't need to be edited later
        return neighbours.toArray(new int[neighbours.size()][2]);
    }

}