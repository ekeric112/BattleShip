package com.company;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Carrier extends Ship {
    private BufferedImage hImage = ImageIO.read(getClass().getResource("ship_airCarrierh.png"));
    private BufferedImage vImage = ImageIO.read(getClass().getResource("ship_airCarrierv.png"));

    public Carrier(int length, Grid grid, SetShipMenu shipMenu) throws IOException {
        super(length, grid, shipMenu);
    }

    public String getType() {
        return "Carrier";
    }

    public BufferedImage getIcon(boolean vertical) {
        if (vertical) {
            return vImage;
        } else {
            return hImage;
        }
    }


}
