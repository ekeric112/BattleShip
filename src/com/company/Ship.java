package com.company;


import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Ship {
    private int x;
    private int y;
    private int shipCount;
    private int length;
    private boolean isVertical;
    private Grid grid;
    private SetShipMenu shipMenu;
    private int hp;
    private BufferedImage hImage;

    public Ship(int length, Grid grid, SetShipMenu shipMenu) {
        this.length = length;
        this.grid = grid;
        this.shipMenu = shipMenu;
        shipCount = 0;
        hp = length-1;
    }

    public void setShipCount(int shipCount) {
        this.shipCount = shipCount;
    }

    /**
     * Shows whether the ship has sunk
     * @return true if the ship has sunk, false if hit, but not sunk
     */
    public boolean Sink(){
        if (hp > 0){
            hp--;
            return false;
        } else {
            return true;
        }
    }

    public int getShipCount() {
        return shipCount;
    }

    public void setVertical(boolean vert) {
        isVertical = vert;
    }

    public boolean isVertical() {
        return isVertical;
    }

    public boolean isSunk(){
        return hp == 0;

    }

    /**
     * places ship at given coordinates
     * @param point point to place
     * @param grid grid to be placed on
     * @throws IOException error if ship file not found
     */
    public void placeShip(Point point, Ship[][] grid) throws IOException {
        if (shipCount == 0 && shipMenu.isPositionValid(point, isVertical, length) && shipMenu.isPositionAvailable(point, isVertical(), getLength(), grid)) {
            shipMenu.add(this, point, grid, "Ship");
            shipCount++;
        } else {
            System.out.println("Position not available!");
        }
    }

    /**
     * places ship on target grid
     * @param point
     * @param grid
     * @throws IOException
     */
    public void placeTargetShip(Point point, Ship[][] grid) throws IOException {
        shipMenu.add(this, point, grid, "Target");
    }


    public int getLength() {
        return length;
    }

    public String getType() {
        return "";
    }

    public BufferedImage getIcon(boolean isVertical) throws IOException {

        return hImage;
    }

    /**
     * get the marker for ship
     * @return the first letter of name
     */
    public String getMarker() {
        return getType().substring(0, 1);
    }


}
