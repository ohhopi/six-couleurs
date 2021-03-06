package edu.isep.sixcolors.model.entity;


import java.io.Serializable;

/**
 * Tile model : Represents a square of the board
 */
public class Tile implements Serializable {

    private Tile[] neighbors;

    /**
     * Constructor taking the initial tileColor of the tile
     *
     * @param tileColor element of Colors enum
     */
    public Tile(TileColor tileColor) {
        this.tileColor = tileColor;
    }

    private Player owner;

    /**
     * Colors of the square
     */
    private TileColor tileColor;

    /**
     * @return the tileColor
     */
    public TileColor getTileColor() {
        return tileColor;
    }

    /**
     * @param tileColor the tileColor to set
     */
    public void setTileColor(TileColor tileColor) {
        this.tileColor = tileColor;
    }

    /**
     * @return the tile owner
     */
    public Player getOwner() {
        return owner;
    }

    /**
     * @param owner the owner to set
     */
    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public Tile[] getNeighbors() {
        return neighbors;
    }

    public void setNeighbors(Tile[] neighbors) {
        this.neighbors = neighbors;
    }

}
