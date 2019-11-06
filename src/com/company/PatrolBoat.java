package com.company;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class PatrolBoat extends Ship {
    private BufferedImage hImage = ImageIO.read(getClass().getResource("ship_boath.png"));
    private BufferedImage vImage = ImageIO.read(getClass().getResource("ship_boatv.png"));

    public PatrolBoat(int length, Grid grid, SetShipMenu shipMenu) throws IOException {
        super(length, grid, shipMenu);
    }

    public String getType() {
        return "PatrolBoat";
    }

    public BufferedImage getIcon(boolean vertical) {
        if (vertical) {
            return vImage;
        } else {
            return hImage;
        }
    }

}
