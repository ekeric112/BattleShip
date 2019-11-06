package com.company;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Destroyer extends Ship {
    private BufferedImage hImage = ImageIO.read(getClass().getResource("ship_destroyerh.png"));
    private BufferedImage vImage = ImageIO.read(getClass().getResource("ship_destroyerv.png"));

    public Destroyer(int length, Grid grid, SetShipMenu shipMenu) throws IOException {
        super(length, grid, shipMenu);
    }

    public String getType() {
        return "Destroyer";
    }

    public BufferedImage getIcon(boolean vertical) {
        if (vertical) {
            return vImage;
        } else {
            return hImage;
        }
    }


}
