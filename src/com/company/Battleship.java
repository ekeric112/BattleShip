package com.company;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Battleship extends Ship {
    private BufferedImage hImage = ImageIO.read(getClass().getResource("ship_cruiserh.png"));
    private BufferedImage vImage = ImageIO.read(getClass().getResource("ship_cruiserv.png"));

    public Battleship(int length, Grid grid, SetShipMenu shipMenu) throws IOException {
        super(length, grid, shipMenu);
    }

    public String getType() {
        return "Battleship";
    }

    public BufferedImage getIcon(boolean vertical) {
        if (vertical) {
            return vImage;
        } else {
            return hImage;
        }
    }

}
