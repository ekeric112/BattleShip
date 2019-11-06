package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

//TODO set buttons to unclickable after game over
public class Grid extends JFrame {
    private final int SIZE = 8;
    private final int TOTAL_SHIP_NUM = 5;
    private final int BUTTON_SIZE = 34;
    private Dimension dim;
    private Ship[][] shipGrid;
    private Ship[][] targetGrid;
    private JButton shipFieldGUI[][];
    private JPanel gameLayout;
    private JPanel targetPanel;
    private JPanel shipPanel;
    private JLabel gameLabel;
    private int randRow;
    private int randCol;
    private int newRow;
    private int newCol;
    private int userShipDestroyed;
    private String predictDir;
    private boolean alreadyGuessed;
    private boolean shipHit;
    private boolean bypass;
    private ArrayList<Point> alreadyGuessedList = new ArrayList<>();

    public Grid(Ship[][] shipGrid, Ship[][] targetGrid, JButton shipFieldGUI[][], JPanel shipPanel, JPanel targetPanel) {
        this.shipPanel = shipPanel;
        this.targetPanel = targetPanel;
        this.shipGrid = shipGrid;
        this.targetGrid = targetGrid;
        this.shipFieldGUI = shipFieldGUI;

        createComponents();
    }

    private void createComponents() {
        bypass = false;
        alreadyGuessed = false;
        shipHit = false;
        userShipDestroyed = 0;
        dim = Toolkit.getDefaultToolkit().getScreenSize();
        gameLayout = new JPanel(new BorderLayout());
        gameLabel = new JLabel("BATTLESHIP");
        gameLabel.setFont(new Font("Times New Roman", Font.BOLD, 48));

        gameLayout.add(gameLabel, BorderLayout.NORTH);
        gameLayout.add(targetPanel, BorderLayout.WEST);
        gameLayout.add(shipPanel, BorderLayout.EAST);

        add(gameLayout);
        setTitle("Battleship");
        setSize(new Dimension(400, 400));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setResizable(false);
        setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
    }

    /**
     * changed title if won
     */
    public void displayWin() {
        gameLabel.setText("You Win!");
    }

    /**
     * changes title if loss
     */
    public void displayLose() {
        gameLabel.setText("You Lose!");
    }

    /**
     * returns if the the direction from the given point has already been guessed
     * @param row the given row
     * @param col the given col
     * @param dir the given direction
     * @return true if the next point has not yet been guessed
     */
    private boolean nextNotAlreadyGuessed(int row, int col, String dir) {
        boolean alreadyGuessed = true;
        switch (dir) {
            case "Right":
                alreadyGuessed = alreadyGuessedList.contains(new Point(row, col + 1));
                break;
            case "Down":
                alreadyGuessed = alreadyGuessedList.contains(new Point(row + 1, col));
                break;
            case "Left":
                alreadyGuessed = alreadyGuessedList.contains(new Point(row, col - 1));
                break;
            case "Up":
                alreadyGuessed = alreadyGuessedList.contains(new Point(row - 1, col));
                break;
        }
        return !alreadyGuessed;
    }

    /**
     * checks if the next direction is within bounds
     * @param row given row
     * @param col given col
     * @param dir given dir
     * @return true if the next point in given direction is within bounds
     */
    private boolean validDir(int row, int col, String dir) {
        boolean valid = false;
        switch (dir) {
            case "Right":
                valid = isPositionValid(row, col + 1);
                break;
            case "Down":
                valid = isPositionValid(row + 1, col);
                break;
            case "Left":
                valid = isPositionValid(row, col - 1);
                break;
            case "Up":
                valid = isPositionValid(row - 1, col);
                break;
        }
        return valid;
    }

    /**
     * adds given point to list of already guessed points
     * @param row given row
     * @param col given col
     */
    private void addToList(int row, int col) {
        alreadyGuessedList.add(new Point(row, col));
    }

    /**
     * randomizes the row and col variable
     */
    private void randomize() {
        randRow = (int) (Math.random() * SIZE);
        randCol = (int) (Math.random() * SIZE);
    }

    /**
     * checks if the next shot will hit the ship
     * @param row original row
     * @param col original col
     * @param dir direction to fire
     */
    private void SinkShipAttempt(int row, int col, String dir) {
        int newRandCol;
        int newRandRow;
        switch (dir) {
            case "Right":
                newRandCol = col + 1;
                ifHit(row, newRandCol, "Right");
                break;
            case "Down":
                newRandRow = row + 1;
                ifHit(newRandRow, col, "Down");
                break;
            case "Left":
                newRandCol = col - 1;
                ifHit(row, newRandCol, "Left");
                break;
            case "Up":
                newRandRow = row - 1;
                ifHit(newRandRow, col, "Up");
                break;
        }

    }

    /**
     * checks to see if the next shot will hit a ship
     * @param row row to shoot
     * @param col col to shoot
     */
    private void ifHit(int row, int col, String type) {
        //TODO bugfix likely here
//        boolean hit = guess(row, col);
        Ship tempShip;
        tempShip = shipGrid[row][col];
            if (tempShip.isSunk()) {
                shipHit = false;

            } else {
//                displayStrike(row, col, tempShip);
                newRow = row;
                newCol = col;
//                bypass = true;
            }

    }

