package com.company;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Submarine extends Ship {
    private BufferedImage hImage = ImageIO.read(getClass().getResource("ship_submarineh.png"));
    private BufferedImage vImage = ImageIO.read(getClass().getResource("ship_submarinev.png"));

    public Submarine(int length, Grid grid, SetShipMenu shipMenu) throws IOException {
        super(length, grid, shipMenu);
    }

    public String getType() {
        return "Submarine";
    }

    public BufferedImage getIcon(boolean vertical) {
        if (vertical) {
            return vImage;
        } else {
            return hImage;
        }
    }

}