    /**
     * starts the turn for the AI
     */
    public void aiTurn() {
        boolean hit;
        boolean found = false;
        Ship tempShip;
        if (shipHit) {
            if (!bypass) {
                found = predict(randRow, randCol);
            }
            if (found) {
                switch (predictDir) {
                    case "Right":
                        SinkShipAttempt(newRow, newCol, "Right");
                        break;
                    case "Down":
                        SinkShipAttempt(newRow, newCol, "Down");
                        break;
                    case "Left":
                        SinkShipAttempt(newRow, newCol, "Left");
                        break;
                    case "Up":
                        SinkShipAttempt(newRow, newCol, "Up");
                        break;
                }
            }


        } else {

            do {
                randomize();
                alreadyGuessed = alreadyGuessedList.contains(new Point(randRow, randCol));
            } while (alreadyGuessed);
            newRow = randRow;
            newCol = randCol;

            hit = guess(randRow, randCol);
            tempShip = shipGrid[randRow][randCol];
            JButton button = getField(randRow, randCol);

        }


    }

    /**
     * guesses if a ship is hit
     * @param row row to shoot
     * @param col col to shoot
     * @return true if a ship is hit
     */
    private boolean guess(int row, int col) {
        addToList(row, col);
        Ship tempShip = shipGrid[row][col];
        JButton button = getField(row, col);

        if (tempShip == null) {
            button.setBackground(new Color(251, 255, 255));
        } else {
            if (tempShip.Sink()) {
                displayStrike(row, col, tempShip);
                userShipDestroyed++;
                if (userShipDestroyed == TOTAL_SHIP_NUM) {
                    displayLose();
                }
            } else {
                displayStrike(row, col, tempShip);
                shipHit = true;
            }

        }


        return tempShip != null;
    }

    /**
     * predicts direction of next shot
     *
     * @param row original row
     * @param col original col
     */
    private boolean predict(int row, int col) {
        boolean hit;
        if (validDir(row, col, "Right") && nextNotAlreadyGuessed(row, col, "Right")) {
            hit = guess(row, col + 1);
            if (hit) {
                predictDir = "Right";
                return true;
            } else {
                return false;
            }
        } else if (validDir(row, col, "Down") && nextNotAlreadyGuessed(row, col, "Down")) {
            hit = guess(row + 1, col);
            if (hit) {
                predictDir = "Down";
                return true;
            } else {
                return false;
            }
        } else if (validDir(row, col, "Left") && nextNotAlreadyGuessed(row, col, "Left")) {
            hit = guess(row, col - 1);
            if (hit) {
                predictDir = "Left";
                return true;
            } else {
                return false;
            }
        } else if (validDir(row, col, "Up") && nextNotAlreadyGuessed(row, col, "Up")) {
            hit = guess(row - 1, col);
            if (hit) {
                predictDir = "Up";
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * gets the button at given coordinates
     * @param col given col
     * @param row given row
     * @return
     */
    private JButton getField(int col, int row) {
        return shipFieldGUI[row][col];
    }

    /**
     * displays sunk ship
     * @param ship given ship
     */
    private void displaySink(Ship ship) {
        int row = 0;
        int col = 0;
        JButton button = new JButton();
        int length = ship.getLength();
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;


        // set position
        c.gridx = col;
        c.gridy = row;

        // button dimensions
        int width = BUTTON_SIZE;
        int height = BUTTON_SIZE;

        // occupy ship field in grid
        if (ship.isVertical()) {
            // vertical
            c.gridwidth = 1;
            c.gridheight = length;
            height = BUTTON_SIZE * length;

        } else {
            // horizontal
            c.gridwidth = length;
            c.gridheight = 1;
            width = BUTTON_SIZE * length;
        }

        // select the ship type's preferences
        button.setPreferredSize(new Dimension(width, height));

        // remove old elements
        removeFields(row, col, ship.isVertical(), length);
        button.setBackground(new Color(255, 0, 0));

        // add new button and update the grid
        shipPanel.add(button, c);
        shipPanel.revalidate();
        shipPanel.repaint();
    }

    /**
     * displays strike on a ship
     * @param row row to strike
     * @param col col to strike
     * @param ship ship struck
     */
    private void displayStrike(int row, int col, Ship ship) {
        // create new button
        JButton button = new JButton();
        button.setLayout(null);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createLineBorder(new Color(0, 1, 180)));


        // grid prefs for the new button
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;

        // set position
        c.gridx = col;
        c.gridy = row;

        // button dimensions

        // occupy ship field in grid
        c.gridwidth = 1;
        c.gridheight = 1;

        // select the ship type's preferences
        button.setPreferredSize(new Dimension(BUTTON_SIZE, BUTTON_SIZE));

        // remove old elements
        removeFields(row, col, ship.isVertical(), ship.getLength());
        shipPanel.revalidate();
        shipPanel.repaint();
        button.setBackground(Color.red);

        // add new button and update the grid
        shipPanel.add(button, c);
//        getField(row, col).add(button);
        shipPanel.revalidate();
        shipPanel.repaint();
    }

    /**
     * remove fields at given point
     * @param row row to be removed
     * @param col col to be removed
     * @param vertical orientation of ship
     * @param length length of ship
     */
    private void removeFields(int row, int col, boolean vertical, int length) {
        //TODO remove for loop
        for (int i = 0; i < length; i++) {
            if (vertical) {
                shipPanel.remove(getField(row, col));
            } else {
                shipPanel.remove(getField(row, col));
            }
        }
    }

    /**
     * checks if given row and col are in bounds
     * @param row given row
     * @param col given col
     * @return true if in bounds
     */
    private boolean isPositionValid(int row, int col) {
        boolean available = true;
        if (row >= SIZE || row < 0) {
            available = false;
        } else {
            if (col >= SIZE || col < 0) {
                available = false;
            }
        }
        return available;
    }

}
